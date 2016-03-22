package warehouse.routePlanning;

import java.util.LinkedList;
import java.util.Optional;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.action.*;
import warehouse.job.Job;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Route;

public class Test {

	public static void main(String[] args) {
		GridMap providedMap = MapUtils.createRealWarehouse();
		Map m = new Map(providedMap);
		TSP tsp = new TSP();

		LinkedList<ItemPickup> pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(5, 3), 1, 0, 15));
		pickups.add(new ItemPickup("test", new Location(2, 2), 1, 0, 15));
		pickups.add(new ItemPickup("test", new Location(5, 1), 1, 0, 15));
		pickups.add(new ItemPickup("test", new Location(9, 5), 1, 0, 15));
		pickups.add(new ItemPickup("test", new Location(8, 2), 1, 0, 15));		
		
		Optional<Route> chosenRoute = tsp.getShortestRoute(new Job(new Location(4, 7), pickups), new Location(0, 1), Direction.NORTH);
		if(chosenRoute.isPresent()){
			Route r = chosenRoute.get();
			System.out.println(r.totalDistance);
			System.out.println(r.finalFacing);
			for(Action a: r.actions){
				if(a instanceof MoveAction){
					System.out.println("Moving forwards to: " +((MoveAction) a).destination.x +" " + ((MoveAction) a).destination.y);
				}else if (a instanceof TurnAction){
					System.out.println("Turning: " +((TurnAction) a).angle);
				}else if (a instanceof PickupAction){
					System.out.println("Picking up");
				}else if (a instanceof DropoffAction){
					System.out.println("Dropping off");
				}
			}
		}else{
			System.out.println("Could not find a route");
		}
	}
}
