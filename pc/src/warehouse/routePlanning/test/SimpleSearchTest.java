package warehouse.routePlanning.test;

import java.util.Optional;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.routePlanning.search.LocationRoute;
import warehouse.routePlanning.search.SimpleSearch;
import warehouse.routePlanning.util.Map;
import warehouse.util.Location;

public class SimpleSearchTest {
	public static void main(String[] args) {
		GridMap providedMap = MapUtils.createMarkingWarehouseMap();
		Map m = new Map(providedMap);
		SimpleSearch ss = new SimpleSearch(m);

		Optional<LocationRoute> distance = ss.getRoute(new Location(0, 0), new Location(0, 2));
		assert (distance.isPresent());
		assert (distance.get().getDistance() == 2);

		distance = ss.getRoute(new Location(0, 0), new Location(0, 5));
		assert (distance.isPresent());
		assert (distance.get().getDistance() == 5);

		distance = ss.getRoute(new Location(0, 0), new Location(3, 5));
		assert (distance.isPresent());
		System.out.println(distance.get());
		System.out.println("total distance is: " + distance.get().getDistance());

		System.out.println("tests passed");
	}
}
