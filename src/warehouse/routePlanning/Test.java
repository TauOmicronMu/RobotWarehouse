package warehouse.routePlanning;

import java.util.LinkedList;
import java.util.Optional;

import warehouse.action.Action;
import warehouse.action.Action.MoveAction;
import warehouse.job.Job;
import warehouse.util.*;


public class Test {

	public static void main(String[] args) {
		Search s = new Search();
		TSP tsp = new TSP(s);
		Optional<Route> o = s.getRoute(new Location(0, 1), new Location(5,4));
		if (o.isPresent()) {
			Route route = o.get();
			for (Action foundLocation: route.actions) {
				Action.MoveAction location = (MoveAction) foundLocation;
				System.out.println(location.destination.x + ", " + location.destination.y);
			}
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
		LinkedList<ItemPickup> pickups= new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location (5, 5), 1));
		pickups.add(new ItemPickup("test", new Location (8, 3), 1));
		pickups.add(new ItemPickup("test", new Location (3, 1), 1));
		LinkedList<Edge> chosenRoute = tsp.getShortestRoute(new Job (new Location(11, 5), pickups), new Location (0, 3));
		for(Edge e: chosenRoute){
			System.out.println("(" + e.getStart().x +", " + e.getStart().y + ") -->" + " (" + e.getEnd().x +", " + e.getEnd().y +")");
		}
	}
}
