package warehouse.routePlanning;
import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import warehouse.util.Location;
import warehouse.util.Route;

public class UnitTests {
	private static Search s;
	  @BeforeClass
	    public static void setUpClass() throws Exception {
	        s = new Search();
	    }
	 
	    @Test
	    public void testSimpleAStar() {
	    	//first test
	    	Optional<Route> o = s.getRoute(new Location(0, 0), new Location(0,7));
	    	assertEquals(o.isPresent(), true);
				Route route = o.get();
			assertEquals(route.totalDistance, 7);
			
			//second test
			o = s.getRoute(new Location(0, 0), new Location(11, 0));
	    	assertEquals(o.isPresent(), true);
				route = o.get();
			assertEquals(route.totalDistance, 11);
			
			//third test
			o = s.getRoute(new Location(0, 0), new Location(2, 3));
	    	assertEquals(o.isPresent(), true);
				route = o.get();
			assertEquals(route.totalDistance, 5);
			
			//fourth test
			o = s.getRoute(new Location(2, 3), new Location(0, 0));
	    	assertEquals(o.isPresent(), true);
				route = o.get();
			assertEquals(route.totalDistance, 5);
			
			//fifth test
			o = s.getRoute(new Location(3, 6), new Location(4, 1));
	    	assertEquals(o.isPresent(), true);
				route = o.get();
			assertEquals(route.totalDistance, 6);
	    }

	    @Test
	    public void testComplexAStar() {
	    	//first test
	    	Optional<Route> o = s.getRoute(new Location(0, 3), new Location(5,5));
	    	assertEquals(o.isPresent(), true);
				Route route = o.get();
			assertEquals(route.totalDistance, 11);
			
			//second test
			o = s.getRoute(new Location(2, 3), new Location(8, 6));
	    	assertEquals(o.isPresent(), true);
				route = o.get();
			assertEquals(route.totalDistance, 11);
			
			//third test
			o = s.getRoute(new Location(8, 6), new Location(3, 3));
	    	assertEquals(o.isPresent(), true);
				route = o.get();
			assertEquals(route.totalDistance, 10);
			
			//fourth test
			o = s.getRoute(new Location(0, 1), new Location(5, 7));
	    	assertEquals(o.isPresent(), true);
				route = o.get();
			assertEquals(route.totalDistance, 11);
			
	    }

	    @Test
	    public void testInvalidAStar() {
	        //TODO Tests
	    }
}
