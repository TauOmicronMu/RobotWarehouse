package warehouse.routePlanning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.Action;
import warehouse.Location;
import warehouse.Route;

public class Search {
	private HashMap<Location, Boolean> available;
	private Location[][] map;

	/**
	 * Constructor which sets up the map and obstacles
	 */
	public Search() {
		Map m = new Map();
		map = m.getMap();
		available = m.getAvailable();
	}

	/**
	 * returns the route to take between two locations
	 * @param start the start location
	 * @param goal the goal location
	 * @return the optimal route between two locations
	 */
	public Optional<Route> getRoute(Location start, Location goal) {
		Optional<LinkedList<Location>> r = BasicAStar(start, goal);
		if (r.isPresent()) {
			//TODO actual code to provide a route
			return Optional.of(new Route(new LinkedList<Action>(), start, goal));
		}else{
			return Optional.empty();
		}
	}
	
	/**
	 * Returns the result of search method for testing
	 * @param start the start location
	 * @param goal the goal location
	 * @return a list of locations which form the optimal route to take
	 */
	public Optional<LinkedList<Location>> testRoute(Location start, Location goal) {
		return BasicAStar(start, goal);
	}

	/**
	 * Gets the optimal route between two locations via basic A*
	 * @param start the start location
	 * @param goal the goal location
	 * @return a list of locations which form the optimal route to take
	 */
	private Optional<LinkedList<Location>> BasicAStar(Location start, Location goal) {
		// The set of nodes already evaluated.
		Set<Location> closedSet = new HashSet<Location>();

		// The set of nodes to be explored
		Set<Location> openSet = new HashSet<Location>();

		// The map which links each location to the most efficient previous
		// location
		HashMap<Location, Location> cameFrom = new HashMap<Location, Location>();

		// The map which links each location with their distance from the start
		// location
		HashMap<Location, Double> gScore = new HashMap<Location, Double>();

		// The map which links each location with a heuristic distance to the
		// goal location
		HashMap<Location, Double> fScore = new HashMap<Location, Double>();

		// Initialise the heuristic maps
		gScore = initHScore(gScore);
		fScore = initHScore(fScore);

		// adds start and set the distance from the start for the start to be 0
		openSet.add(start);
		gScore.put(start, 0.0);
		fScore.put(start, manhatttenEstimate(start, goal));

		// Main loop
		while (!openSet.isEmpty()) {
			// get the next best node to expand
			Location current = getLowest(openSet, fScore);
			// if the best node is the goal then finished
			if (current.x == goal.x && current.y == goal.y) {
				// return the optimal route
				return Optional.of(getPath(cameFrom, current));
			}
			// else continue
			// remove current form the openSet and add to closedSet
			openSet.remove(current);
			closedSet.add(current);

			// for each neighbour of current
			for (Location neighbour : getNeighbours(current)) {
				// if the neighbour is not an obstacle
				if (available.get(neighbour)) {
					// if the neighbour has not already been explored
					if (!closedSet.contains(neighbour)) {
						// get current gScore value
						Double tempGScore = gScore.get(current) + 1;
						// add to set if not already a member of the set
						openSet.add(neighbour);
						// if tempGScore is smaller than previously, record it
						// as best route until now
						if (tempGScore < gScore.get(neighbour)) {
							cameFrom.put(neighbour, current);
							gScore.put(neighbour, tempGScore);
							fScore.put(neighbour, manhatttenEstimate(neighbour, goal));
						}
					}
				}
			}
		}
		// no route could be found
		return Optional.empty();
	}

	/**
	 * Sets up the a map with infinity values for every location
	 * 
	 * @param hScore
	 *            the map which holds the heuristic distances
	 * @return map with initialised values
	 */
	private HashMap<Location, Double> initHScore(HashMap<Location, Double> hScore) {
		for (Location[] x : map) {
			for (Location y : x) {
				hScore.put(y, Double.POSITIVE_INFINITY);
			}
		}
		return hScore;
	}

	/**
	 * Gets the location with the lowest estimated cost to the goal
	 * 
	 * @param openSet
	 *            the set of nodes to be explored
	 * @param fScore
	 *            the map between locations and their estimated cost to the goal
	 * @return the location with the lowest estimated cost to the goal
	 */
	private Location getLowest(Set<Location> openSet, HashMap<Location, Double> fScore) {
		Iterator<Location> it = openSet.iterator();
		Double lowest = Double.POSITIVE_INFINITY;
		Location best = null;
		while (it.hasNext()) {
			Location x = it.next();
			Double f = fScore.get(x);
			if (fScore.get(x) < lowest) {
				best = x;
				lowest = f;
			}
		}
		return best;
	}

	/**
	 * Gets the list of locations passed through to reach the goal
	 * 
	 * @param cameFrom
	 *            the map of locations to their best predecessor
	 * @param current
	 *            the current location (goal)
	 * @return the list of locations visited on optimal path
	 */
	private LinkedList<Location> getPath(HashMap<Location, Location> cameFrom, Location current) {
		// The list of moves to make to reach goal
		LinkedList<Location> path = new LinkedList<Location>();
		// add the current location (goal in this case)
		path.addFirst(current);
		// while not at the start node
		while (cameFrom.containsKey(current)) {
			// add the location which came before it
			current = cameFrom.get(current);
			// add the current location
			path.addFirst(current);
		}
		return path;
	}

	/**
	 * Gets the neighbours of a node
	 * 
	 * @param node
	 *            the location to find the neighbours for
	 * @return the array of neighbours
	 */
	private LinkedList<Location> getNeighbours(Location node) {
		LinkedList<Location> neighbours = new LinkedList<Location>();
		if(inMap(node.y + 1, node.x)){
			neighbours.add(map[node.y + 1][node.x]);
		}
		if(inMap(node.y -1, node.x)){
			neighbours.add(map[node.y - 1][node.x]);
		}
		if(inMap(node.y, node.x +1)){
			neighbours.add(map[node.y][node.x + 1]);
		}
		if(inMap(node.y, node.x -1)){
			neighbours.add(map[node.y][node.x - 1]);
		}
		return neighbours;
	}

	/**
	 * Estimates the cost to travel from one node to the goal
	 * 
	 * @param l
	 *            the start location
	 * @param goal
	 *            the goal location
	 * @return the estimated distance
	 */
	private Double manhatttenEstimate(Location l, Location goal) {
		// returns Manhattan distance
		return (double) (Math.abs(goal.x - l.x) + Math.abs(goal.y - l.y));
	}
	
	private boolean inMap(int y, int x){
		return (y >= 0 && y < map.length && x >= 0 && x < map[y].length);
	}
}
