package warehouse.routePlanning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import warehouse.action.Action;
import warehouse.util.Direction;
import warehouse.util.Location;
import warehouse.util.Route;;

public class Search {
	private HashMap<Location, Boolean> available;
	private Location[][] map;

	/**
	 * Constructor which sets up the map and obstacles
	 */
	public Search(Map m) {
		map = m.getMap();
		available = m.getAvailable();
	}

	/**
	 * Returns the result of search method for testing
	 * 
	 * @param start
	 *            the start location
	 * @param goal
	 *            the goal location
	 * @return a list of locations which form the optimal route to take
	 */
	public Optional<Route> getRoute(Location start, Location goal, Direction facing) {
		if (inMap(start.y, start.x) && inMap(goal.y, goal.x)) {
			start = map[start.y][start.x];
			goal = map[goal.y][goal.x];
			if (available.get(start) && available.get(goal)) {
				return BasicAStar(start, goal, facing);
			}
		}
		return Optional.empty();
	}

	/**
	 * Gets the optimal route between two locations via basic A*
	 * 
	 * @param start
	 *            the start location
	 * @param goal
	 *            the goal location
	 * @param start
	 *            the start location
	 * @param goal
	 *            the goal location
	 * @return a list of locations which form the optimal route to take
	 */
	private Optional<Route> BasicAStar(Location start, Location goal, Direction facing) {
		State startState = new State(start, facing);

		// The set of states already evaluated.
		Set<State> closedSet = new HashSet<State>();

		// The set of states to be explored
		Set<State> openSet = new HashSet<State>();

		// The map which links each state to the most efficient previous
		// state
		HashMap<State, State> cameFrom = new HashMap<State, State>();

		// The map which links each state with their distance from the start
		// location
		HashMap<State, Double> gScore = new HashMap<State, Double>();

		// The map which links each state with a heuristic distance to the
		// goal location
		HashMap<State, Double> fScore = new HashMap<State, Double>();

		// Initialise the heuristic maps
		gScore = initHScore(gScore);
		fScore = initHScore(fScore);

		// adds start and set the distance from the start for the start to be 0
		openSet.add(startState);
		gScore.put(startState, 0.0);
		fScore.put(startState, (double) start.manhattanDistance(goal));

		// Main loop
		while (!openSet.isEmpty()) {
			// get the next best node to expand
			State current = getLowest(openSet, fScore);
			// if the best node is the goal then finished
			if (current.getLocation().x == goal.x && current.getLocation().y == goal.y) {
				// return the optimal route
				return Optional.of(getPath(cameFrom, current, startState, goal));
			}
			// else continue
			// remove current form the openSet and add to closedSet
			openSet.remove(current);
			closedSet.add(current);

			// for each neighbour of current
			for (State neighbour : getNeighbours(current)) {
				// if the neighbour is not an obstacle
				if (available.get(neighbour.getLocation())) {
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
							fScore.put(neighbour, (double) neighbour.getLocation().manhattanDistance(goal) + gScore.get(neighbour));
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
	private HashMap<State, Double> initHScore(HashMap<State, Double> hScore) {
		for (Location[] x : map) {
			for (Location y : x) {
				hScore.put(new State(y, Direction.NORTH), Double.POSITIVE_INFINITY);
				hScore.put(new State(y, Direction.EAST), Double.POSITIVE_INFINITY);
				hScore.put(new State(y, Direction.SOUTH), Double.POSITIVE_INFINITY);
				hScore.put(new State(y, Direction.WEST), Double.POSITIVE_INFINITY);
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
	private State getLowest(Set<State> openSet, HashMap<State, Double> fScore) {
		Iterator<State> it = openSet.iterator();
		Double lowest = Double.POSITIVE_INFINITY;
		State best = null;
		while (it.hasNext()) {
			State x = it.next();
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
	private Route getPath(HashMap<State, State> cameFrom, State current, State startState, Location goal) {
		// The list of moves to make to reach goal
		LinkedList<Action> path = new LinkedList<Action>();
		if(cameFrom.get(current).getFacing().equals(current.getFacing())){
			if(cameFrom.get(current).getLocation().x == cameFrom.get(current).getLocation().x && cameFrom.get(current).getLocation().y == cameFrom.get(current).getLocation().y){
				//add stay still action
			}else{
				//Add forward action
			}
		}else if(cameFrom.get(current).getFacing().turnLeft().equals(current.getFacing())){
			//add turn left action
		}else{
			//add turn right action
		}
		return new Route(path, startState.getLocation(), goal);
	}

	/**
	 * Gets the neighbours of a node
	 * 
	 * @param node
	 *            the location to find the neighbours for
	 * @return the array of neighbours
	 */
	private LinkedList<State> getNeighbours(State currentState) {
		LinkedList<State> neighbours = new LinkedList<State>();

		Location currentLocation = currentState.getLocation();
		Direction currentDirection = currentState.getFacing();

		// check for direction of current facing and adds that type of movement
		// to the neighbours available
		switch (currentDirection) {
		case NORTH:
			if (inMap(currentLocation.y + 1, currentLocation.x)) {
				neighbours.add(new State(map[currentLocation.y + 1][currentLocation.x], currentDirection));
			}
			break;
			
		case EAST:
			if (inMap(currentLocation.y, currentLocation.x + 1)) {
				neighbours.add(new State(map[currentLocation.y][currentLocation.x + 1], currentDirection));
			}
			break;
			
		case SOUTH:
			if (inMap(currentLocation.y - 1, currentLocation.x)) {
				neighbours.add(new State(map[currentLocation.y - 1][currentLocation.x], currentDirection));
			}
			break;
			
		case WEST:
			if (inMap(currentLocation.y, currentLocation.x - 1)) {
				neighbours.add(new State(map[currentLocation.y][currentLocation.x - 1], currentDirection));
			}
			break;
		}
		
		//add neighbours which are always available
		neighbours.add(new State(currentLocation, currentDirection.turnLeft()));
		neighbours.add(new State(currentLocation, currentDirection.turnRight()));
		neighbours.add(currentState);
		
		return neighbours;
	}

	private boolean inMap(int y, int x) {
		return (y >= 0 && y < map.length && x >= 0 && x < map[y].length);
	}
}
