package warehouse.routePlanning.test;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.routePlanning.search.LocationRoute;
import warehouse.routePlanning.search.SimpleSearch;
import warehouse.routePlanning.util.Map;
import warehouse.util.Location;

public class SimpleSearchTest {

	private static SimpleSearch s;

	@BeforeClass
	public static void setUpClass() throws Exception {
		GridMap providedMap = MapUtils.createMarkingWarehouseMap();
		Map warehouseMap = new Map(providedMap);
		s = new SimpleSearch(warehouseMap);
	}

	@Test
	public void testSimpleAStar() {
		// Tests straight line movements and turning single corners
		// These tests should all pass at the moment
		Optional<LocationRoute> o;
		LocationRoute returnedRoute;

		// first test
		o = s.getRoute(new Location(0, 0), new Location(0, 7));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 7);

		// second test
		o = s.getRoute(new Location(0, 0), new Location(11, 0));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 11);

		// third test
		o = s.getRoute(new Location(0, 0), new Location(3, 3));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 6);

		// fourth test
		o = s.getRoute(new Location(3, 3), new Location(0, 0));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 6);

		// fifth test
		o = s.getRoute(new Location(3, 5), new Location(5, 2));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 5);

		// sixth test
		o = s.getRoute(new Location(3, 4), new Location(7, 4));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 6);
	}

	@Test
	public void testComplexAStar() {
		// Tests slightly more complex routes
		// These tests should all pass at the moment
		Optional<LocationRoute> o;
		LocationRoute returnedRoute;

		// first test
		o = s.getRoute(new Location(1, 4), new Location(10, 3));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 14);

		// second test
		o = s.getRoute(new Location(4, 3), new Location(10, 5));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 10);

		// third test
		o = s.getRoute(new Location(7, 1), new Location(4, 5));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 7);

		// fourth test
		o = s.getRoute(new Location(1, 3), new Location(7, 4));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 11);

		// fifth test
		o = s.getRoute(new Location(7, 4), new Location(1, 3));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 11);

		// sixth test
		o = s.getRoute(new Location(0, 0), new Location(11, 7));
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.getDistance(), 18);

	}

	@Test
	public void testInvalidAStar() {
		// Tests various places where invalid coordinates may cause problems
		// These tests should all pass at the moment
		Optional<LocationRoute> o;

		// tests trying to find a route into an obstacle
		o = s.getRoute(new Location(0, 0), new Location(2, 2));
		assertEquals(o.isPresent(), false);

		// tests starting inside obstacle
		o = s.getRoute(new Location(2, 2), new Location(0, 0));
		assertEquals(o.isPresent(), false);

		// tests trying to get to location outside of map
		o = s.getRoute(new Location(0, 0), new Location(0, -1));
		assertEquals(o.isPresent(), false);

		// tests trying to get to location outside of map by more than one space
		o = s.getRoute(new Location(0, 0), new Location(0, -3));
		assertEquals(o.isPresent(), false);

		// tests starting outside of map
		o = s.getRoute(new Location(0, -1), new Location(0, 0));
		assertEquals(o.isPresent(), false);

		// tests starting outside of map by more than one space
		o = s.getRoute(new Location(0, -5), new Location(0, 0));
		assertEquals(o.isPresent(), false);

	}
}
