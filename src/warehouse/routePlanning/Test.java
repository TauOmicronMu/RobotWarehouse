package warehouse.routePlanning;

import java.util.Optional;

import warehouse.Location;
import warehouse.Route;

public class Test {

	public static void main(String[] args) {
		System.out.println("DFDSHFBHFJHKGFJDSF");
		Search s = new Search();
		Optional<Route> o = s.getRoute(new Location(0,0), new Location(4, 7));
		if (o.isPresent()) {
			Route route = o.get();
			System.out.println("Total Distance: " + route.totalDistance);
		}else{
			System.out.println("no route found");
		}
	}
}
