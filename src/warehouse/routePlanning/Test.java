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
		TSP tsp = new TSP(s);
		double distance = 0;
		Optional<Route> o = s.getRoute(new Location(6, 6), new Location(11, 6), Direction.EAST);
		if (o.isPresent()) {
			Route route = o.get();
			distance += route.totalDistance;
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
		
		o = s.getRoute(new Location(11, 6), new Location(9, 5), Direction.EAST);
		if (o.isPresent()) {
			Route route = o.get();
			distance += route.totalDistance;
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
		
		o = s.getRoute(new Location(9, 5), new Location(9, 0), Direction.SOUTH);
		if (o.isPresent()) {
			Route route = o.get();
			distance += route.totalDistance;
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
		
		o = s.getRoute(new Location(9, 0), new Location(5, 2), Direction.SOUTH);
		if (o.isPresent()) {
			Route route = o.get();
			distance += route.totalDistance;
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
		
		o = s.getRoute(new Location(5, 2), new Location(3, 2), Direction.NORTH);
		if (o.isPresent()) {
			Route route = o.get();
			distance += route.totalDistance;
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
		
		o = s.getRoute(new Location(3, 2), new Location(2, 0), Direction.NORTH);
		if (o.isPresent()) {
			Route route = o.get();
			distance += route.totalDistance;
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
		
		o = s.getRoute(new Location(1, 0), new Location(0, 4), Direction.WEST);
		if (o.isPresent()) {
			Route route = o.get();
			distance += route.totalDistance;
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
		
		o = s.getRoute(new Location(0, 4), new Location(1, 6), Direction.NORTH);
		if (o.isPresent()) {
			Route route = o.get();
			distance += route.totalDistance;
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
		
		o = s.getRoute(new Location(1, 6), new Location(4, 7), Direction.EAST);
		if (o.isPresent()) {
			Route route = o.get();
			distance += route.totalDistance;
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
		System.out.println(distance);
//		LinkedList<ItemPickup> pickups = new LinkedList<ItemPickup>();
//		pickups.add(new ItemPickup("test", new Location(5, 3), 1));
//		pickups.add(new ItemPickup("test", new Location(2, 2), 1));
//		pickups.add(new ItemPickup("test", new Location(5, 1), 1));
//		pickups.add(new ItemPickup("test", new Location(9, 5), 1));
//		pickups.add(new ItemPickup("test", new Location(8,2), 1));
//		pickups.add(new ItemPickup("test", new Location(6, 5), 1));
//		
//		LinkedList<Edge> chosenRoute = tsp.getShortestRoute(new Job(new Location(4, 7), pickups), new Location(0, 1));
//		double distance = 0;
//		for (Edge e : chosenRoute) {
//			distance += s.getRoute(e.getStart(), e.getEnd(), Direction.NORTH).get().totalDistance;
//			System.out.println("(" + e.getStart().x + ", " + e.getStart().y + ") -->" + " (" + e.getEnd().x + ", "
//					+ e.getEnd().y + "): " + s.getRoute(e.getStart(), e.getEnd(), Direction.NORTH).get().totalDistance);
//		}
//		System.out.println("Total Distance: " + distance);
	}
}
