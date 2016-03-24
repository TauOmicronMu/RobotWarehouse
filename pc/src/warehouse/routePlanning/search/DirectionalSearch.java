package warehouse.routePlanning.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import warehouse.routePlanning.util.Map;
import warehouse.util.Direction;
import warehouse.util.Location;

public class DirectionalSearch extends Search {

	private ArrayList<State> states;

	public DirectionalSearch(Map m) {
		super(m);
		states = generateStates();
	}

	/**
	 * Generates all possible states in the map
	 * 
	 * @return all possible states
	 */
	private ArrayList<State> generateStates() {
		ArrayList<State> allStates = new ArrayList<State>();
		for (Location[] x : map) {
			for (Location y : x) {
				allStates.add(new State(y, Direction.NORTH));
				allStates.add(new State(y, Direction.EAST));
				allStates.add(new State(y, Direction.SOUTH));
				allStates.add(new State(y, Direction.WEST));
			}

		}
		return allStates;
	}

	/**
	 * Gets the route between two locations including turn and idle actions
	 * 
	 * @param start
	 *            the start location
	 * @param goal
	 *            the end location
	 * @param facing
	 *            the start facing
	 * @return a route in the form of a list of states, if a route is found
	 */
	public Optional<StateRoute> getRoute(Location start, Location goal, Direction facing) {
		if (inMap(start.y, start.x) && inMap(goal.y, goal.x)) {
			start = map[start.y][start.x];
			goal = map[goal.y][goal.x];
			if (available.get(start) && available.get(goal)) {
				return StateAStar(start, goal, facing);
			}
		}
		return Optional.empty();
	}

	/**
	 * Performs A* search between two locations including turn and idle actions
	 * 
	 * @param start
	 *            the start location
	 * @param goal
	 *            the end location
	 * @param facing
	 *            the start facing
	 * @return a state route (a list of states in order and a total distance)
	 */
	private Optional<StateRoute> StateAStar(Location start, Location goal, Direction facing) {
		State startState = getState(start, facing).get();

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
		gScore = initHScoreState(gScore);
		fScore = initHScoreState(fScore);

		// adds start and set the distance from the start for the start to be 0
		openSet.add(startState);
		gScore.put(startState, 0.0);
		fScore.put(startState, (double) start.manhattanDistance(goal));

		// Main loop
		while (!openSet.isEmpty()) {
			// get the next best node to expand
			State current = getLowestState(openSet, fScore);
			// if the best node is the goal then finished
			if (current.getLocation().x == goal.x && current.getLocation().y == goal.y) {
				// return the optimal route
				LinkedList<State> path = getPath(cameFrom, current);
				StateRoute route = new StateRoute(path, path.size());
				return Optional.of(route);
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
							fScore.put(neighbour,
									(double) neighbour.getLocation().manhattanDistance(goal) + gScore.get(neighbour));
						}
					}
				}
			}
		}
		// no route could be found
		return Optional.empty();
	}

	/**
	 * Sets up a map with infinity values for every location
	 * 
	 * @param hScore
	 *            a map which holds heuristic distances
	 * @return map with initialised values
	 */
	private HashMap<State, Double> initHScoreState(HashMap<State, Double> hScore) {
		for (Location[] x : map) {
			for (Location y : x) {
				hScore.put(getState(y, Direction.NORTH).get(), Double.POSITIVE_INFINITY);
				hScore.put(getState(y, Direction.EAST).get(), Double.POSITIVE_INFINITY);
				hScore.put(getState(y, Direction.SOUTH).get(), Double.POSITIVE_INFINITY);
				hScore.put(getState(y, Direction.WEST).get(), Double.POSITIVE_INFINITY);
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
	private State getLowestState(Set<State> openSet, HashMap<State, Double> fScore) {
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
	 * Assembles a list of states in order from a series of linked states
	 * 
	 * @param cameFrom
	 *            a map which maps a state to the state which came before it in
	 *            search
	 * @param current
	 *            the current state
	 * @return the list of states
	 */
	private LinkedList<State> getPath(HashMap<State, State> cameFrom, State current) {
		// The list of moves to make to reach goal
		LinkedList<State> path = new LinkedList<State>();
		path.add(current);
		while (cameFrom.containsKey(current)) {
			// Gets the previous location and facing
			path.addFirst(cameFrom.get(current));
			current = cameFrom.get(current);
		}
		return path;
	}

	/**
	 * Gets the neighbours of a current state
	 * 
	 * @param currentState
	 *            the current state
	 * @return the list of neighbours
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
				neighbours.add(getState(map[currentLocation.y + 1][currentLocation.x], currentDirection).get());
			}
			break;

		case EAST:
			if (inMap(currentLocation.y, currentLocation.x + 1)) {
				neighbours.add(getState(map[currentLocation.y][currentLocation.x + 1], currentDirection).get());
			}
			break;

		case SOUTH:
			if (inMap(currentLocation.y - 1, currentLocation.x)) {
				neighbours.add(getState(map[currentLocation.y - 1][currentLocation.x], currentDirection).get());
			}
			break;

		case WEST:
			if (inMap(currentLocation.y, currentLocation.x - 1)) {
				neighbours.add(getState(map[currentLocation.y][currentLocation.x - 1], currentDirection).get());
			}
			break;
		}

		// add neighbours which are always available
		neighbours.add(getState(currentLocation, currentDirection.turnLeft()).get());
		neighbours.add(getState(currentLocation, currentDirection.turnRight()).get());
		neighbours.add(currentState);

		return neighbours;
	}

	/**
	 * Gets the state for a given location and direction
	 * 
	 * @param location
	 *            the location of the state
	 * @param facing
	 *            the facing of the state
	 * @return the state if found, or null value if not
	 */
	private Optional<State> getState(Location location, Direction facing) {
		for (State s : states) {
			if (s.getLocation().equals(location) && s.getFacing().equals(facing)) {
				return Optional.of(s);
			}
		}
		return Optional.empty();
	}
}
