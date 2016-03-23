package warehouse.routePlanning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import warehouse.util.Location;;

public class SimpleSearch extends Search {

	public SimpleSearch(Map m) {
		super(m);
	}

	private Optional<LocationRoute> DistanceAStar(Location start, Location goal) {
		// The set of Locations already evaluated.
		Set<Location> closedSet = new HashSet<Location>();

		// The set of Locations to be explored
		Set<Location> openSet = new HashSet<Location>();

		// The map which links each state to the most efficient previous
		// state
		HashMap<Location, Location> cameFrom = new HashMap<Location, Location>();

		// The map which links each state with their distance from the start
		// location
		HashMap<Location, Double> gScore = new HashMap<Location, Double>();

		// The map which links each state with a heuristic distance to the
		// goal location
		HashMap<Location, Double> fScore = new HashMap<Location, Double>();

		// Initialise the heuristic maps
		gScore = initHScoreLocation(gScore);
		fScore = initHScoreLocation(fScore);

		// adds start and set the distance from the start for the start to be 0
		openSet.add(start);
		gScore.put(start, 0.0);
		fScore.put(start, (double) start.manhattanDistance(goal));

		// Main loop
		while (!openSet.isEmpty()) {
			// get the next best node to expand
			Location current = getLowestLocation(openSet, fScore);
			// if the best node is the goal then finished
			if (current.x == goal.x && current.y == goal.y) {
				// return the optimal route
				LinkedList<Location> route = getPath(cameFrom, current);
				return Optional.of(getPathSize(cameFrom, current));
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
							fScore.put(neighbour, (double) neighbour.manhattanDistance(goal) + gScore.get(neighbour));
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
	private HashMap<Location, Double> initHScoreLocation(HashMap<Location, Double> hScore) {
		for (Location[] x : map) {
			for (Location y : x) {
				hScore.put(y, Double.POSITIVE_INFINITY);
				hScore.put(y, Double.POSITIVE_INFINITY);
				hScore.put(y, Double.POSITIVE_INFINITY);
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
	private Location getLowestLocation(Set<Location> openSet, HashMap<Location, Double> fScore) {
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
	 * Assembles a list of states in order from a series of linked states
	 * 
	 * @param cameFrom
	 *            a map which maps a state to the state which came before it in
	 *            search
	 * @param current
	 *            the current state
	 * @return the list of states
	 */
	private LinkedList<Location> getPath(HashMap<Location, Location> cameFrom, Location current) {
		// The list of moves to make to reach goal
		LinkedList<Location> path = new LinkedList<Location>();
		path.add(current);
		while (cameFrom.containsKey(current)) {
			// Gets the previous location and facing
			path.addFirst(cameFrom.get(current));
			current = cameFrom.get(current);
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
	private LinkedList<Location> getNeighbours(Location currentLocation) {
		LinkedList<Location> neighbours = new LinkedList<Location>();
		if (inMap(currentLocation.y + 1, currentLocation.x)) {
			neighbours.add(map[currentLocation.y + 1][currentLocation.x]);
		}
		if (inMap(currentLocation.y, currentLocation.x + 1)) {
			neighbours.add(map[currentLocation.y][currentLocation.x + 1]);
		}
		if (inMap(currentLocation.y - 1, currentLocation.x)) {
			neighbours.add(map[currentLocation.y - 1][currentLocation.x]);
		}
		if (inMap(currentLocation.y, currentLocation.x - 1)) {
			neighbours.add(map[currentLocation.y][currentLocation.x - 1]);
		}
		return neighbours;
	}
}
