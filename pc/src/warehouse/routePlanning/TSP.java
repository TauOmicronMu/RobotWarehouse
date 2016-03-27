package warehouse.routePlanning;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.job.Job;
import warehouse.routePlanning.search.SimpleSearch;
import warehouse.routePlanning.util.Branch;
import warehouse.routePlanning.util.Edge;
import warehouse.routePlanning.util.LocationRoute;
import warehouse.routePlanning.util.Map;
import warehouse.routePlanning.util.RouteBuilder;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Route;

public class TSP {
	private SimpleSearch ss;
	private RouteBuilder rb;
	private int numberOfLocations;
	private ArrayList<Location> allLocations;
	private Map m;
	LinkedList<Edge> bestRoute = new LinkedList<Edge>();
	LinkedList<Location> finalRoute = new LinkedList<Location>();
	double lowestWeight;

	public TSP() {
		GridMap providedMap = MapUtils.createMarkingWarehouseMap();
		m = new Map(providedMap);
	}

	/**
	 * This will be the final method used
	 * 
	 * This method finds the optimal route to pick up all the items in a job
	 * This is used by job-selection to assign the best job to the robot Route
	 * includes the turning of the robot and the total weight it can carry
	 * 
	 * @param job
	 *            the job to find the best route for
	 * @param start
	 *            the starting location
	 * @param facing
	 *            the final facing of the robot
	 * @return optional type which should contain the optimal route
	 */
	public Optional<Route> getShortestRoute(Job job, Location start, Direction facing) {
		this.ss = new SimpleSearch(m);
		this.rb = new RouteBuilder(job.dropLocation, m);
		// builds full list of locations
		allLocations = makeListLocations(job, start);

		// gets the number of locations to visit
		numberOfLocations = allLocations.size();

		// creates a matrix to contain the best distance from one location to
		// another
		double[][] adjacencyMatrix = new double[numberOfLocations][numberOfLocations];
		adjacencyMatrix = initiateMatrix(adjacencyMatrix);

		// gives these matrices their initial values
		adjacencyMatrix = setUpMatrices(adjacencyMatrix, allLocations, start, job.dropLocation);

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
		int pos = 0;
		for (int i = 0; i < bestRoute.size(); i++) {
			if (bestRoute.get(i).getStart().equals(start) && bestRoute.get(i).getEnd().equals(job.dropLocation)) {
				pos = i;
			}
		}
		bestRoute.remove(pos);

		LinkedList<Location> path = createLocationList(start, job);

		return rb.getRoute(path, facing);
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
	 * Places initial values into the adjacency matrix
	 * 
	 * @param adjacencyMatrix
	 *            the matrix which contains the weight of each potential edge
	 * @param allLocations
	 *            the list of all locations in the graph
	 * @param start
	 *            the start location
	 * @param end
	 *            the end location
	 * @return filled adjacencyMatrix
	 */
	private double[][] setUpMatrices(double[][] adjacencyMatrix, ArrayList<Location> allLocations, Location start,
			Location end) {
		for (int Node1 = 0; Node1 < allLocations.size() - 1; Node1++) {
			for (int Node2 = Node1 + 1; Node2 < allLocations.size(); Node2++) {

				// dummy edge to ensure end linked to start for removal later
				if (allLocations.get(Node1).equals(start) && allLocations.get(Node2).equals(end)) {
					adjacencyMatrix[Node1][Node2] = 0;
					adjacencyMatrix[Node2][Node1] = 0;
				} else {
					// normal creation
					Optional<LocationRoute> foundDistance = ss.getRoute(allLocations.get(Node1),
							allLocations.get(Node2));
					if (foundDistance.isPresent()) {
						adjacencyMatrix[Node1][Node2] = foundDistance.get().getDistance();
						adjacencyMatrix[Node2][Node1] = foundDistance.get().getDistance();
					} else {
					}
				}
			}
		}
		return adjacencyMatrix;
	}

	/**
	 * Recursive method which splits the potential route into two branches, one
	 * including an edge, one removing
	 * 
	 * @param currentBranch
	 *            the current branch of the route
	 */
	private void getRoute(Branch currentBranch) {
		// creates local versions of current and connected node
		int currentNode = currentBranch.getCurrentNode();
		int connectedNode = currentBranch.getConnectedNode();

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

			// check to see if an edge must be added as it would not be possible
			// for either node to have two edges without it
			branches[x].addEssentialConnections(allLocations);

			// checks for potential cycles in branch
			branches[x].checkForCycles(allLocations);

			// calculate the lower bound for the branch
			branches[x].setLowerBound();

		}

		// deals with the two different routes in order of lowest lower bound
		// first
		// checks which route has the lowest lower bound
		if (branches[0].getLowerBound() > branches[1].getLowerBound()) {
			pruneBranches(branches[1], branches[0]);
		} else {
			pruneBranches(branches[0], branches[1]);
		}
	}

	/**
	 * Prunes branches or commences further splitting of branches in order of
	 * smallest lower bound first
	 * 
	 * @param better
	 *            the branch with the smaller lower bound
	 * @param worse
	 *            the branch with the larger lower bound
	 */
	private void pruneBranches(Branch better, Branch worse) {
		// checks if we can prune either of the splits (i.e. not bother
		// checking their children as their lower bounds are higher than the
		// current best weight)
		if (better.getLowerBound() < lowestWeight) {
			// check if the branch is complete
			if (better.checkComplete()) {
				// branch is the new best route
				lowestWeight = better.getLowerBound();
				bestRoute = better.getRoute();
				// prune both routes, do not continue searching
			} else {
				// find next available location
				better.incrementNodes(allLocations);
				// recurse on the best branch
				getRoute(better);

				// check the other route
				if (worse.getLowerBound() < lowestWeight) {
					// check if the branch is complete
					if (worse.checkComplete()) {
						// branch is the new best route
						lowestWeight = worse.getLowerBound();
						bestRoute = worse.getRoute();
					} else {
						// find next available location
						worse.incrementNodes(allLocations);
						// recurse on the worse branch
						getRoute(worse);
					}
				}
				// prune only the second route
			}
		}
	}

	private LinkedList<Location> createLocationList(Location start, Job j) {
		LinkedList<Location> path = new LinkedList<Location>();

		Location[][] positions = new Location[bestRoute.size()][2];
		for (int x = 0; x < bestRoute.size(); x++) {
			positions[x][0] = bestRoute.get(x).getStart();
			positions[x][1] = bestRoute.get(x).getEnd();
		}

		path = addNext(start, positions, path, j);

		LinkedList<Location> finalPath = new LinkedList<Location>();
		int totalWeight = 0;
		for (Location l : path) {
			for (ItemPickup item : j.pickups) {
				if (item.location.x == l.x && item.location.y == l.y) {
					totalWeight += item.weight;
				}
			}
			if (totalWeight >= 50) {
				finalPath.add(j.dropLocation);
				totalWeight = 0;
			}
			finalPath.add(l);
		}
		return finalPath;
	}

	private LinkedList<Location> addNext(Location toFind, Location[][] positions, LinkedList<Location> path, Job j) {
		path.add(toFind);
		for (int x = 0; x < positions.length; x++) {
			for (int y = 0; y < 2; y++) {
				if (positions[x][y].x == toFind.x && positions[x][y].y == toFind.y) {

					Location next = new Location(positions[x][1 - y].x, positions[x][1 - y].y);
					positions[x][0] = new Location(-1, -1);
					positions[x][1] = new Location(-1, -1);

					addNext(next, positions, path, j);
				}
			}
		}
		return path;
	}
}
