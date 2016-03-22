package warehouse.routePlanning;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import warehouse.job.Job;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Route;

public class HeldKarpTSP {

	Optional<Route> getShortestRoute(Job job, Location start, Direction facing) {
		//creates sets
		Set<Location> allLocations = new HashSet<Location>();
		Set<Edge> allEdges = new HashSet<Edge>();
		
		//initialises sets
		for(ItemPickup pickup: job.pickups){
			allLocations.add(pickup.location);
			for(ItemPickup pickup2: job.pickups){
				if(!pickup.equals(pickup2)){
					allEdges.add(new Edge(pickup.location, pickup2.location));
				}
			}
		}
		
		//creates 
		
	}
}
