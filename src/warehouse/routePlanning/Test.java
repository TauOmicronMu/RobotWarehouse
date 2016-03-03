package warehouse.routePlanning;

import java.util.Optional;
import warehouse.util.*;


public class Test {

	public static void main(String[] args) {
		Search s = new Search();
		Optional<Route> o = s.getRoute(new Location(0, 0), new Location(4, 7));
		if (o.isPresent()) {
			Route route = o.get();
			System.out.println("Total Distance: " + route.totalDistance);
		} else {
			System.out.println("no route found");
		}
	}
}
