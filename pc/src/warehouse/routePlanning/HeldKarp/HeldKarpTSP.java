package warehouse.routePlanning.HeldKarp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.job.Job;
import warehouse.routePlanning.Edge;
import warehouse.routePlanning.Map;
import warehouse.routePlanning.Search;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Route;

public class HeldKarpTSP {
	private Search s;
	private Map m;

	public HeldKarpTSP() {
		GridMap providedMap = MapUtils.createRealWarehouse();
		m = new Map(providedMap);
	}

	Optional<Route> getShortestRoute(Job job, Location start, Direction facing) {
		this.s = new Search(m, job.dropLocation);

		// creates sets
		List<Location> allLocations = new ArrayList<Location>();
		List<Edge> allEdges = new ArrayList<Edge>();

		// links an id number to every location
		HashMap<Integer, Location> ids = new HashMap<Integer, Location>();

		// creates a map of edges to distances
		HashMap<Edge, Integer> distances = new HashMap<Edge, Integer>();

		// initialises the list of allLocations as nodes
		int i = 0;
		for (ItemPickup pickup : job.pickups) {
			allLocations.add(pickup.location);
			ids.put(i, pickup.location);
			i++;
		}

		// initialises the list of edges
		for (Location l : allLocations) {
			for (Location l2 : allLocations) {
				allEdges.add(new Edge(l, l2));
			}
		}

		for (int node = 1; node < allLocations.size(); node++) {
				Edge found = getEdge(allEdges, allLocations.get(0), allLocations.get(node));
				distances.put(found, getDistance(found));
		}
		
		for(int node =2; node < allLocations.size(); node++){
			
		}
		
		// TODO return method
		return null;
	}

	/**
	 * Helper method which gets a distance form route planning
	 * @param e the edge to find the distance for
	 * @return the distance found or null if a route could not be found
	 */
	private Integer getDistance(Edge e) {
		Optional<Integer> estimatedDistance = s.getEstimatedDistance(e.getStart(), e.getEnd());
		if (estimatedDistance.isPresent()) {
			return estimatedDistance.get();
		} else {
			System.err.println("could not find a route");
		}
		return null;
	}

	/**
	 * Helper method which finds a specific edge in the list of all edges, given
	 * two locations
	 * 
	 * @param allEdges
	 *            the list of all edges
	 * @param start
	 *            the start location
	 * @param end
	 *            the end location
	 * @return the found location (or null if none is found)
	 */
	private Edge getEdge(List<Edge> allEdges, Location start, Location end) {
		Edge found = null;
		for (Edge e : allEdges) {
			if ((e.getStart().x == start.x && e.getStart().y == start.y)
					&& (e.getEnd().x == end.x && e.getEnd().y == end.y)) {
				found = e;
			}
		}
		assert (found != null);
		return found;
	}
}
