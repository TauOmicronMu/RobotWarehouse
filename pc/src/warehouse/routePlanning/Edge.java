package warehouse.routePlanning;

import warehouse.util.Location;

/**
 * Helper class to store an edge between two locations
 * 
 * @author Aidan
 *
 */
public class Edge {
	private Location start;
	private Location end;

	public Edge(Location start, Location end) {
		this.start = start;
		this.end = end;
	}

	public Location getStart() {
		return start;
	}

	public Location getEnd() {
		return end;
	}
}
