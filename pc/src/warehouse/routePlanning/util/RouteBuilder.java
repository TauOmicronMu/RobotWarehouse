package warehouse.routePlanning.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import warehouse.action.Action;
import warehouse.action.DropoffAction;
import warehouse.action.IdleAction;
import warehouse.action.MoveAction;
import warehouse.action.PickupAction;
import warehouse.action.TurnAction;
import warehouse.routePlanning.search.DirectionalSearch;
import warehouse.routePlanning.search.State;
import warehouse.routePlanning.search.StateRoute;
import warehouse.util.Direction;
import warehouse.util.Location;
import warehouse.util.Route;

public class RouteBuilder {
	private final int pickUpTime = 10;
	private final int dropOffTime = 8;
	private Location dropLocation;
	private DirectionalSearch ds;

	public RouteBuilder(Location dropOffLocation, Map m) {
		this.dropLocation = dropOffLocation;
		ds = new DirectionalSearch(m);
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
		Route finalRoute = new Route(new LinkedList<Action>(), toVisit.getFirst(), toVisit.getFirst(), facing);

		// sets the initial route to be the first edge in the search
		if(!(toVisit.size() > 1)){
			return Optional.empty();
		}
		Optional<StateRoute> currentEdge = ds.getRoute(toVisit.get(0), toVisit.get(1), facing);
		if (currentEdge.isPresent()) {
			LinkedList<State> route = currentEdge.get().getRoute();
			finalRoute = createRoute(route);
		} else {
			return Optional.empty();
		}

		// keeps appending sections of route to the final route until all
		// locations have been visited
		Direction currentFacing = currentEdge.get().getRoute().getLast().getFacing();
		for (int edge = 1; edge < toVisit.size() - 1; edge++) {
			currentEdge = ds.getRoute(toVisit.get(edge), toVisit.get(edge + 1), currentFacing);
			if (currentEdge.isPresent()) {
				LinkedList<State> route = currentEdge.get().getRoute();
				currentFacing = route.getLast().getFacing();
				finalRoute = appendRoute(finalRoute, createRoute(route));
			} else {
				return Optional.empty();
			}
		}
		finalRoute.actions.add(new DropoffAction());
		finalRoute.totalDistance += dropOffTime;
		return Optional.of(finalRoute);
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
					path.add(new IdleAction(1));
				} else {
					path.add(new MoveAction(1, currentLocation));
				}
			} else if (previousFacing.turnLeft().equals(currentFacing)) {
				path.add(new TurnAction(-90));
			} else {
				path.add(new TurnAction(90));
			}
		}

		Route route = new Route(path, allStates.getFirst().getLocation(), allStates.getLast().getLocation(),
				allStates.getLast().getFacing());
		route.totalDistance = path.size();
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
		List<Action> totalPath = new LinkedList<Action>();
		totalPath = r1.actions;
		if (r1.end.x == dropLocation.x && r1.end.y == dropLocation.y) {
			totalPath.add(new DropoffAction());
			r1.totalDistance += dropOffTime;
		} else {
			totalPath.add(new PickupAction());
			r1.totalDistance += pickUpTime;
		}
		totalPath.addAll(r2.actions);
		Route route = new Route(totalPath, r1.start, r2.end, r2.finalFacing);
		route.totalDistance = r1.totalDistance + r2.totalDistance;
		return route;
	}
}
