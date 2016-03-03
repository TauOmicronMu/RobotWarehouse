package warehouse.routePlanning;

<<<<<<< HEAD
import warehouse.util.Location;
=======
import warehouse.Location;
>>>>>>> b538efcc8d671bc0ff2b3bcec9e0d8cfceb5dd2e

/**
 * Helper class to store an edge between two locations
 * @author Aidan
 *
 */
public class Edge {
	private Location start;
	private Location end;
	
	public Edge(Location start, Location end){
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
