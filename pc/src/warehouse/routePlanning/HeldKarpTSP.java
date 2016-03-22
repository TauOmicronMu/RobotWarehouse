package warehouse.routePlanning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.job.Job;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Route;

public class HeldKarpTSP {
	private Search s;
	
	public HeldKarpTSP(){
		GridMap providedMap = MapUtils.createRealWarehouse();
		Map m = new Map(providedMap);
		this.s = new Search(m);
	}

	Optional<Route> getShortestRoute(Job job, Location start, Direction facing) {
		//creates sets
		Set<Location> allLocations = new HashSet<Location>();
		Set<Edge> allEdges = new HashSet<Edge>();
		
		//creates a map of edges to distances
		HashMap<Edge, Integer> distances = new HashMap<Edge, Integer>();
		
		//stores whether a value has been added or not
		HashMap<Edge, Boolean> added = new HashMap<Edge, Boolean>();
		
		//initialises sets
		for(ItemPickup pickup: job.pickups){
			allLocations.add(pickup.location);
			for(ItemPickup pickup2: job.pickups){
				if(!pickup.equals(pickup2)){
					Edge e = new Edge(pickup.location, pickup2.location);
					allEdges.add(e);
					added.put(e, false);
					if(s.getEstimatedDistance(pickup.location, pickup2.location).isPresent()){
						distances.put(e, s.getEstimatedDistance(pickup.location, pickup2.location).get());
					}else{
						System.err.println("No route found between: " + pickup.location +" and " + pickup2.location);
					}
				}
			}
		}		
	}
}
