package warehouse.routePlanning;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.util.Location;
import warehouse.util.Route;

public class UnitTests {
	private static Search s;
	private static TSP tsp;

	@BeforeClass
	public static void setUpClass() throws Exception {
		GridMap providedMap = MapUtils.createRealWarehouse();
		Map warehouseMap = new Map(providedMap);
		s = new Search(warehouseMap);
		tsp = new TSP(s);
	}

	@Test
	public void testMapCreation() {
		// tests real map
		GridMap providedMap = MapUtils.createRealWarehouse();
		Map testMap = new Map(providedMap);
		HashMap<Location, Boolean> available = testMap.getAvailable();
		Location[][] map = testMap.getMap();
		for (Location[] l : map) {
			for (Location loc : l) {
				assertEquals(available.get(loc), (!providedMap.isObstructed(loc.x, loc.y)));
			}
		}
	}

	@Test
	public void testSimpleAStar() {
		// first test
		Optional<Route> o = s.getRoute(new Location(0, 0), new Location(0, 7));
		assertEquals(o.isPresent(), true);
		Route route = o.get();
		assertEquals(route.totalDistance, 7);

		// second test
		o = s.getRoute(new Location(0, 0), new Location(11, 0));
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 11);

		// third test
		o = s.getRoute(new Location(0, 0), new Location(2, 3));
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 5);

		// fourth test
		o = s.getRoute(new Location(2, 3), new Location(0, 0));
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 5);

		// fifth test
		o = s.getRoute(new Location(3, 5), new Location(4, 0));
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 6);
	}

	@Test
	public void testComplexAStar() {
		// first test
		Optional<Route> o = s.getRoute(new Location(0, 3), new Location(5, 5));
		assertEquals(o.isPresent(), true);
		Route route = o.get();
		assertEquals(route.totalDistance, 9);

		// second test
		o = s.getRoute(new Location(2, 3), new Location(8, 6));
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 9);

		// third test
		o = s.getRoute(new Location(8, 6), new Location(3, 3));
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 8);

		// fourth test
		o = s.getRoute(new Location(0, 1), new Location(5, 7));
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 11);

	}

	@Test
	public void testInvalidAStar() {
		// tests trying to get to location inside obstacle
		Optional<Route> o = s.getRoute(new Location(0, 0), new Location(1, 1));
		assertEquals(o.isPresent(), false);

		// tests trying to get to location outside of map
		o = s.getRoute(new Location(0, 0), new Location(0, -1));
		assertEquals(o.isPresent(), false);

		// tests starting outside of map
		o = s.getRoute(new Location(0, -2), new Location(0, 0));
		assertEquals(o.isPresent(), false);

		// tests starting outside of map by 1 space
		o = s.getRoute(new Location(0, -1), new Location(0, 0));
		assertEquals(o.isPresent(), false);

		// tests starting inside obstacle
		o = s.getRoute(new Location(1, 1), new Location(0, 0));
		assertEquals(o.isPresent(), false);

	}

	@Test
	public void testTSPSimple() {
		// TODO
	}

	@Test
	public void testTSPComplex() {
		// TODO
	}

	@Test
	public void testTSPLArge() {
		// TODO
	}
}
