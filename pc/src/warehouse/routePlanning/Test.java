package warehouse.routePlanning;

import java.util.LinkedList;
import java.util.Optional;

import warehouse.action.*;
import warehouse.job.Job;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Route;

public class Test {

	public static void main(String[] args) {
		TSP tsp = new TSP();

		LinkedList<ItemPickup> pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("aa", new Location(4,4), 1, 0, 15));
		pickups.add(new ItemPickup("ab", new Location(3,0), 1, 0, 15));
		pickups.add(new ItemPickup("bj", new Location(7,4), 1, 0, 15));
		pickups.add(new ItemPickup("bh", new Location(3,3), 1, 0, 15));
		pickups.add(new ItemPickup("af", new Location(4,0), 1, 0, 15));		
		pickups.add(new ItemPickup("cf", new Location(6,2), 1, 0, 15));		
		
		Optional<Route> chosenRoute = tsp.getShortestRoute(new Job(new Location(2, 7), pickups), new Location(0,0), Direction.NORTH);
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
