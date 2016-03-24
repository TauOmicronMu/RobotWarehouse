package warehouse.routePlanning.test;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.routePlanning.search.DirectionalSearch;
import warehouse.routePlanning.search.StateRoute;
import warehouse.routePlanning.util.Map;
import warehouse.util.Direction;
import warehouse.util.Location;

public class DirectionalSearchTest {
	private static DirectionalSearch s;

	@BeforeClass
	public static void setUpClass() throws Exception {
		GridMap providedMap = MapUtils.createMarkingWarehouseMap();
		Map warehouseMap = new Map(providedMap);
		s = new DirectionalSearch(warehouseMap);
	}

	@Test
	public void testSimpleAStar() {
		// Tests simple searches, involving few turns
		// These tests should all pass at the moment
		Optional<StateRoute> o;
		StateRoute returnedRoute;

		// first test
		o = s.getRoute(new Location(0, 0), new Location(0, 7), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 7);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.NORTH);

		// second test
		o = s.getRoute(new Location(0, 0), new Location(0, 7), Direction.SOUTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 9);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.NORTH);

		// third test
		o = s.getRoute(new Location(0, 0), new Location(11, 0), Direction.EAST);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 11);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.EAST);

		// fourth test
		o = s.getRoute(new Location(0, 0), new Location(11, 0), Direction.WEST);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 13);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.EAST);

		// fifth test
		o = s.getRoute(new Location(8, 1), new Location(10, 4), Direction.EAST);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 6);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.NORTH);

		// sixth test
		o = s.getRoute(new Location(10, 5), new Location(1, 1), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 16);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.WEST);
	}

	@Test
	public void testComplexAStar() {
		// Tests slightly more complex routes
		// These tests should all pass at the moment
		Optional<StateRoute> o;
		StateRoute returnedRoute;

		// first test
		o = s.getRoute(new Location(1, 3), new Location(10, 4), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 16);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.SOUTH);

		// second test
		o = s.getRoute(new Location(1, 3), new Location(10, 4), Direction.SOUTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 16);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.NORTH);

		// third test
		o = s.getRoute(new Location(4, 4), new Location(7, 3), Direction.EAST);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 9);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.NORTH);

		// fourth test
		o = s.getRoute(new Location(4, 4), new Location(7, 3), Direction.NORTH);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 8);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.SOUTH);

		// fifth test
		o = s.getRoute(new Location(11, 5), new Location(4, 2), Direction.EAST);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 15);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.NORTH);

		// sixth test
		o = s.getRoute(new Location(1, 6), new Location(5, 2), Direction.EAST);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 10);
		assertEquals(returnedRoute.getRoute().getLast().getFacing(), Direction.EAST);

	}

	@Test
	public void testInvalidAStar() {
		// Tests various places where invalid coordinates may cause problems
		// These tests should all pass at the moment
		Optional<StateRoute> o;

		// tests trying to find a route into an obstacle
		o = s.getRoute(new Location(0, 0), new Location(2, 2), Direction.NORTH);
		assertEquals(o.isPresent(), false);

		// tests starting inside obstacle
		o = s.getRoute(new Location(2, 2), new Location(0, 0), Direction.NORTH);
		assertEquals(o.isPresent(), false);

		// tests trying to get to location outside of map
		o = s.getRoute(new Location(0, 0), new Location(0, -1), Direction.NORTH);
		assertEquals(o.isPresent(), false);

		// tests trying to get to location outside of map by more than one space
		o = s.getRoute(new Location(0, 0), new Location(0, -3), Direction.NORTH);
		assertEquals(o.isPresent(), false);

		// tests starting outside of map
		o = s.getRoute(new Location(0, -1), new Location(0, 0), Direction.NORTH);
		assertEquals(o.isPresent(), false);

		// tests starting outside of map by more than one space
		o = s.getRoute(new Location(0, -5), new Location(0, 0), Direction.NORTH);
		assertEquals(o.isPresent(), false);

	}
}
