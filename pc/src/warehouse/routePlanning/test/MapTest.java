package warehouse.routePlanning.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.routePlanning.util.Map;
import warehouse.util.Location;

public class MapTest {

	@Test
	public void testMapCreation() {
		// Tests creation of a map and checks it works as intended
		// This test should pass at the moment

		GridMap providedMap = MapUtils.createMarkingWarehouseMap();
		Map testMap = new Map(providedMap);
		HashMap<Location, Boolean> available = testMap.getAvailable();
		Location[][] map = testMap.getMap();
		for (Location[] l : map) {
			for (Location loc : l) {
				if (available.get(loc)) {
					System.out.print("-");
				} else {
					System.out.print("X");
				}
				assertEquals(available.get(loc), (!providedMap.isObstructed(loc.x, loc.y)));
			}
			System.out.println();
		}
	}
}
