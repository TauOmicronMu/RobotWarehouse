package warehouse.routePlanning.JUnitTests;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.routePlanning.Map;
import warehouse.routePlanning.Search;
import warehouse.routePlanning.TSP;

public class MultipleRobotTests {
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
	public void testSimpleAStar() {
		// Routes have very little interaction in these tests (checking multi
		// robot systems work for single robot problems)
		// Multi Robot Systems have not yet been implemented
		// TODO
	}

	@Test
	public void testComplexAStar() {
		// Routes should have to deal with interaction (avoid crashes)
		// Multi Robot Systems have not yet been implemented
		// TODO
	}

	@Test
	public void testSimpleTSP() {
		// Routes have very little interaction in these tests (checking multi
		// robot systems work for single robot problems)
		// Multi Robot Systems have not yet been implemented
		// TODO
	}

	@Test
	public void testComplexTSP() {
		// Routes should have to deal with interaction (avoid crashes)
		// Multi Robot Systems have not yet been implemented
		// TODO
	}
}