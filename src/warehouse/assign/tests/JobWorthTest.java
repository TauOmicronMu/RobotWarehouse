package warehouse.assign.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import warehouse.assign.JobWorth;
import warehouse.job.Job;
import warehouse.util.Location;
import warehouse.util.Robot;
import warehouse.util.Route;

public class JobWorthTest {

	private JobWorth testJobWorth;
	private Job testJob;
	private Location testLocation;
	private Robot testRobot;
	
	@Before
	public void setUp(){
		
		testLocation = new Location(0, 0);
		testRobot = new Robot("testrobot", testLocation);
		testJob = new Job(testLocation, null);
		
		testJobWorth = new JobWorth(testJob, testRobot, testLocation);
	}
	
	@Test
	public void testGetRoute() {
		
		//TODO work out what the correct route is
		assertEquals(testJobWorth.getRoute(), new Route(null, null, null));
	}

	@Test
	public void testGetRewardTime() {

		//TODO work out actual reward per timestep
		assert(testJobWorth.getRewardTime() == 0);
	}

	@Test
	public void testGetRewardWeight() {
		
		//TODO work out actual reward per weight
		assert(testJobWorth.getRewardWeight() == 0);
	}
}
