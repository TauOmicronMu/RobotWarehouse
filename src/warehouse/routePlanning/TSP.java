package warehouse.routePlanning;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import warehouse.job.Job;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Route;

public class TSP {
	private Search s;
	private int numberOfLocations;
	private ArrayList<Location> allLocations;
	LinkedList<Edge> bestRoute = new LinkedList<Edge>();
	LinkedList<Location> finalRoute = new LinkedList<Location>();
	double lowestWeight;

	public TSP(Search s) {
		this.s = s;
	}
	
	/**
	 * This will be the final method used
	 * @param job
	 * @param start
	 * @param facing
	 * @return
	 */
	public Optional<Route> getShortestRoute(Job job, Location start, Direction facing) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	
	//Current method used
	public LinkedList<Edge> getShortestRoute(Job j, Location startingPosition) {
		// builds full list of locations
		allLocations = makeListLocations(j, startingPosition);

		// gets the number of locations to visit
		numberOfLocations = allLocations.size();

		// creates a matrix to contain the best distance from one location to
		// another
		double[][] adjacencyMatrix = new double[numberOfLocations][numberOfLocations];
		adjacencyMatrix = initiateMatrix(adjacencyMatrix);

		// gives these matrices their initial values
		adjacencyMatrix = setUpMatrices(adjacencyMatrix, allLocations);

		// initialises array of the amount of edges left available to each node
		int[] edgesLeft = new int[numberOfLocations];
		for (int x = 0; x < edgesLeft.length; x++) {
			edgesLeft[x] = numberOfLocations - 1;
		}
		// initialises array of the amount of edges connected to each node
		int[] edgesConnected = new int[numberOfLocations];
		for (int x = 0; x < edgesLeft.length; x++) {
			edgesConnected[x] = 0;
		}

		// initialises list of groups
		LinkedList<LinkedList<Edge>> currentGroups = new LinkedList<LinkedList<Edge>>();

		// reset lowestWeight
		lowestWeight = Double.POSITIVE_INFINITY;

		// set up fresh route
		LinkedList<Edge> route = new LinkedList<Edge>();

		// creates the initial branch
		Branch initial = new Branch(adjacencyMatrix, route, edgesLeft, edgesConnected, 0, 1, currentGroups);

		// starts the first call of the route finding algorithm
		getRoute(initial);

		// return new Route(moves, startingPosition, j.dropLocation);
		return bestRoute;
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

	private double[][] setUpMatrices(double[][] adjacencyMatrix, ArrayList<Location> allLocations) {
		for (int Node1 = 0; Node1 < allLocations.size() - 1; Node1++) {
			for (int Node2 = Node1 + 1; Node2 < allLocations.size(); Node2++) {
				// THIS NEEDS TO BE CHANGED DIRECTION NEEDS TO BE KEPT TRACK OF
				// METHODS NEED TO BE CHANGED TO FIND BEST ROUTE GIVEN LOCATION
				// ARRIVES IN PREVIOUS NODE
				Optional<Route> foundRoute = s.getRoute(allLocations.get(Node1), allLocations.get(Node2),
						Direction.NORTH);
				if (foundRoute.isPresent()) {
					Route r = foundRoute.get();
					adjacencyMatrix[Node1][Node2] = r.totalDistance;
					adjacencyMatrix[Node2][Node1] = r.totalDistance;
				} else {
					System.err.println("Could not find a route in adjMatrix between: " + Node1 + " and " + Node2);
				}
			}
		}
		return adjacencyMatrix;
	}

	/**
	 * 
	 * @param currentBranch
	 */
	private void getRoute(Branch currentBranch) {
		// creates local versions of current and connected node
		int currentNode = currentBranch.getCurrentNode();
		int connectedNode = currentBranch.getConnectedNode();
		System.out.println("CurrentNode: " + currentNode);
		System.out.println("ConnectedNode: " + connectedNode);

		// creates two new branches
		Branch[] branches = new Branch[2];
		for (int i = 0; i < 2; i++) {
			branches[i] = new Branch(currentBranch.getAdjacencyMatrix(), currentBranch.getRoute(),
					currentBranch.getEdgesLeft(), currentBranch.getEdgesConnected(), currentNode, connectedNode,
					currentBranch.getCurrentGroups());
		}

		// one branch adds a node, the other removes it
		branches[0].addEdge(allLocations, currentNode, connectedNode);
		branches[1].removeEdge(currentNode, connectedNode);

		// checks impact of adding or removing nodes on the adjacency matrix and
		// modifies accordingly
		for (int x = 0; x < branches.length; x++) {

			// TODO FIX ERRORS IN LARGE NETWORKS AND WHERE START DIRECTION
			// MATTERS
			System.out.println();
			System.out.println("BRANCH: " + x);

			System.out.println("Adds needed connections");
			// check to see if an edge must be added as it would not be possible
			// for either node to have two edges without it
			branches[x].addEssentialConnections(allLocations);

			System.out.println("Checking for cycles");
			// checks for cycles
			// only if node was excluded: not sure why yet, check old code
			branches[x].checkForCycles(allLocations);

			System.out.println("Adds needed connections 2");
			// check for essential connections again
			branches[x].addEssentialConnections(allLocations);

			System.out.println();
			System.out.println("printing before calculating lower bounds");
			branches[x].printAdjacencyMatrix();
			// get lower bounds
			branches[x].setLowerBound();

			System.out.println("END OF BRANCH");
		}

		// deals with the two different routes in order of lowest lower bound
		// first
		// checks which route has the lowest lower bound
		if (branches[0].getLowerBound() > branches[1].getLowerBound()) {
			pruneBranches(branches, 1, 0);
		} else {
			pruneBranches(branches, 0, 1);
		}
	}

	/**
	 * Makes the job into a list of locations to visit
	 * 
	 * @param j
	 *            the job to convert
	 * @param startPosition
	 *            the start position of the robot
	 * @return the list of all locations to visit
	 */
	private ArrayList<Location> makeListLocations(Job j, Location startPosition) {
		ArrayList<Location> allLocations = new ArrayList<Location>();
		allLocations.add(startPosition);
		for (ItemPickup x : j.pickups) {
			allLocations.add(x.location);
		}
		allLocations.add(j.dropLocation);
		return allLocations;
	}

	private void pruneBranches(Branch[] branches, int better, int worse) {
		// checks if we can prune either of the splits (i.e. not bother
		// checking their children as their lower bounds are higher than the
		// current best weight)
		if (branches[better].getLowerBound() < lowestWeight) {
			// check if the branch is complete
			if (branches[better].checkComplete()) {
				// branch is the new best route
				lowestWeight = branches[better].getLowerBound();
				bestRoute = branches[better].getRoute();
				System.out.println("lowestWeight: " + lowestWeight);
				// prune other route as this is now the shortest length and
				// it had a larger lower bound
			} else {
				// find next available location
				branches[better].incrementNodes(allLocations);
				// recurse on the best branch
				getRoute(branches[better]);

				// check the other route
				if (branches[worse].getLowerBound() < lowestWeight) {
					// check if the branch is complete
					if (branches[worse].checkComplete()) {
						// branch is the new best route
						lowestWeight = branches[better].getLowerBound();
						bestRoute = branches[better].getRoute();
					} else {
						// find next available location
						branches[worse].incrementNodes(allLocations);
						// recurse on the worse branch
						getRoute(branches[worse]);
					}
				}
			}
		}
	}
}
