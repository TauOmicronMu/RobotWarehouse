package warehouse.assign.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import warehouse.job.Job;
import warehouse.jobselection.JobWorth;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Robot;

public class JobWorthTest {

	private JobWorth testJobWorth;
	private Job testJob;
	private Location testLocation;
	private Robot testRobot;
	
	@Before
	public void setUp(){
		
		testLocation = new Location(4, 7);
		testRobot = new Robot("testrobot", testLocation, Direction.NORTH);
		
		LinkedList<ItemPickup> pickups = new LinkedList<ItemPickup>();
		
		ItemPickup testitem1 = new ItemPickup("testitem1", new Location(4, 0), 5);
		testitem1.reward = 10; testitem1.weight = 1;
		pickups.add(testitem1);
		
		ItemPickup testitem2 = new ItemPickup("testitem2", new Location(5, 2), 10);
		testitem2.reward = 5; testitem2.weight = 1.2;
		pickups.add(testitem2);
		
		ItemPickup testitem3 = new ItemPickup("testitem3", new Location(5, 4), 1);
		testitem3.reward = 2; testitem3.weight = 3;
		pickups.add(testitem3);
		
		testJob = new Job(new Location(5, 5), pickups);
		
		testJobWorth = new JobWorth(testJob, testRobot, testLocation, Direction.NORTH);
	}
	
	@Test
	public void testGetRewardTime() {

		assertNotNull(testJobWorth.getRewardTime());
		assertEquals(testJobWorth.getRewardTime(), 10.2, 0.001);
	}
	
	@Test
	public void testGetMetric(){
		
		assertNotNull(testJobWorth.getMetric());
		assertEquals(testJobWorth.getMetric(), 10.2, 0.001);
	}
}
