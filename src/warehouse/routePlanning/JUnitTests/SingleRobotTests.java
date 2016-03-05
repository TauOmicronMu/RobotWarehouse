package warehouse.routePlanning.JUnitTests;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.routePlanning.Map;
import warehouse.routePlanning.Search;
import warehouse.routePlanning.TSP;
import warehouse.util.Direction;
import warehouse.util.Location;
import warehouse.util.Route;

public class SingleRobotTests {
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
		// tests creation of a map and checks it works as intended
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
		//Tests straight line movements and turning single corners
		// first test
		Optional<Route> o = s.getRoute(new Location(0, 0), new Location(0, 7), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		Route route = o.get();
		assertEquals(route.totalDistance, 7);

		// second test
		o = s.getRoute(new Location(0, 0), new Location(11, 0), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 12);

		// third test
		o = s.getRoute(new Location(0, 0), new Location(2, 3), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 7);

		// fourth test
		o = s.getRoute(new Location(2, 3), new Location(0, 0), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 8);

		// fifth test
		o = s.getRoute(new Location(3, 5), new Location(4, 0), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 9);
	}

	@Test
	public void testComplexAStar() {
		//Tests more complex routes where optimisation is needed
		// first test
		Optional<Route> o = s.getRoute(new Location(0, 3), new Location(5, 5), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		Route route = o.get();
		assertEquals(route.totalDistance, 11);

		// second test
		o = s.getRoute(new Location(2, 3), new Location(8, 6), Direction.SOUTH);
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 12);

		// third test
		o = s.getRoute(new Location(8, 6), new Location(3, 3), Direction.EAST);
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 11);

		// fourth test
		o = s.getRoute(new Location(0, 1), new Location(5, 7), Direction.WEST);
		assertEquals(o.isPresent(), true);
		route = o.get();
		assertEquals(route.totalDistance, 13);

	}

	@Test
	public void testInvalidAStar() {
		//Tests various places where invalid coordinates may cause problems
		// tests trying to get to location inside obstacle
		Optional<Route> o = s.getRoute(new Location(0, 0), new Location(1, 1), Direction.NORTH);
		assertEquals(o.isPresent(), false);

		// tests trying to get to location outside of map
		o = s.getRoute(new Location(0, 0), new Location(0, -1), Direction.NORTH);
		assertEquals(o.isPresent(), false);

		// tests starting outside of map
		o = s.getRoute(new Location(0, -2), new Location(0, 0),Direction.NORTH);
		assertEquals(o.isPresent(), false);

		// tests starting outside of map by 1 space
		o = s.getRoute(new Location(0, -1), new Location(0, 0), Direction.NORTH);
		assertEquals(o.isPresent(), false);

		// tests starting inside obstacle
		o = s.getRoute(new Location(1, 1), new Location(0, 0), Direction.NORTH);
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
	
	@Test
	public void testTSPInvalid(){
		//TODo
	}
}
