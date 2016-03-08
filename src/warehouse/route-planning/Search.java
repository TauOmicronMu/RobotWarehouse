package warehouse.routePlanning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import org.w3c.dom.Node;

import warehouse.action.Action;
import warehouse.action.DropoffAction;
import warehouse.action.IdleAction;
import warehouse.action.MoveAction;
import warehouse.action.PickupAction;
import warehouse.action.TurnAction;
import warehouse.util.Direction;
import warehouse.util.Location;
import warehouse.util.Route;;

public class Search {
	private HashMap<Location, Boolean> available;
	private Location[][] map;
	private final int pickUpTime = 10;
	private final int dropOffTime = 8;
	ArrayList<State> states;

	/**
	 * Constructor which sets up the map and obstacles
	 */
	public Search(Map m) {
		map = m.getMap();
		available = m.getAvailable();
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
	 * Gets the route around a series of locations
	 * 
	 * @param toVisit
	 *            the list of locations to visit
	 * @param facing
	 *            the initial facing of the robot
	 * @return the final route around all locations including pick up and drop
	 *         off actions
	 */
	public Optional<Route> getRoute(LinkedList<Location> toVisit, Direction facing) {
		// makes the final route variable
		Route finalRoute = new Route(new LinkedList<Action>(), toVisit.getFirst(), toVisit.getFirst());

		// sets the initial route to be the first edge in the search
		Optional<LinkedList<State>> currentEdge = getEdge(toVisit.get(0), toVisit.get(1), facing);
		if (currentEdge.isPresent()) {
			finalRoute = createRoute(currentEdge.get());
		} else {
			return Optional.empty();
		}

		// keeps appending sections of route to the final route until all
		// locations have been visited
		Direction currentFacing = facing;
		for (int edge = 1; edge < toVisit.size() -1; edge++) {
			currentEdge = getEdge(toVisit.get(edge), toVisit.get(edge + 1), currentFacing);
			if (currentEdge.isPresent()) {
				currentFacing = currentEdge.get().getLast().getFacing();
				finalRoute = appendRoute(finalRoute, createRoute(currentEdge.get()));
			} else {
				return Optional.empty();
			}
		}

		finalRoute.actions.add(new DropoffAction());
		finalRoute.totalDistance += dropOffTime;
		return Optional.of(finalRoute);
	}

	public Optional<Integer> getEstimatedDistance(Location start, Location goal) {
		if (inMap(start.y, start.x) && inMap(goal.y, goal.x)) {
			start = map[start.y][start.x];
			goal = map[goal.y][goal.x];
			if (available.get(start) && available.get(goal)) {
				return DistanceAStar(start, goal);
			}
		}
		return Optional.empty();
	}

	/**
	 * Gets the list of states which represent the optimal route found by A*
	 * 
	 * @param start
	 *            the start location
	 * @param goal
	 *            the goal location
	 * @param facing
	 *            the starting facing
	 * @return the list of states
	 */
	private Optional<LinkedList<State>> getEdge(Location start, Location goal, Direction facing) {
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
	 * Gets the optimal route between two locations via A* using direction of
	 * turning
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
	private Optional<LinkedList<State>> StateAStar(Location start, Location goal, Direction facing) {
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
				return Optional.of(getPath(cameFrom, current));
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

	private Optional<Integer> DistanceAStar(Location start, Location goal) {
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
	 * Assembles a list of states in order from a series of linked states
	 * 
	 * @param cameFrom
	 *            a map which maps a state to the state which came before it in
	 *            search
	 * @param current
	 *            the current state
	 * @return the list of states
	 */
	private int getPathSize(HashMap<Location, Location> cameFrom, Location current) {
		// The list of moves to make to reach goal
		LinkedList<Location> path = new LinkedList<Location>();
		path.add(current);
		while (cameFrom.containsKey(current)) {
			// Gets the previous location and facing
			path.addFirst(cameFrom.get(current));
			current = cameFrom.get(current);
		}
		return path.size();
	}

	/**
	 * Creates a route from a given list of states
	 * 
	 * @param allStates
	 *            the list of all states passed through
	 * @return the route generated
	 */
	private Route createRoute(LinkedList<State> allStates) {
		// The list of moves to make to reach goal
		LinkedList<Action> path = new LinkedList<Action>();

		for (int current = 1; current < allStates.size(); current++) {
			Location currentLocation = allStates.get(current).getLocation();
			Direction currentFacing = allStates.get(current).getFacing();
			Location previousLocation = allStates.get(current - 1).getLocation();
			Direction previousFacing = allStates.get(current - 1).getFacing();

			// checks the type of action to add to the route
			if (previousFacing.equals(currentFacing)) {
				if (previousLocation.x == currentLocation.x && previousLocation.y == currentLocation.y) {
					path.addFirst(new IdleAction(1));
				} else {
					path.addFirst(new MoveAction(1, previousLocation));
				}
			} else if (previousFacing.turnLeft().equals(currentFacing)) {
				path.addFirst(new TurnAction(90));
			} else {
				path.addFirst(new TurnAction(-90));
			}
		}

		Route route = new Route(path, allStates.getFirst().getLocation(), allStates.getLast().getLocation());
		route.totalDistance = path.size();
		route.finalFacing = allStates.getLast().getFacing();
		return route;
	}

	/**
	 * Joins one route onto another and adds a pickup action in the middle
	 * 
	 * @param r1
	 *            the first route
	 * @param r2
	 *            the second route
	 * @return the total route
	 */
	private Route appendRoute(Route r1, Route r2) {
		LinkedList<Action> totalPath = new LinkedList<Action>();
		totalPath = r1.actions;
		totalPath.add(new PickupAction());
		totalPath.addAll(r2.actions);
		Route route = new Route(totalPath, r1.start, r2.end);
		route.totalDistance = r1.totalDistance + r2.totalDistance + pickUpTime;
		route.finalFacing = r2.finalFacing;
		return route;
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

	/**
	 * Gets the state for a given location and direction
	 * 
	 * @param location
	 *            the location of the state
	 * @param facing
	 *            the facing of the state
	 * @return the state if found, or null vlaue if not
	 */
	private Optional<State> getState(Location location, Direction facing) {
		for (State s : states) {
			if (s.getLocation().equals(location) && s.getFacing().equals(facing)) {
				return Optional.of(s);
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns whether a given location is inside the map or not
	 * 
	 * @param y
	 *            the y coordinate
	 * @param x
	 *            the x coordinate
	 * @return boolean representing whether the location is in the map or not
	 */
	private boolean inMap(int y, int x) {
		return (y >= 0 && y < map.length && x >= 0 && x < map[y].length);
	}
}
