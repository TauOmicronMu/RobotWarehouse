package warehouse.routePlanning.SearchTest;

import java.util.Optional;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.routePlanning.Map;
import warehouse.routePlanning.SimpleSearch;
import warehouse.util.Location;

public class SimpleSearchTest {
	public static void main(String[] args) {
		GridMap providedMap = MapUtils.createMarkingWarehouseMap();
		Map m = new Map(providedMap);
		SimpleSearch ss = new SimpleSearch(m);
		
		Optional<Integer> distance = ss.getDistance(new Location(0,0), new Location(0,2));
		assert(distance.isPresent());
		assert(distance.get() == 3);
		
		System.out.println("tests passed");
	}
}
