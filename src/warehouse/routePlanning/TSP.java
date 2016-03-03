package warehouse.routePlanning;

import java.util.LinkedList;
import java.util.Optional;

import warehouse.Action;
import warehouse.Action.MoveAction;
import warehouse.ItemPickup;
import warehouse.Location;
import warehouse.Route;
import warehouse.job.Job;

public class TSP {
	private Search s = new Search();
	private LinkedList<Action> moves;
	private final Location dropOffLocation = new Location(4, 7);
	private int numberOfNodes;
	LinkedList<Edge> bestRoute = new LinkedList<Edge>();
	LinkedList<Location> finalRoute = new LinkedList<Location>();
	double lowestWeight;
	// stores the route taken to travel between each node
	private LinkedList<Location>[][] routeMatrix;

	public Route getShortestRoute(Job j, Location startingPosition) {
		numberOfNodes = j.pickups.size() + 2;
		double[][] adjacencyMatrix = new double[numberOfNodes][numberOfNodes];
		adjacencyMatrix = initiateMatrix(adjacencyMatrix);
		adjacencyMatrix = adjMatrix(adjacencyMatrix, makeListLocations(j, startingPosition));
		moves = new LinkedList<Action>();

		// initialises array of the amount of edges left available to each node
		int[] edgesLeft = new int[numberOfNodes];
		for (int x = 0; x < edgesLeft.length; x++) {
			edgesLeft[x] = numberOfNodes - 1;
		}
		// initialises array of the amount of edges connected to each node
		int[] edgesConnected = new int[numberOfNodes];
		for (int x = 0; x < edgesLeft.length; x++) {
			edgesConnected[x] = 0;
		}

		// initialises list of groups
		LinkedList<LinkedList<Edge>> currentGroups = new LinkedList<LinkedList<Edge>>();

		// reset lowestWeight
		lowestWeight = Double.POSITIVE_INFINITY;

		// set up fresh route
		LinkedList<Edge> route = new LinkedList<Edge>();

		// starts the first call of the route finding algorithm
		getRoute(adjacencyMatrix, route, edgesLeft, edgesConnected, 0, 1, currentGroups);

		return new Route(moves, startingPosition, j.dropLocation);
	}

	/**
	 * Method which calculates the lower bound for a subset of possible routes
	 *
	 * @param adjacencyMatrix
	 *            The adjacency matrix which stores all of the connections
	 *            between my nodes
	 * @return lowerBound - The lowest possible weight of that a subset of
	 *         possible routes could have
	 */
	private double findLowerBound(double[][] adjacencyMatrix) {
		// finds the lower bound for the rest of the route from the current node
		// by looking through current adjacency matrix.
		double lowerBound = 0;
		for (int currentNode = 0; currentNode < adjacencyMatrix.length; currentNode++) {
			double bestWeight = Double.POSITIVE_INFINITY;
			double secondWeight = Double.POSITIVE_INFINITY;

			// most wanted loop for two lowest weights
			for (int connectedNode = 0; connectedNode < adjacencyMatrix.length; connectedNode++) {
				double foundWeight = adjacencyMatrix[currentNode][connectedNode];
				if (foundWeight < secondWeight) {
					if (foundWeight < bestWeight) {
						secondWeight = bestWeight;
						bestWeight = foundWeight;
					} else {
						secondWeight = foundWeight;
					}
				}
			}
			lowerBound = lowerBound + secondWeight + bestWeight;
		}
		lowerBound = lowerBound / 2;
		return lowerBound;
	}

	/**
	 * creates an adjacency matrix to contain the distances between every node
	 * is the system
	 *
	 * @param adjacencyMatrix
	 *            The adjacency matrix which stores all of the connections
	 *            between my nodes
	 * @param destinations
	 *            The list of Nodes which the user has chosen to visit
	 * @param listOfNodes
	 *            The list of every Node in the network
	 * @return adjacencyMatrix - The adjacency matrix which stores all of the
	 *         connections between my nodes
	 */
	private double[][] adjMatrix(double[][] adjacencyMatrix, LinkedList<Location> allLocations) {
		for (int Node1 = 0; Node1 < allLocations.size() - 1; Node1++) {
			for (int Node2 = Node1 + 1; Node2 < allLocations.size(); Node2++) {
				Optional<Route> foundRoute = s.getRoute(allLocations.get(Node1), allLocations.get(Node2));
				if (foundRoute.isPresent()) {
					Route r = foundRoute.get();
					adjacencyMatrix[Node1][Node2] = r.totalDistance;
					adjacencyMatrix[Node2][Node1] = r.totalDistance;
					for (Action foundLocation: r.actions) {
						Action.MoveAction location = (MoveAction) foundLocation;
						routeMatrix[Node1][Node2].add(location.destination);
					}
					foundRoute = dj.completeDijkstras(destinations.get(Node2), destinations.get(Node1), listOfNodes);
					for (Node foundNode : foundRoute) {
						routeMatrix[Node2][Node1].add(foundNode);
					}
				}
			}
		}
		return adjacencyMatrix;
	}

	/**
	 * Fills the adjacency matrix with values of infinity to represent no
	 * connection before values are added
	 *
	 * @param adjacencyMatrix
	 *            The adjacency matrix which stores all of the connections
	 *            between my nodes
	 * @return adjacencyMatrix - The adjacency matrix which stores all of the
	 *         connections between my nodes
	 */
	private double[][] initiateMatrix(double[][] adjacencyMatrix) {
		for (double[] adjacencyMatrix1 : adjacencyMatrix) {
			for (int j = 0; j < adjacencyMatrix.length; j++) {
				adjacencyMatrix1[j] = Double.POSITIVE_INFINITY;
			}
		}
		return adjacencyMatrix;
	}

	/**
	 * Fills the route matrix with values of infinity to represent no connection
	 * before values are added
	 *
	 * @param routeMatrix
	 *            Stores the route taken between each destination
	 * @return routeMatrix - Stores the route taken between each destination
	 */
	private List<Node>[][] initiateRouteMatrix(List<Node>[][] routeMatrix) {
		for (List<Node>[] routeMatrix1 : routeMatrix) {
			for (int j = 0; j < routeMatrix.length; j++) {
				routeMatrix1[j] = new ArrayList<>();
			}
		}
		return routeMatrix;
	}

	/**
	 * Finds the shortest route by a branch and bound algorithm splits the route
	 * into two possible route, one including a specific edge, and one avoiding
	 * it. Checks the impact this has on the possible edges this route can
	 * include in future (including or removing certain edges makes other edges
	 * essential or impossible). Finds the lower bound for each of these routes
	 * to check if either of these routes can be pruned (not checked) since
	 * their lower bounds are higher than the best route already found. If one
	 * or both subsets remain then check if the route with the smallest lower
	 * bound has formed a complete route: if not then call the getShortestRoute
	 * method again with new values. Once it finally hits a complete route for
	 * this subgroup it will return up to then check if it can prune the
	 * remaining child or then call this method again with the values of this
	 * second split.
	 *
	 * @param adjacencyMatrix
	 *            The adjacency matrix which stores all of the connections
	 *            between my nodes
	 * @param route
	 *            The current route for this subset of possible routes
	 * @param edgesLeft
	 *            The amount of edges still available to each specific node
	 * @param edgesConnected
	 *            The amount of edges connected to each specific node
	 * @param currentNode
	 *            The value of the current node which the connections are being
	 *            decided for
	 * @param connectedNode
	 *            The value of the node which the current node is either being
	 *            connected to or removed from
	 * @param currentGroups
	 *            The list of groups (separate sections of route) which are
	 *            involved in this subset of possible routes
	 */
	private void getRoute(double[][] adjacencyMatrix, List<Pair> route, int[] edgesLeft, int[] edgesConnected,
			int currentNode, int connectedNode, List<List<Pair>> currentGroups) {
		// split into two routes and adjacency matrices
		Pair temp = new Pair();
		temp.setNode1(currentNode);
		temp.setNode2(connectedNode);

		// list of routes used to pass on both halves of split
		List<List<Pair>> routes = new ArrayList<>();

		// route for including edge
		List<Pair> route1 = new ArrayList<>();
		for (Pair z : route) {
			route1.add(z);
		}
		route1.add(temp);

		// route for removing edge
		List<Pair> route2 = new ArrayList<>();
		for (Pair z : route) {
			route2.add(z);
		}

		routes.add(route1);
		routes.add(route2);

		// matrix used to pass on adjacency matrices for both halves of split
		int length = adjacencyMatrix.length;
		double[][][] matrices = new double[2][length][length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				matrices[0][i][j] = adjacencyMatrix[i][j];
				matrices[1][i][j] = adjacencyMatrix[i][j];
			}
		}

		matrices[1][currentNode][connectedNode] = Double.POSITIVE_INFINITY;
		matrices[1][connectedNode][currentNode] = Double.POSITIVE_INFINITY;

		// edges connected split
		int[][] allEdges = new int[2][edgesConnected.length];
		for (int j = 0; j < edgesConnected.length; j++) {
			allEdges[0][j] = edgesConnected[j];
			allEdges[1][j] = edgesConnected[j];
		}
		allEdges[0][currentNode]++;
		allEdges[0][connectedNode]++;

		// edges left split
		edgesLeft[currentNode]--;
		edgesLeft[connectedNode]--;
		int[][] allEdgesLeft = new int[2][edgesLeft.length];
		for (int j = 0; j < edgesLeft.length; j++) {
			allEdgesLeft[0][j] = edgesLeft[j];
			allEdgesLeft[1][j] = edgesLeft[j];

		}

		// groups split
		List<List<Pair>>[] groupsArray = new ArrayList[2];
		groupsArray[0] = new ArrayList<>();
		groupsArray[1] = new ArrayList<>();
		for (List<Pair> group : currentGroups) {
			for (Pair p : group) {
				groupsArray[0] = addToGroups(groupsArray[0], p);
				groupsArray[1] = addToGroups(groupsArray[1], p);
			}
		}
		groupsArray[0] = addToGroups(groupsArray[0], temp);

		// checks impact of adding or removing nodes on the adjacency matrix and
		// modifies accordingly
		for (int x = 0; x < matrices.length; x++) {
			List<Pair> currentRoute = routes.get(x);
			List<List<Pair>> groups = groupsArray[x];
			double[][] currentMatrix = matrices[x];
			int[] currentConnectedEdges = allEdges[x];
			int[] edgesLeftNew = allEdgesLeft[x];

			// removes any possible remaining connections to nodes which are
			// already fully connected
			for (int i = 0; i < currentConnectedEdges.length; i++) {
				if (currentConnectedEdges[i] == 2) {
					for (int linkedNode = 0; linkedNode < currentMatrix.length; linkedNode++) {
						boolean notAdded = addedCheck(currentRoute, i, linkedNode);
						if (notAdded && currentMatrix[i][linkedNode] != Double.POSITIVE_INFINITY) {
							// removes edge from the adjacency matrix
							currentMatrix[i][linkedNode] = Double.POSITIVE_INFINITY;
							currentMatrix[linkedNode][i] = Double.POSITIVE_INFINITY;
							edgesLeftNew[linkedNode]--;
							edgesLeftNew[i]--;
						}
					}
				}
			}

			// check to see if an edge must be added as it would not be possible
			// for either node to have two edges without it
			for (int i = 0; i < edgesLeftNew.length; i++) {
				// finds how many edges are left unconnected, if there are 2
				// left unconnected and there are
				// currently none connected to that node, then connect both of
				// them.
				// if there is only one edge left and one connected then add the
				// last edge
				if (edgesLeftNew[i] == 2) {
					if (currentConnectedEdges[i] == 0) {
						// find both of the edges which need to be added to the
						// route
						for (int linkedNode = 0; linkedNode < currentMatrix.length; linkedNode++) {
							boolean notAdded = addedCheck(currentRoute, i, linkedNode);
							if (notAdded && currentMatrix[i][linkedNode] != Double.POSITIVE_INFINITY) {
								// adds the edge to the route and updates values
								Pair temp1 = new Pair();
								temp1.setNode1(i);
								temp1.setNode2(linkedNode);
								currentRoute.add(temp1);
								edgesLeftNew[i]--;
								edgesLeftNew[linkedNode]--;
								currentConnectedEdges[i]++;
								currentConnectedEdges[linkedNode]++;
								groups = addToGroups(groups, temp1);
							}
						}
					}

				} else if (edgesLeftNew[i] == 1) {
					if (currentConnectedEdges[i] == 1) {
						// finds edge which needs to be added
						for (int linkedNode = 0; linkedNode < currentMatrix.length; linkedNode++) {
							boolean notAdded = addedCheck(currentRoute, i, linkedNode);
							if (notAdded && currentMatrix[i][linkedNode] != Double.POSITIVE_INFINITY) {
								// adds the edge to the route and updates values
								Pair temp2 = new Pair();
								temp2.setNode1(i);
								temp2.setNode2(linkedNode);
								currentRoute.add(temp2);
								edgesLeftNew[i]--;
								edgesLeftNew[linkedNode]--;
								currentConnectedEdges[i]++;
								currentConnectedEdges[linkedNode]++;
								groups = addToGroups(groups, temp2);
							}
						}
					}
				} else if (edgesLeftNew[i] == 0) {
					if (currentConnectedEdges[i] != 2) {
						// too many edges have been removed
					}
				}
			}

			if (currentRoute.size() > 1) {
				// checks through the entire rest of the adjacency matrix
				for (int checkNode1 = currentNode; checkNode1 < adjacencyMatrix.length - 1; checkNode1++) {
					for (int checkNode2 = checkNode1 + 1; checkNode2 < adjacencyMatrix.length; checkNode2++) {
						boolean notAdded = addedCheck(currentRoute, checkNode1, checkNode2);
						if (notAdded && currentMatrix[checkNode1][checkNode2] != Double.POSITIVE_INFINITY) {

							// check to see if edge would create a node with
							// more than two edges or create a cycle
							int[] foundPerGroup = new int[groups.size()];
							for (int i = 0; i < groups.size(); i++) {
								for (Pair k : groups.get(i)) {
									if (x == 0) {
									}
									if (k.getNode1() == checkNode1 || k.getNode1() == checkNode2) {
										foundPerGroup[i]++;
									}
									if (k.getNode2() == checkNode1 || k.getNode2() == checkNode2) {
										foundPerGroup[i]++;
									}
								}
							}

							// if the check found an edge which could be
							// removed, then remove it from matrix and list of
							// edges left
							for (int found : foundPerGroup) {
								if (found >= 2) {
									// remove from the adjacency matrix
									currentMatrix[checkNode1][checkNode2] = Double.POSITIVE_INFINITY;
									currentMatrix[checkNode2][checkNode1] = Double.POSITIVE_INFINITY;
									edgesLeftNew[checkNode1]--;
									edgesLeftNew[checkNode2]--;
								}
							}

						}

					}

				}
			} // else no point in checking

			for (int i = 0; i < edgesLeftNew.length; i++) {
				if (edgesLeftNew[i] == 2) {
					if (currentConnectedEdges[i] == 0) {
						// finds the two remaining edges which need to be added
						// to the route
						for (int linkedNode = 0; linkedNode < currentMatrix.length; linkedNode++) {
							boolean notAdded = addedCheck(currentRoute, i, linkedNode);
							if (notAdded && currentMatrix[i][linkedNode] != Double.POSITIVE_INFINITY) {
								// adds edge to route
								Pair temp1 = new Pair();
								temp1.setNode1(i);
								temp1.setNode2(linkedNode);
								currentRoute.add(temp1);
								edgesLeftNew[i]--;
								edgesLeftNew[linkedNode]--;
								currentConnectedEdges[i]++;
								currentConnectedEdges[linkedNode]++;
								groups = addToGroups(groups, temp1);

							}
						}
					}

				} else if (edgesLeftNew[i] == 1) {
					if (currentConnectedEdges[i] == 1) {
						// finds edge which needs to be added
						for (int linkedNode = 0; linkedNode < currentMatrix.length; linkedNode++) {
							boolean notAdded = addedCheck(currentRoute, i, linkedNode);
							if (notAdded && currentMatrix[i][linkedNode] != Double.POSITIVE_INFINITY) {
								// adds the edge to the route and updates values
								Pair temp2 = new Pair();
								temp2.setNode1(i);
								temp2.setNode2(linkedNode);
								currentRoute.add(temp2);
								edgesLeftNew[i]--;
								edgesLeftNew[linkedNode]--;
								currentConnectedEdges[i]++;
								currentConnectedEdges[linkedNode]++;
								groups = addToGroups(groups, temp2);
							}
						}
					}
				} else if (edgesLeftNew[i] == 0) {
					if (currentConnectedEdges[i] != 2) {
						// too many edges have been removed
					}
				}
			}

			// updates the arrays for both halves of split with new values for
			// this half
			// arrays are copied manually to avoid errors occurring from being
			// passed by reference
			for (int i = 0; i < currentRoute.size(); i++) {
				routes.get(x).set(i, currentRoute.get(i));
			}
			for (int i = 0; i < currentMatrix.length; i++) {
				for (int j = 0; j < currentMatrix.length; j++) {
					matrices[x][i][j] = currentMatrix[i][j];
				}
			}
			for (int j = 0; j < currentConnectedEdges.length; j++) {
				allEdges[x][j] = currentConnectedEdges[j];
			}

			for (int j = 0; j < edgesLeftNew.length; j++) {
				allEdgesLeft[x][j] = edgesLeftNew[j];
			}

			for (int i = 0; i < groups.size(); i++) {
				groupsArray[x].set(i, groups.get(i));
			}

		}

		// finds the lower bound of each split
		double[] lowerBounds = new double[2];
		for (int i = 0; i < lowerBounds.length; i++) {
			lowerBounds[i] = findLowerBound(matrices[i]);
		}

		// deals with the two different routes in order of lowest lower bound
		// first
		// checks which route has the lowest lower bound
		if (lowerBounds[0] > lowerBounds[1]) {
			int split = 1;
			// checks if we can prune either of the splits (i.e. not bother
			// checking their children as their lower bounds are higher than the
			// current best weight)
			if (lowerBounds[split] < lowestWeight) {
				// check if the branch is complete
				boolean complete = checkComplete(allEdges, split);
				if (complete) {
					// branch is complete
					// checks to see if it is the new best route
					if (lowerBounds[split] < lowestWeight) {
						lowestWeight = lowerBounds[split];
						bestRoute = routes.get(split);
					}
					// prune other route as this is now the shortest length and
					// it had a larger lower bound
				} else {
					// run next split
					call(routes, matrices, allEdges, allEdgesLeft, currentNode, connectedNode, split, groupsArray);

					// check the other route
					split = 0;
					if (lowerBounds[split] < lowestWeight) {
						// check if the branch is complete
						complete = checkComplete(allEdges, split);
						if (complete) {
							// branch is complete
							// checks to see if it is the new best route
							if (lowerBounds[split] < lowestWeight) {
								lowestWeight = lowerBounds[split];
								bestRoute = routes.get(split);
							}
						} else {
							// run next split
							call(routes, matrices, allEdges, allEdgesLeft, currentNode, connectedNode, split,
									groupsArray);
						}
					}
				}
			}
		} else {
			int split = 0;
			// checks if we can prune either of the splits (i.e. not bother
			// checking their children as their lower bounds are higher than the
			// current best weight)
			if (lowerBounds[0] < lowestWeight) {
				// check if the branch is complete
				boolean complete = checkComplete(allEdges, split);
				if (complete) {
					// branch is complete
					// checks to see if it is the new best route
					if (lowerBounds[split] < lowestWeight) {
						lowestWeight = lowerBounds[split];
						bestRoute = routes.get(split);
					}
					// prune other route as this is now the shortest length and
					// it had a larger lower bound
				} else {
					// run next split
					call(routes, matrices, allEdges, allEdgesLeft, currentNode, connectedNode, split, groupsArray);

					// check other route
					split = 1;
					if (lowerBounds[split] < lowestWeight) {
						// checks if branch is complete
						complete = checkComplete(allEdges, split);
						if (complete) {
							// branch is complete
							// checks to see if it is the new best route
							if (lowerBounds[split] < lowestWeight) {
								lowestWeight = lowerBounds[split];
								bestRoute = routes.get(split);
							}
							// prune other route as this is now the shortest
							// length
							// and it had a larger lower bound
						} else {
							// run next split
							call(routes, matrices, allEdges, allEdgesLeft, currentNode, connectedNode, split,
									groupsArray);
						}
					}
				}
			}
		}
	}

	/**
	 * Calls the getShortestRoute method with the next set of values
	 *
	 * @param routes
	 *            The two different routes which are created by splitting the
	 *            previous route into two subsets.
	 * @param matrices
	 *            The two adjacency matrices made by splitting the previous
	 *            route into subsets
	 * @param allEdges
	 *            The two different lists containing the amount of edges already
	 *            connected for each node for both splits
	 * @param allEdgesLeft
	 *            The two different lists containing the amount of edges still
	 *            available for each node for both splits
	 * @param currentNode
	 *            The value of the current node which the connections are being
	 *            decided for
	 * @param connectedNode
	 *            The value of the node which the current node is either being
	 *            connected to or removed from
	 * @param split
	 *            A number which states which half of the split is being passed
	 *            down
	 * @param groupsArray
	 *            The two different lists of groups for both splits
	 */
	private void call(List<List<Pair>> routes, double[][][] matrices, int[][] allEdges, int[][] allEdgesLeft,
			int currentNode, int connectedNode, int split, List<List<Pair>>[] groupsArray) {
		// calls the next layer of recursion with split values
		List<Pair> currentRoute = routes.get(split);
		double[][] currentMatrix = matrices[split];
		int[] currentConnectedEdges = allEdges[split];
		int[] edgesLeftNew = allEdgesLeft[split];
		List<List<Pair>> currentGroups = groupsArray[split];

		// calls the next layer of function using split values
		// increment connected and current nodes
		// if it has reached the end of a row in the adjacency matrix, then
		// increment current node and set connected node to currentNode +1;
		Boolean available;
		do {
			if (connectedNode + 1 >= currentMatrix.length) {
				// increment currentNode and set connected to be current +1;
				if (currentNode + 2 < currentMatrix.length) {
					// safe to increment
					currentNode++;
					connectedNode = currentNode + 1;
				} else {
					// REACHED END OF ARRAY
				}
			} else {
				// increment connectedNode
				connectedNode++;
			}
			available = addedCheck(currentRoute, currentNode, connectedNode);
		} while (currentMatrix[currentNode][connectedNode] == Double.POSITIVE_INFINITY || !available);

		// call function with values found
		getShortestRoute(currentMatrix, currentRoute, edgesLeftNew, currentConnectedEdges, currentNode, connectedNode,
				currentGroups);

	}

	/**
	 * Checks if the current route is a complete route which contains all the
	 * chosen destinations
	 *
	 * @param allEdges
	 *            The two different lists containing the amount of edges already
	 *            connected for each node for both splits
	 * @param split
	 *            A number which states which half of the split is being passed
	 *            down
	 * @return complete - Boolean to mark whether the route is complete or not
	 */
	private boolean checkComplete(int[][] allEdges, int split) {
		// checks if this branch is complete
		boolean complete = true;
		for (int connected : allEdges[split]) {
			if (connected != 2) {
				complete = false;
			}
		}
		return complete;
	}

	/**
	 * Checks if a given edge has already been added to the route
	 *
	 * @param route
	 *            The current route for this subset of possible routes
	 * @param currentNode
	 *            The value of the current node which the connections are being
	 *            decided for
	 * @param connectedNode
	 *            The value of the node which the current node is either being
	 *            connected to or removed from
	 * @return available - Boolean, true if the node has not been used as part
	 *         of the route
	 */
	private boolean addedCheck(List<Pair> route, int currentNode, int connectedNode) {
		boolean available = true;
		for (Pair p : route) {
			if ((p.getNode1() == currentNode && p.getNode2() == connectedNode)
					|| (p.getNode1() == connectedNode && p.getNode2() == currentNode)) {
				available = false;
			}
		}
		return available;
	}

	/**
	 * assembles the final route around all destinations
	 *
	 * @param startNode
	 *            The user's chosen starting node, route must start and finish
	 *            here
	 * @param destinations
	 *            The list of Nodes which the user has chosen to visit
	 */
	private void getFinalRoute(Node startNode, List<Node> destinations) {
		Node[][] position = new Node[bestRoute.size()][2];
		for (int pos = 0; pos < bestRoute.size(); pos++) {
			position[pos][0] = destinations.get(bestRoute.get(pos).getNode1());
			position[pos][1] = destinations.get(bestRoute.get(pos).getNode2());
		}

		// ensure route is empty
		finalRoute.clear();
		// begin recursion to create the route
		createRoute(position, startNode, destinations);
		finalRoute.add(startNode);
	}

	/**
	 * method which recursively creates the final route
	 *
	 * @param position
	 *            An array which contains both nodes from all the pairs in the
	 *            best route found separately
	 * @param nodeToFind
	 *            The node which is being looked for in this round of recursion
	 * @param destinations
	 *            The list of Nodes which the user has chosen to visit
	 */
	private void createRoute(Node[][] position, Node nodeToFind, List<Node> destinations) {
		// searches for node
		for (int x = 0; x < bestRoute.size(); x++) {
			for (int y = 0; y < 2; y++) {
				if (position[x][y] == nodeToFind) {
					int z = 1 - y;
					nodeToFind = position[x][z];
					position = addRoute(position, destinations, x, y, z);
					createRoute(position, nodeToFind, destinations);
				}
			}
		}
	}

	/**
	 * Adds a section of route to the final route
	 *
	 * @param position
	 *            An array which contains both nodes from all the pairs in the
	 *            best route found separately
	 * @param destinations
	 *            The list of Nodes which the user has chosen to visit
	 * @param x
	 *            The value for which Pair location in the position array the
	 *            node was found in
	 * @param y
	 *            The value for which Node location in the position array the
	 *            node was found in
	 * @param z
	 *            The value for which Node location in the position array the
	 *            next node to find is in
	 * @return position An array which contains both nodes from all the pairs in
	 *         the best route found separately
	 */
	private Node[][] addRoute(Node[][] position, List<Node> destinations, int x, int y, int z) {
		// adds a route to the final route
		List<Node> foundRoute = routeMatrix[findLocation(position[x][y], destinations)][findLocation(position[x][z],
				destinations)];
		for (int i = 0; i < foundRoute.size() - 1; i++) {
			finalRoute.add(foundRoute.get(i));
		}
		position[x][y] = null;
		position[x][z] = null;
		return position;
	}

	/**
	 * Finds the location of a specific Node within the destinations list
	 *
	 * @param n
	 *            The node to find the location of
	 * @param destinations
	 *            The list of Nodes which the user has chosen to visit
	 * @return location - The location the node within the destinations list
	 */
	private int findLocation(Node n, List<Node> destinations) {
		int location = 0;
		for (int x = 0; x < destinations.size(); x++) {
			if (destinations.get(x) == n) {
				location = x;
			}
		}
		return location;
	}

	/**
	 * Adds a new edge to whichever group it belongs in and merges groups if
	 * necessary
	 *
	 * @param currentGroups
	 *            The list of groups (separate sections of route) which are
	 *            involved in this subset of possible routes
	 * @param pairToAdd
	 *            The Pair which needs to be added to a group
	 * @return currentGroups - The list of groups (separate sections of route)
	 *         which are involved in this subset of possible routes
	 */
	private List<List<Pair>> addToGroups(List<List<Pair>> currentGroups, Pair pairToAdd) {
		// adds a new edge to whichever group it belongs in and merges groups
		// checks to see if it is the first node ever added
		if (currentGroups.isEmpty()) {
			List<Pair> g = new ArrayList<>();
			g.add(pairToAdd);
			currentGroups.add(g);
		} else {
			// initialises boolean array
			boolean[] groupsAddedTo = new boolean[currentGroups.size()];
			for (int x = 0; x < groupsAddedTo.length; x++) {
				groupsAddedTo[x] = false;
			}
			// finds which groups to add to
			for (int i = 0; i < currentGroups.size(); i++) {
				for (Pair p : currentGroups.get(i)) {
					if (p.getNode1() == pairToAdd.getNode1() || p.getNode1() == pairToAdd.getNode2()
							|| p.getNode2() == pairToAdd.getNode1() || p.getNode2() == pairToAdd.getNode2()) {
						groupsAddedTo[i] = true;
					}
				}
			}
			// adds to groups and merges groups if needed
			int numberOfAdds = 0;
			for (int x = 0; x < groupsAddedTo.length; x++) {
				if (groupsAddedTo[x]) {
					if (numberOfAdds == 0) {
						currentGroups.get(x).add(pairToAdd);
						numberOfAdds++;
					} else {
						for (int i = 0; i < groupsAddedTo.length; i++) {
							if (groupsAddedTo[i]) {
								if (x != i) {
									currentGroups.get(x).addAll(currentGroups.get(i));
									currentGroups.remove(i);
								}
							}
						}
					}
				}
			}
			// creates a new group if it did not fit into any groups
			if (numberOfAdds == 0) {
				List<Pair> g = new ArrayList<>();
				g.add(pairToAdd);
				currentGroups.add(g);
			}
		}

		return currentGroups;
	}

	private LinkedList<Location> makeListLocations(Job j, Location startPosition) {
		LinkedList<Location> allLocations = new LinkedList<Location>();
		allLocations.add(startPosition);
		for (ItemPickup x : j.pickups) {
			allLocations.add(x.location);
		}
		allLocations.add(dropOffLocation);
		return allLocations;
	}
}