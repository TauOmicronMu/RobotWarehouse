package warehouse.routePlanning;

import java.util.LinkedList;
import java.util.Optional;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.action.Action;
import warehouse.action.IdleAction;
import warehouse.action.MoveAction;
import warehouse.action.TurnAction;
import warehouse.job.Job;
import warehouse.util.*;

public class Test {

	public static void main(String[] args) {
		GridMap providedMap = MapUtils.createRealWarehouse();
		Map m = new Map(providedMap);
		Search s = new Search(m);
		TSP tsp = new TSP();
		Optional<Route> o = s.getRoute(new Location(0, 1), new Location(2, 2), Direction.NORTH);
		if (o.isPresent()) {
			Route route = o.get();
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
		LinkedList<ItemPickup> pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(5, 3), 1));
		pickups.add(new ItemPickup("test", new Location(2, 2), 1));
		pickups.add(new ItemPickup("test", new Location(5, 1), 1));
		pickups.add(new ItemPickup("test", new Location(9, 5), 1));
		pickups.add(new ItemPickup("test", new Location(8, 2), 1));		
		
		LinkedList<Edge> chosenRoute = tsp.getShortestRoute(new Job(new Location(4, 7), pickups), new Location(0, 1));
		double distance = 0;
		for (Edge e : chosenRoute) {
			distance += s.getRoute(e.getStart(), e.getEnd(), Direction.NORTH).get().totalDistance;
			System.out.println("(" + e.getStart().x + ", " + e.getStart().y + ") -->" + " (" + e.getEnd().x + ", "
					+ e.getEnd().y + "): " + s.getRoute(e.getStart(), e.getEnd(), Direction.NORTH).get().totalDistance);
		}
		System.out.println("Total Distance: " + distance);
	}
}
