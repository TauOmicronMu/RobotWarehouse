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

		LinkedList<ItemPickup> pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(5, 3), 1));
//		pickups.add(new ItemPickup("test", new Location(2, 2), 1));
//		pickups.add(new ItemPickup("test", new Location(5, 1), 1));
//		pickups.add(new ItemPickup("test", new Location(9, 5), 1));
//		pickups.add(new ItemPickup("test", new Location(8, 2), 1));		
		
		Optional<Route> chosenRoute = tsp.getShortestRoute(new Job(new Location(4, 7), pickups), new Location(0, 1), Direction.SOUTH);
		if(chosenRoute.isPresent()){
			Route r = chosenRoute.get();
			System.out.println(r.totalDistance);
			System.out.println(r.finalFacing);
			for(Action a: r.actions){
			}
		}else{
			System.out.println("Could not find a route");
		}
	}
}
