package warehouse.routePlanning.util;

import java.util.ArrayList;
import java.util.LinkedList;

import warehouse.util.Location;

public class Branch {
	private double[][] adjacencyMatrix;
	private LinkedList<Edge> route;
	private int[] edgesLeft;
	private int[] edgesConnected;
	private int currentNode;
	private int connectedNode;
	private LinkedList<LinkedList<Edge>> currentGroups;
	private double lowerBound;

	/**
	 * Constructor which creates a branch given the branch information
	 * 
	 * @param adjacencyMatrix
	 *            the matrix which contains the distance for every edge
	 * @param route
	 *            the current route
	 * @param edgesLeft
	 *            the current amount of edges left for each node
	 * @param edgesConnected
	 *            the current amount of edges connected for each node
	 * @param currentNode
	 *            the current node being inspected
	 * @param connectedNode
	 *            the next node to be inspected
	 * @param currentGroups
	 *            the list of groups for this branch
	 */
	public Branch(double[][] adjacencyMatrix, LinkedList<Edge> route, int[] edgesLeft, int[] edgesConnected,
			int currentNode, int connectedNode, LinkedList<LinkedList<Edge>> currentGroups) {
		setAdjacencyMatrix(adjacencyMatrix);
		setRoute(route);
		setEdgesLeft(edgesLeft);
		setEdgesConnected(edgesConnected);
		setCurrentNode(currentNode);
		setConnectedNode(connectedNode);
		setCurrentGroups(currentGroups);
	}

	/**
	 * Gets the adjacencyMatrix for the branch
	 * 
	 * @return the adjacencyMatrix
	 */
	public double[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	/**
	 * Gets the lowerBound for the branch
	 * 
	 * @return the lowerBound
	 */
	public double getLowerBound() {
		return lowerBound;
	}

	/**
	 * Gets the route for the branch
	 * 
	 * @return the route
	 */
	public LinkedList<Edge> getRoute() {
		return route;
	}

	/**
	 * Gets the edgesLeft for the branch
	 * 
	 * @return the edgesLeft
	 */
	public int[] getEdgesLeft() {
		return edgesLeft;
	}

	/**
	 * Gets the edgesConnected for the branch
	 * 
	 * @return the edgesConnected
	 */
	public int[] getEdgesConnected() {
		return edgesConnected;
	}

	/**
	 * Gets the currentNode for the branch
	 * 
	 * @return the currentNode
	 */
	public int getCurrentNode() {
		return currentNode;
	}

	/**
	 * Gets the connectedNode for the branch
	 * 
	 * @return the connectedNode
	 */
	public int getConnectedNode() {
		return connectedNode;
	}

	/**
	 * Gets the groups for the branch
	 * 
	 * @return the currentGroups
	 */
	public LinkedList<LinkedList<Edge>> getCurrentGroups() {
		return currentGroups;
	}

	/**
	 * Sets the adjacency matrix in this branch to be a new version of the
	 * adjacency matrix passed
	 * 
	 * @param adjacencyMatrix
	 *            the adjacencyMatrix to set
	 */
	private void setAdjacencyMatrix(double[][] adjacencyMatrix) {
		this.adjacencyMatrix = new double[adjacencyMatrix.length][adjacencyMatrix.length];
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix.length; j++) {
				this.adjacencyMatrix[i][j] = adjacencyMatrix[i][j];
			}
		}
	}

	/**
	 * Sets the route in this branch to be a new version of the route passed
	 * 
	 * @param route
	 *            the route to set
	 */
	private void setRoute(LinkedList<Edge> route) {
		this.route = new LinkedList<Edge>();
		for (Edge z : route) {
			// System.out.println("Adding: " +z.getStart().x +", " +
			// z.getStart().y + " ---> " + z.getEnd().x +", " +z.getEnd().y +"
			// to route");
			this.route.add(z);
		}
	}

	/**
	 * Sets the edgesLeft in this branch to be a new version of the edgesLeft
	 * passed
	 * 
	 * @param edgesLeft
	 *            the edgesLeft to set
	 */
	private void setEdgesLeft(int[] edgesLeft) {
		this.edgesLeft = new int[edgesLeft.length];
		for (int i = 0; i < edgesLeft.length; i++) {
			this.edgesLeft[i] = edgesLeft[i];
		}
	}

	/**
	 * Sets the edgesConnected in this branch to be a new version of the
	 * edgesConnected passed
	 * 
	 * @param edgesConnected
	 *            the edgesConnected to set
	 */
	private void setEdgesConnected(int[] edgesConnected) {
		this.edgesConnected = new int[edgesConnected.length];
		for (int i = 0; i < edgesConnected.length; i++) {
			this.edgesConnected[i] = edgesConnected[i];
		}
	}

	/**
	 * Sets the currentNode in this branch to be the currentNode passed
	 * 
	 * @param currentNode
	 *            the currentNode to set
	 */
	private void setCurrentNode(int currentNode) {
		this.currentNode = currentNode;
	}

	/**
	 * Sets the connectedNode in this branch to be the connectedNode passed
	 * 
	 * @param connectedNode
	 *            the connectedNode to set
	 */
	private void setConnectedNode(int connectedNode) {
		this.connectedNode = connectedNode;
	}

	/**
	 * Sets the current groups in this branch to be a new version of the passed
	 * groups
	 * 
	 * @param currentGroups
	 *            the currentGroups to set
	 */
	private void setCurrentGroups(LinkedList<LinkedList<Edge>> currentGroups) {
		this.currentGroups = new LinkedList<LinkedList<Edge>>();
		for (LinkedList<Edge> group : currentGroups) {
			for (Edge p : group) {
				addToGroups(p);
			}
		}
	}

	/**
	 * Increments the edges connected for both nodes in an edge
	 * 
	 * @param node1
	 *            the first node
	 * @param node2
	 *            the second node
	 */
	public void incrementConnected(int node1, int node2) {
		edgesConnected[node1]++;
		edgesConnected[node2]++;
	}

	/**
	 * sets the value for an edge in the adjacency matrix
	 * 
	 * @param node1
	 *            the first node
	 * @param node2
	 *            the second node
	 * @param value
	 *            the value to set
	 */
	public void setWeight(int node1, int node2, double value) {
		adjacencyMatrix[node1][node2] = value;
		adjacencyMatrix[node2][node1] = value;
	}

	/**
	 * Adds an edge to the route
	 * 
	 * @param temp
	 *            the edge to add
	 */
	public void addToRoute(Edge temp) {
		// System.out.println("Adding: " +temp.getStart().x +", " +
		// temp.getStart().y + " ---> " + temp.getEnd().x +", " +temp.getEnd().y
		// +" to route");
		route.add(temp);
	}

	/**
	 * Decrements the number of edges left for each node
	 * 
	 * @param node1
	 *            the first node
	 * @param node2
	 *            the second node
	 */
	public void decrementEdgesLeft(int node1, int node2) {
		edgesLeft[node1]--;
		edgesLeft[node2]--;
	}

	/**
	 * Removes any edges which are no longer possible in the current branch
	 * 
	 * @param allLocations
	 *            the list of all locations
	 */
	public void trimFullyConnected(ArrayList<Location> allLocations) {
		for (int node = 0; node < edgesConnected.length; node++) {
			// System.out.println("Current connected Edges: " +
			// edgesConnected[i]);
			// if a node already has two connected edges then it can have no
			// more connections
			if (edgesConnected[node] == 2) {
				for (int linkedNode = 0; linkedNode < adjacencyMatrix.length; linkedNode++) {
					// find the connections which are no longer possible and
					// removes it
					if (isAvailable(allLocations, node, linkedNode)) {
						removeEdge(node, linkedNode);
					}
				}
			}
		}
	}

	/**
	 * Adds a edge which is now essential in the graph since it will not be
	 * possible to create a full graph otherwise
	 * 
	 * @param allLocations
	 *            the list of all locations
	 */
	public void addEssentialConnections(ArrayList<Location> allLocations) {
		// look through the number of edges left for each node
		for (int i = 0; i < edgesLeft.length; i++) {
			// if the edge has only one or two possible edges left, and needs
			// that many more connections, then add them
			if ((edgesLeft[i] == 2 && edgesConnected[i] == 0) || (edgesLeft[i] == 1 && edgesConnected[i] == 1)) {
				// finds the edges which need to be added to the route
				for (int linkedNode = 0; linkedNode < adjacencyMatrix.length; linkedNode++) {
					if (isAvailable(allLocations, i, linkedNode)) {
						addEdge(allLocations, i, linkedNode);
					}
				}
			} else if (edgesLeft[i] == 0 && edgesConnected[i] != 2) {
				// System.err.println("Too many edges have been removed: Error
				// in addEssentialConnections");
			}
		}
	}

	/**
	 * Checks the graph for possible cycles and removes any potential edges
	 * which would cause this cycle
	 * 
	 * @param allLocations
	 *            the list of all locations
	 */
	public void checkForCycles(ArrayList<Location> allLocations) {
		// only worth checking if more than one thing in route
		if (route.size() > 1) {
			// checks through the entire rest of the adjacency matrix
			for (int checkNode1 = currentNode; checkNode1 < adjacencyMatrix.length - 1; checkNode1++) {
				for (int checkNode2 = checkNode1 + 1; checkNode2 < adjacencyMatrix.length; checkNode2++) {
					// if that the current edge has not yet been selected or
					// removed
					if (isAvailable(allLocations, checkNode1, checkNode2)) {
						// check to see if edge would create a cycle by checking
						// if both of it's end locations are already part of the
						// same group
						int[] foundPerGroup = new int[currentGroups.size()];
						for (int i = 0; i < currentGroups.size(); i++) {
							for (Edge k : currentGroups.get(i)) {
								if (k.getStart().equals(allLocations.get(checkNode1))
										|| k.getStart().equals(allLocations.get(checkNode2))) {
									foundPerGroup[i]++;
								}
								if (k.getEnd().equals(allLocations.get(checkNode1))
										|| k.getEnd().equals(allLocations.get(checkNode2))) {
									foundPerGroup[i]++;
								}
							}
						}

						// remove any edges which would form cycles
						for (int found : foundPerGroup) {
							if (found >= 2) {
								removeEdge(checkNode1, checkNode2);
							}
						}

					}

				}

			}
		}
	}

	/**
	 * Checks if an edge has already been selected or removed yet
	 * 
	 * @param allLocations
	 *            the list of all locations
	 * @param node1
	 *            the first node
	 * @param node2
	 *            the second node
	 * @return a boolean representing whether an edge is available or not
	 */
	private boolean isAvailable(ArrayList<Location> allLocations, int node1, int node2) {
		boolean notAdded = true;
		Location start = allLocations.get(node1);
		Location end = allLocations.get(node2);
		for (Edge e : route) {
			// if the route already contains the edge then it is not available
			// System.out.println("Found Edge: " +e.getStart().x +", " +
			// e.getStart().y + " ---> " + e.getEnd().x +", " +e.getEnd().y);;
			if ((e.getStart().equals(start) && e.getEnd().equals(end))
					|| (e.getStart().equals(end) && e.getEnd().equals(start))) {
				// System.out.println("added: " + node1 +", " + node2);
				notAdded = false;
			} else {
				// System.out.println("not added: " + node1 +", " + node2);
			}
		}
		// if the edge has been removed then it's distance will be infinity
		// therefore it is not available
		return notAdded && adjacencyMatrix[node1][node2] != Double.POSITIVE_INFINITY;
	}

	/**
	 * Removes an edge from the branch
	 * 
	 * @param node1
	 *            the first node
	 * @param node2
	 *            the second node
	 */
	public void removeEdge(int node1, int node2) {
		// System.out.println("Removing edge between: " + node1 +", " + node2);
		// removes edge from the adjacency matrix
		setWeight(node1, node2, Double.POSITIVE_INFINITY);
		decrementEdgesLeft(node1, node2);
		// printAdjacencyMatrix();
	}

	/**
	 * Adds an edge to the branch
	 * 
	 * @param allLocations
	 *            the list of all locations
	 * @param node1
	 *            the first node
	 * @param node2
	 *            the second node
	 */
	public void addEdge(ArrayList<Location> allLocations, int node1, int node2) {
		// System.out.println("Adding edge between: " + node1 +", " + node2);
		// adds the edge to the route and updates values
		Edge temp1 = new Edge(allLocations.get(node1), allLocations.get(node2));
		addToRoute(temp1);
		decrementEdgesLeft(node1, node2);
		incrementConnected(node1, node2);
		addToGroups(temp1);
		trimFullyConnected(allLocations);
		// printAdjacencyMatrix();
	}

	/**
	 * Sets the lower bound for the branch
	 */
	public void setLowerBound() {
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
		// System.out.println("LowerBound: " + lowerBound/2);
		this.lowerBound = lowerBound / 2;
	}

	private void addToGroups(Edge pairToAdd) {
		// adds a new edge to whichever group it belongs in and merges groups
		// checks to see if it is the first node ever added
		if (currentGroups.isEmpty()) {
			LinkedList<Edge> g = new LinkedList<Edge>();
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
				for (Edge p : currentGroups.get(i)) {
					if (p.getStart().equals(pairToAdd.getStart()) || p.getStart().equals(pairToAdd.getEnd())
							|| p.getEnd().equals(pairToAdd.getStart()) || p.getEnd().equals(pairToAdd.getEnd())) {
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
				LinkedList<Edge> g = new LinkedList<Edge>();
				g.add(pairToAdd);
				currentGroups.add(g);
			}
		}
	}

	/**
	 * Checks whether the branch is a complete route
	 * 
	 * @return a boolean representing whether the branch is complete or not
	 */
	public boolean checkComplete() {
		// checks if this branch is complete
		// System.out.println("Checking complete");
		for (int connected : edgesConnected) {
			if (connected != 2) {
				// System.out.println("Not Complete");
				return false;
			}
		}
		// System.out.println("Complete");
		return true;
	}

	/**
	 * Increments the values of current and connected nodes in the branch to the
	 * next available location
	 * 
	 * @param allLocations
	 *            the list of all locations
	 */
	public void incrementNodes(ArrayList<Location> allLocations) {
		do {
			if (connectedNode + 1 >= adjacencyMatrix.length) {
				// increment currentNode and set connected to be current +1;
				if (currentNode + 2 < adjacencyMatrix.length) {
					// safe to increment
					currentNode++;
					connectedNode = currentNode + 1;
				} else {
					// System.err.println("Reached end of array without finding
					// any valid route, error in TSP");
				}
			} else {
				connectedNode++;
			}
		} while (!isAvailable(allLocations, currentNode, connectedNode));
	}

	/**
	 * Prints the adjacency matrix: for testing!
	 */
	public void printAdjacencyMatrix() {
		for (double[] x : adjacencyMatrix) {
			for (double y : x) {
				System.out.print(y + "  ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
