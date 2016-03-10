package warehouse.routePlanning.JUnitTests;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.job.Job;
import warehouse.routePlanning.Map;
import warehouse.routePlanning.Search;
import warehouse.routePlanning.TSP;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
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
		tsp = new TSP();
	}

	@Test
	public void testMapCreation() {
		// Tests creation of a map and checks it works as intended
		// This test should pass at the moment

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
		// Tests straight line movements and turning single corners
		// These tests should all pass at the moment
		Optional<Route> o;
		Route returnedRoute;

		// first test
		o = s.getRoute(new Location(0, 0), new Location(0, 7), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 7);

		// second test
		o = s.getRoute(new Location(0, 0), new Location(11, 0), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 12);

		// third test
		o = s.getRoute(new Location(0, 0), new Location(2, 3), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 7);

		// fourth test
		o = s.getRoute(new Location(2, 3), new Location(0, 0), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 8);

		// fifth test
		o = s.getRoute(new Location(3, 5), new Location(4, 0), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 9);
	}

	@Test
	public void testComplexAStar() {
		// Tests more complex routes where optimisation is needed
		// These tests should all pass at the moment
		Optional<Route> o;
		Route returnedRoute;

		// first test
		o = s.getRoute(new Location(0, 3), new Location(5, 5), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 11);

		// second test
		o = s.getRoute(new Location(2, 3), new Location(8, 6), Direction.SOUTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 12);

		// third test
		o = s.getRoute(new Location(8, 6), new Location(3, 3), Direction.EAST);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 11);

		// fourth test
		o = s.getRoute(new Location(0, 1), new Location(5, 7), Direction.WEST);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 13);

	}

	@Test
	public void testInvalidAStar() {
		// Tests various places where invalid coordinates may cause problems
		// These tests should all pass at the moment

		// tests trying to get to location inside obstacle
		Optional<Route> o;

		o = s.getRoute(new Location(0, 0), new Location(1, 1), Direction.NORTH);
		assertEquals(o.isPresent(), false);

		// tests trying to get to location outside of map
		o = s.getRoute(new Location(0, 0), new Location(0, -1), Direction.NORTH);
		assertEquals(o.isPresent(), false);

		// tests starting outside of map
		o = s.getRoute(new Location(0, -2), new Location(0, 0), Direction.NORTH);
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
		// Tests simple TSP problems with few obstacles to deal with
		// These tests will fail since TSP is currently not implemented in the
		// style of the final system
		LinkedList<ItemPickup> pickups;
		Location dropOff;
		Location start;
		Direction facing;
		Optional<Route> o;
		Route returnedRoute;

		// test 1
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(0, 6), 1));
		pickups.add(new ItemPickup("test", new Location(4, 6), 1));
		dropOff = new Location(4, 7);
		start = new Location(0, 7);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 10);
		assertEquals(returnedRoute.end, dropOff);
		assertEquals(returnedRoute.start, start);

		// test 2
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(0, 6), 1));
		pickups.add(new ItemPickup("test", new Location(8, 6), 1));
		dropOff = new Location(4, 7);
		start = new Location(0, 7);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 19);
		assertEquals(returnedRoute.end, dropOff);
		assertEquals(returnedRoute.start, start);

		// test 3
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(3, 2), 1));
		pickups.add(new ItemPickup("test", new Location(2, 5), 1));
		dropOff = new Location(4, 7);
		start = new Location(0, 0);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 18);
		assertEquals(returnedRoute.end, dropOff);
		assertEquals(returnedRoute.start, start);

		// test 4 - same start and end location
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(3, 6), 1));
		pickups.add(new ItemPickup("test", new Location(5, 6), 1));
		dropOff = new Location(4, 7);
		start = new Location(4, 7);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 11);
		assertEquals(returnedRoute.end, dropOff);
		assertEquals(returnedRoute.start, start);
	}

	@Test
	public void testTSPComplex() {
		// Tests more complex TSP problems with many obstacles to deal with
		// These tests will fail since TSP is currently not implemented in the
		// style of the final system

		LinkedList<ItemPickup> pickups;
		Location dropOff;
		Location start;
		Direction facing;
		Optional<Route> o;
		Route returnedRoute;

		// test 1
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(3, 5), 1));
		pickups.add(new ItemPickup("test", new Location(5, 1), 1));
		pickups.add(new ItemPickup("test", new Location(8, 4), 1));
		dropOff = new Location(4, 7);
		start = new Location(2, 0);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 34);
		assertEquals(returnedRoute.end, dropOff);
		assertEquals(returnedRoute.start, start);

		// test 2
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(3, 2), 1));
		pickups.add(new ItemPickup("test", new Location(6, 4), 1));
		pickups.add(new ItemPickup("test", new Location(8, 0), 1));
		dropOff = new Location(4, 7);
		start = new Location(0, 3);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 35);
		assertEquals(returnedRoute.end, dropOff);
		assertEquals(returnedRoute.start, start);

		// test 3
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(2, 4), 1));
		pickups.add(new ItemPickup("test", new Location(6, 1), 1));
		pickups.add(new ItemPickup("test", new Location(9, 3), 1));
		dropOff = new Location(4, 7);
		start = new Location(5, 3);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 36);
		assertEquals(returnedRoute.end, dropOff);
		assertEquals(returnedRoute.start, start);
	}

	@Test
	public void testTSPLarge() {
		// Tests TSP problems with large amounts of locations to visit
		// These tests will fail since TSP is currently not implemented in the
		// style of the final system

		LinkedList<ItemPickup> pickups;
		Location dropOff;
		Location start;
		Direction facing;
		Optional<Route> o;
		Route returnedRoute;

		// test 1
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(11, 5), 1));
		pickups.add(new ItemPickup("test", new Location(5, 0), 1));
		pickups.add(new ItemPickup("test", new Location(2, 3), 1));
		pickups.add(new ItemPickup("test", new Location(3, 7), 1));
		pickups.add(new ItemPickup("test", new Location(8, 2), 1));
		pickups.add(new ItemPickup("test", new Location(0, 0), 1));
		dropOff = new Location(4, 7);
		start = new Location(11, 0);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 41);
		assertEquals(returnedRoute.end, dropOff);
		assertEquals(returnedRoute.start, start);

		// test 2
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(10, 7), 1));
		pickups.add(new ItemPickup("test", new Location(7, 0), 1));
		pickups.add(new ItemPickup("test", new Location(3, 4), 1));
		pickups.add(new ItemPickup("test", new Location(4, 0), 1));
		pickups.add(new ItemPickup("test", new Location(6, 3), 1));
		pickups.add(new ItemPickup("test", new Location(11, 2), 1));
		pickups.add(new ItemPickup("test", new Location(2, 2), 1));
		dropOff = new Location(4, 7);
		start = new Location(9, 2);
		facing = Direction.WEST;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 50);
		assertEquals(returnedRoute.end, dropOff);
		assertEquals(returnedRoute.start, start);

		// test 3
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(9, 5), 1));
		pickups.add(new ItemPickup("test", new Location(9, 0), 1));
		pickups.add(new ItemPickup("test", new Location(5, 2), 1));
		pickups.add(new ItemPickup("test", new Location(1, 0), 1));
		pickups.add(new ItemPickup("test", new Location(0, 4), 1));
		pickups.add(new ItemPickup("test", new Location(3, 2), 1));
		pickups.add(new ItemPickup("test", new Location(1, 6), 1));
		pickups.add(new ItemPickup("test", new Location(11, 6), 1));
		dropOff = new Location(4, 7);
		start = new Location(6, 6);
		facing = Direction.EAST;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 54);
		assertEquals(returnedRoute.end, dropOff);
		assertEquals(returnedRoute.start, start);
	}

	@Test
	public void testTSPInvalid() {
		// Tests various places where invalid coordinates may cause problems
		// These tests will pass, but for the wrong reasons: should fail since
		// TSP is currently not implemented in the
		// style of the final system

		LinkedList<ItemPickup> pickups;
		Location dropOff;
		Location start;
		Direction facing;
		Optional<Route> o;
		Route returnedRoute;

		// test 1 - location inside obstacle
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(1, 3), 1));
		pickups.add(new ItemPickup("test", new Location(6, 1), 1));
		pickups.add(new ItemPickup("test", new Location(9, 3), 1));
		dropOff = new Location(4, 7);
		start = new Location(5, 3);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), false);

		// test 2 - starting inside obstacle
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(2, 2), 1));
		pickups.add(new ItemPickup("test", new Location(6, 1), 1));
		pickups.add(new ItemPickup("test", new Location(9, 3), 1));
		dropOff = new Location(4, 7);
		start = new Location(4, 3);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), false);

		// test 3 - location outside of map
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(-1, 0), 1));
		pickups.add(new ItemPickup("test", new Location(6, 1), 1));
		pickups.add(new ItemPickup("test", new Location(9, 3), 1));
		dropOff = new Location(4, 7);
		start = new Location(5, 3);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), false);

		// test 4 - starting outside map
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(2, 2), 1));
		pickups.add(new ItemPickup("test", new Location(6, 1), 1));
		pickups.add(new ItemPickup("test", new Location(9, 3), 1));
		dropOff = new Location(4, 7);
		start = new Location(-3, 0);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), false);

	}
}
