package warehouse.routePlanning;

import java.util.LinkedList;
import java.util.Optional;

import warehouse.Location;

public class Test {

	public static void main(String[] args) {
		Search s = new Search();
		Optional<LinkedList<Location>> o = s.getRoute(new Location(0,0), new Location(11, 7));
		if (o.isPresent()) {
			LinkedList<Location> route = o.get();
			for (Location r : route) {
				System.out.println(r.x + ", " + r.y);
			}
		}else{
			System.out.println("no route found");
		}
	}
}
