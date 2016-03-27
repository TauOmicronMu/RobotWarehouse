package warehouse.routePlanning.test;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import warehouse.action.Action;
import warehouse.action.DropoffAction;
import warehouse.action.MoveAction;
import warehouse.action.PickupAction;
import warehouse.action.TurnAction;
import warehouse.job.Job;
import warehouse.routePlanning.TSP;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Route;

public class TSPTest {
	private static TSP tsp;

	private static int pickUpTime = 10;
	private static int dropOffTime = 8;

	@BeforeClass
	public static void setUpClass() throws Exception {
		tsp = new TSP();
	}

	@Test
	public void printedTest() {
		LinkedList<ItemPickup> pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("aa", new Location(4, 4), 1, 0, 15));
		pickups.add(new ItemPickup("ab", new Location(3, 0), 1, 0, 15));
		pickups.add(new ItemPickup("bj", new Location(7, 4), 1, 0, 15));
		pickups.add(new ItemPickup("bh", new Location(3, 3), 1, 0, 15));
		pickups.add(new ItemPickup("af", new Location(4, 0), 1, 0, 15));
		pickups.add(new ItemPickup("cf", new Location(6, 2), 1, 0, 15));

		Optional<Route> chosenRoute = tsp.getShortestRoute(new Job(new Location(2, 7), pickups), new Location(0, 0),
				Direction.NORTH);
		if (chosenRoute.isPresent()) {
			Route r = chosenRoute.get();
			System.out.println(r.totalDistance);
			System.out.println(r.finalFacing);
			for (Action a : r.actions) {
				if (a instanceof MoveAction) {
					System.out.println("Moving forwards to: " + ((MoveAction) a).destination.x + " "
							+ ((MoveAction) a).destination.y);
				} else if (a instanceof TurnAction) {
					System.out.println("Turning: " + ((TurnAction) a).angle);
				} else if (a instanceof PickupAction) {
					System.out.println("Picking up");
				} else if (a instanceof DropoffAction) {
					System.out.println("Dropping off");
				}
			}
		} else {
			System.out.println("Could not find a route");
		}
	}

	@Test
	public void testTSPSimple() {
		// Tests simple TSP problems with few obstacles to deal with
		// These tests will pass
		LinkedList<ItemPickup> pickups;
		Location dropOff;
		Location start;
		Direction facing;
		Optional<Route> o;
		Route returnedRoute;

		// test 1
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(0, 6), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(0, 4), 1, 0, 10));
		dropOff = new Location(2, 7);
		start = new Location(0, 0);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 10 + dropOffTime + pickUpTime * pickups.size());
		System.out.println(returnedRoute.end);
		assertEquals(returnedRoute.end.x, dropOff.x);
		assertEquals(returnedRoute.end.y, dropOff.y);
		assertEquals(returnedRoute.start.x, start.x);
		assertEquals(returnedRoute.start.y, start.y);

		// test 2
		facing = Direction.SOUTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 12 + dropOffTime + pickUpTime * pickups.size());
		assertEquals(returnedRoute.end.x, dropOff.x);
		assertEquals(returnedRoute.end.y, dropOff.y);
		assertEquals(returnedRoute.start.x, start.x);
		assertEquals(returnedRoute.start.y, start.y);

		// test 3 - same start and end location
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(0, 6), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(4, 6), 1, 0, 10));
		dropOff = new Location(2, 7);
		start = new Location(2, 7);
		facing = Direction.EAST;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 16 + dropOffTime + pickUpTime * pickups.size());
		assertEquals(returnedRoute.end.x, dropOff.x);
		assertEquals(returnedRoute.end.y, dropOff.y);
		assertEquals(returnedRoute.start.x, start.x);
		assertEquals(returnedRoute.start.y, start.y);
	}

	@Test
	public void testTSPComplex() {
		// Tests more complex TSP problems with many obstacles to deal with
		// These tests will pass

		LinkedList<ItemPickup> pickups;
		Location dropOff;
		Location start;
		Direction facing;
		Optional<Route> o;
		Route returnedRoute;

		// test 1
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(3, 5), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(5, 2), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(8, 4), 1, 0, 10));
		dropOff = new Location(2, 7);
		start = new Location(2, 0);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 26 + dropOffTime + pickUpTime * pickups.size());
		assertEquals(returnedRoute.end.x, dropOff.x);
		assertEquals(returnedRoute.end.y, dropOff.y);
		assertEquals(returnedRoute.start.x, start.x);
		assertEquals(returnedRoute.start.y, start.y);

		// test 2
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(1, 3), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(7, 4), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(10, 3), 1, 0, 10));
		dropOff = new Location(2, 7);
		start = new Location(0, 0);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 42 + dropOffTime + +pickUpTime * pickups.size());
		assertEquals(returnedRoute.end.x, dropOff.x);
		assertEquals(returnedRoute.end.y, dropOff.y);
		assertEquals(returnedRoute.start.x, start.x);
		assertEquals(returnedRoute.start.y, start.y);

		// test 3 - same start and end
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(1, 4), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(10, 4), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(4, 3), 1, 0, 10));
		dropOff = new Location(2, 7);
		start = new Location(2, 7);
		facing = Direction.EAST;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 44 + dropOffTime + +pickUpTime * pickups.size());
		assertEquals(returnedRoute.end.x, dropOff.x);
		assertEquals(returnedRoute.end.y, dropOff.y);
		assertEquals(returnedRoute.start.x, start.x);
		assertEquals(returnedRoute.start.y, start.y);
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
		pickups.add(new ItemPickup("test", new Location(11, 5), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(5, 0), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(1, 3), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(3, 7), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(8, 2), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(10, 1), 1, 0, 10));
		dropOff = new Location(2, 7);
		start = new Location(0, 0);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 70 + dropOffTime * 2 + +pickUpTime * pickups.size());
		assertEquals(returnedRoute.end.x, dropOff.x);
		assertEquals(returnedRoute.end.y, dropOff.y);
		assertEquals(returnedRoute.start.x, start.x);
		assertEquals(returnedRoute.start.y, start.y);

		// test 2
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(10, 7), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(7, 0), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(3, 4), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(4, 0), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(6, 5), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(11, 2), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(2, 1), 1, 0, 10));
		dropOff = new Location(2, 7);
		start = new Location(0, 0);
		facing = Direction.WEST;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 67 + dropOffTime * 2 + +pickUpTime * pickups.size());
		assertEquals(returnedRoute.end.x, dropOff.x);
		assertEquals(returnedRoute.end.y, dropOff.y);
		assertEquals(returnedRoute.start.x, start.x);
		assertEquals(returnedRoute.start.y, start.y);

		// test 3
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(8, 5), 1, 0, 15));
		pickups.add(new ItemPickup("test", new Location(9, 0), 1, 0, 15));
		pickups.add(new ItemPickup("test", new Location(5, 2), 1, 0, 10));
		pickups.add(new ItemPickup("test", new Location(1, 0), 1, 0, 15));
		pickups.add(new ItemPickup("test", new Location(0, 4), 1, 0, 15));
		pickups.add(new ItemPickup("test", new Location(3, 2), 1, 0, 15));
		pickups.add(new ItemPickup("test", new Location(1, 6), 1, 0, 15));
		pickups.add(new ItemPickup("test", new Location(11, 6), 1, 0, 10));
		dropOff = new Location(2, 7);
		start = new Location(2, 7);
		facing = Direction.EAST;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), true);
		returnedRoute = o.get();
		assertEquals(returnedRoute.totalDistance, 84 + dropOffTime * 3 + +pickUpTime * pickups.size());
		assertEquals(returnedRoute.end.x, dropOff.x);
		assertEquals(returnedRoute.end.y, dropOff.y);
		assertEquals(returnedRoute.start.x, start.x);
		assertEquals(returnedRoute.start.y, start.y);
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

		// test 1 - location inside obstacle
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(1, 3), 1));
		pickups.add(new ItemPickup("test", new Location(6, 1), 1));
		pickups.add(new ItemPickup("test", new Location(9, 3), 1));
		dropOff = new Location(2, 7);
		start = new Location(5, 3);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), false);

		// test 2 - starting inside obstacle
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(1, 1), 1));
		pickups.add(new ItemPickup("test", new Location(3, 1), 1));
		pickups.add(new ItemPickup("test", new Location(7, 1), 1));
		dropOff = new Location(2, 7);
		start = new Location(2, 2);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), false);

		// test 3 - location outside of map
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(-1, 0), 1));
		pickups.add(new ItemPickup("test", new Location(6, 1), 1));
		pickups.add(new ItemPickup("test", new Location(9, 1), 1));
		dropOff = new Location(2, 7);
		start = new Location(5, 3);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), false);

		// test 4 - starting outside map
		pickups = new LinkedList<ItemPickup>();
		pickups.add(new ItemPickup("test", new Location(1, 1), 1));
		pickups.add(new ItemPickup("test", new Location(1, 7), 1));
		pickups.add(new ItemPickup("test", new Location(2, 6), 1));
		dropOff = new Location(2, 7);
		start = new Location(-3, 0);
		facing = Direction.NORTH;

		o = tsp.getShortestRoute(new Job(dropOff, pickups), start, facing);
		assertEquals(o.isPresent(), false);

	}
}
