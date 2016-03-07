package warehouse.routePlanning;

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
	 * @param adjacencyMatrix the matrix which contains the distance for every edge
	 * @param route the current route
	 * @param edgesLeft the current amount of edges left for each node
	 * @param edgesConnected the current amount of edges connected for each node
	 * @param currentNode the current node being inspected
	 * @param connectedNode the next node to be inspected
	 * @param currentGroups the list of groups for this branch
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
	 * @return the adjacencyMatrix
	 */
	public double[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	/**
	 * Gets the lowerBound for the branch
	 * @return the lowerBound
	 */
	public double getLowerBound() {
		return lowerBound;
	}

	/**
	 * Gets the route for the branch
	 * @return the route
	 */
	public LinkedList<Edge> getRoute() {
		return route;
	}

	/**
	 * Gets the edgesLeft for the branch
	 * @return the edgesLeft
	 */
	public int[] getEdgesLeft() {
		return edgesLeft;
	}

	/**
	 * Gets the edgesConnected for the branch
	 * @return the edgesConnected
	 */
	public int[] getEdgesConnected() {
		return edgesConnected;
	}

	/**
	 * Gets the currentNode for the branch
	 * @return the currentNode
	 */
	public int getCurrentNode() {
		return currentNode;
	}

	/**
	 * Gets the connectedNode for the branch
	 * @return the connectedNode
	 */
	public int getConnectedNode() {
		return connectedNode;
	}

	/**
	 * Gets the groups for the branch
	 * @return the currentGroups
	 */
	public LinkedList<LinkedList<Edge>> getCurrentGroups() {
		return currentGroups;
	}

	/**
	 * Sets the adjacency matrix in this branch to be a new version of the adjacency matrix passed
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
	 * @param route
	 *            the route to set
	 */
	private void setRoute(LinkedList<Edge> route) {
		this.route = new LinkedList<Edge>();
		for (Edge z : route) {
//			System.out.println("Adding: " +z.getStart().x +", " + z.getStart().y + " ---> " + z.getEnd().x +", " +z.getEnd().y +" to route");
			this.route.add(z);
		}
	}

	/**
	 * Sets the edgesLeft in this branch to be a new version of the edgesLeft passed
	 * @param edgesLeft
	 *            the edgesLeft to set
	 */
	private void setEdgesLeft(int[] edgesLeft) {
		this.edgesLeft = new int [edgesLeft.length];
		for (int i = 0; i < edgesLeft.length; i++) {
			this.edgesLeft[i] = edgesLeft[i];
		}
	}

	/**
	 * Sets the edgesConnected in this branch to be a new version of the edgesConnected passed
	 * @param edgesConnected
	 *            the edgesConnected to set
	 */
	private void setEdgesConnected(int[] edgesConnected) {
		this.edgesConnected = new int [edgesConnected.length];
		for (int i = 0; i < edgesConnected.length; i++) {
			this.edgesConnected[i] = edgesConnected[i];
		}
	}

	/**
	 * Sets the currentNode in this branch to be the currentNode passed
	 * @param currentNode
	 *            the currentNode to set
	 */
	private void setCurrentNode(int currentNode) {
		this.currentNode = currentNode;
	}

	/**
	 * Sets the connectedNode in this branch to be the connectedNode passed
	 * @param connectedNode
	 *            the connectedNode to set
	 */
	private void setConnectedNode(int connectedNode) {
		this.connectedNode = connectedNode;
	}

	/**
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

	public void addToGroup(Edge temp) {
		addToGroups(temp);
	}

	public void incrementConnected(int node1, int node2) {
		edgesConnected[node1]++;
		edgesConnected[node2]++;
	}

	public void setWeight(int node1, int node2, double value) {
		adjacencyMatrix[node1][node2] = value;
		adjacencyMatrix[node2][node1] = value;
	}

	public void addToRoute(Edge temp) {
//		System.out.println("Adding: " +temp.getStart().x +", " + temp.getStart().y + " ---> " + temp.getEnd().x +", " +temp.getEnd().y +" to route");
		route.add(temp);
	}

	public void decrementEdgesLeft(int node1, int node2) {
		edgesLeft[node1]--;
		edgesLeft[node2]--;
	}

	public void trimFullyConnected(ArrayList<Location> allLocations) {
		for (int i = 0; i < edgesConnected.length; i++) {
//			System.out.println("Current connected Edges: " + edgesConnected[i]);
			if (edgesConnected[i] == 2) {
				for (int linkedNode = 0; linkedNode < adjacencyMatrix.length; linkedNode++) {
					if (isAvailable(allLocations, i, linkedNode)) {
						removeEdge(i, linkedNode);
					}
				}
			}
		}
	}

	public void addEssentialConnections(ArrayList<Location> allLocations) {
		for (int i = 0; i < edgesLeft.length; i++) {
			if ((edgesLeft[i] == 2 && edgesConnected[i] == 0) || (edgesLeft[i] == 1 && edgesConnected[i] == 1)) {
				// find any edges which need to be added to the route
				for (int linkedNode = 0; linkedNode < adjacencyMatrix.length; linkedNode++) {
					if (isAvailable(allLocations, i, linkedNode)) {
						addEdge(allLocations, i, linkedNode);
					}
				}
			} else if (edgesLeft[i] == 0 && edgesConnected[i] != 2) {
//				System.err.println("Too many edges have been removed: Error in addEssentialConnections");
			}
		}
	}

	public void checkForCycles(ArrayList<Location> allLocations) {
		// only worth checking if more than one thing in route
		if (route.size() > 1) {
			// checks through the entire rest of the adjacency matrix
			for (int checkNode1 = currentNode; checkNode1 < adjacencyMatrix.length - 1; checkNode1++) {
				for (int checkNode2 = checkNode1 + 1; checkNode2 < adjacencyMatrix.length; checkNode2++) {
					if (isAvailable(allLocations, checkNode1, checkNode2)) {
						// check to see if edge would create a node with
						// more than two edges or create a cycle
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

						// if the check found an edge which could be
						// removed, then remove it from matrix and list of
						// edges left
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

	private boolean isAvailable(ArrayList<Location> allLocations, int node1, int node2) {
		boolean notAdded = true;
		Location start = allLocations.get(node1);
		Location end = allLocations.get(node2);
		for (Edge e : route) {
//			System.out.println("Found Edge: " +e.getStart().x +", " + e.getStart().y + " ---> " + e.getEnd().x +", " +e.getEnd().y);;
			if ((e.getStart().equals(start) && e.getEnd().equals(end))
					|| (e.getStart().equals(end) && e.getEnd().equals(start))) {
//				System.out.println("added: " + node1 +", " + node2);
				notAdded = false;
			}else{
//				System.out.println("not added: " + node1 +", " + node2);
			}
		}
		return notAdded && adjacencyMatrix[node1][node2] != Double.POSITIVE_INFINITY;
	}

	public void removeEdge(int node1, int node2) {
//		System.out.println("Removing edge between: " + node1 +", " + node2);
		// removes edge from the adjacency matrix
		setWeight(node1, node2, Double.POSITIVE_INFINITY);
		decrementEdgesLeft(node1, node2);
//		printAdjacencyMatrix();
	}

	public void addEdge(ArrayList<Location> allLocations, int node1, int node2) {
//		System.out.println("Adding edge between: " + node1 +", " + node2);
		// adds the edge to the route and updates values
		Edge temp1 = new Edge(allLocations.get(node1), allLocations.get(node2));
		addToRoute(temp1);
		decrementEdgesLeft(node1, node2);
		incrementConnected(node1, node2);
		addToGroup(temp1);
		trimFullyConnected(allLocations);
//		printAdjacencyMatrix();
	}

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
//		System.out.println("LowerBound: " + lowerBound/2);
		this.lowerBound = lowerBound / 2;
	}
	
	private void addToGroups(Edge pairToAdd) {
		// adds a new edge to whichever group it belongs in and merges groups
		// checks to see if it is the first node ever added
		if (currentGroups.isEmpty()) {
			LinkedList<Edge> g = new LinkedList<>();
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
				LinkedList<Edge> g = new LinkedList<>();
				g.add(pairToAdd);
				currentGroups.add(g);
			}
		}
	}
	
	public boolean checkComplete() {
		// checks if this branch is complete
//		System.out.println("Checking complete");
		for (int connected : edgesConnected) {
			if (connected != 2) {
//				System.out.println("Not Complete");
				return false;
			}
		}
//		System.out.println("Complete");
		return true;
	}

	public void incrementNodes(ArrayList<Location> allLocations) {
		do {
			if (connectedNode + 1 >= adjacencyMatrix.length) {
				// increment currentNode and set connected to be current +1;
				if (currentNode + 2 < adjacencyMatrix.length) {
					// safe to increment
					currentNode++;
					connectedNode = currentNode + 1;
				} else {
//					System.err.println("Reached end of array without finding any valid route, error in TSP");
				}
			} else {
				connectedNode++;
			}
		} while (!isAvailable(allLocations, currentNode, connectedNode));
	}
	
	public void printAdjacencyMatrix(){
		for(double [] x: adjacencyMatrix){
			for(double y: x){
				System.out.print(y +"  ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
