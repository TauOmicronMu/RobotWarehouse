package warehouse.routePlanning.searchTest;

import java.util.Optional;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.routePlanning.Map;
import warehouse.routePlanning.search.DirectionalSearch;
import warehouse.routePlanning.search.StateRoute;
import warehouse.util.Direction;
import warehouse.util.Location;

public class DirectionalSearchTest {
	public static void main(String[] args) {
		GridMap providedMap = MapUtils.createMarkingWarehouseMap();
		Map m = new Map(providedMap);
		DirectionalSearch ds = new DirectionalSearch(m);
		Optional<StateRoute> distance = ds.getRoute(new Location(0, 0), new Location(3, 5), Direction.NORTH);
		
		assert (distance.isPresent());
		System.out.println(distance.get());
		System.out.println("total distance is: " + distance.get().getDistance());
		
	}
}
