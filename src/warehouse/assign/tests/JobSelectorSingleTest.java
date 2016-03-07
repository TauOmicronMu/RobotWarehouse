package warehouse.assign.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import warehouse.job.AssignedJob;
import warehouse.job.Job;
import warehouse.select.JobSelectorSingle;
import warehouse.select.JobWorth;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Robot;

public class JobSelectorSingleTest {

	private JobSelectorSingle testSelector;
	private Job testJob1;
	private Job testJob2;
	private Job testJob3;
	private Robot testRobot;
	private Location testLocation;
	private LinkedList<JobWorth> jobworths;
	private JobWorth testJobWorth1;
	
	@Before
	public void setUp(){
		
		//make 3 jobs, with the 1st being the best
		
		//JOB 1
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
		
		testJob1 = new Job(new Location(5, 5), pickups);
		
		//JOB 2
		LinkedList<ItemPickup> pickups2 = new LinkedList<ItemPickup>();
		
		ItemPickup testitem4 = new ItemPickup("testitem4", new Location(0, 1), 1);
		testitem4.reward = 1; testitem4.weight = 1;
		pickups2.add(testitem4);
		
		ItemPickup testitem5 = new ItemPickup("testitem5", new Location(0, 3), 2);
		testitem5.reward = 0.5; testitem5.weight = 10;
		pickups2.add(testitem5);
		
		testJob2 = new Job(new Location(0, 5), pickups2);
		
		//JOB 3
		LinkedList<ItemPickup> pickups3 = new LinkedList<ItemPickup>();
		
		ItemPickup testitem6 = new ItemPickup("testitem6", new Location(2, 1), 40);
		testitem6.reward = 1; testitem6.weight = 1;
		pickups3.add(testitem6);
		
		testJob3 = new Job(new Location(2, 2), pickups3);
		
		testLocation = new Location(0, 0);
		testRobot = new Robot("testrobot", testLocation, Direction.NORTH);
		LinkedList<Job> jobs = new LinkedList<Job>();
		
		jobs.add(testJob1);
		jobs.add(testJob2);
		jobs.add(testJob3);
		
		testSelector = new JobSelectorSingle(testRobot, jobs);
		
		testJobWorth1 = new JobWorth(testJob1, testRobot, testLocation, Direction.NORTH);
		JobWorth testJobWorth2 = new JobWorth(testJob2, testRobot, testLocation, Direction.NORTH);
		JobWorth testJobWorth3 = new JobWorth(testJob3, testRobot, testLocation, Direction.NORTH);
		
		jobworths = new LinkedList<JobWorth>();
		
		jobworths.add(testJobWorth1);
		jobworths.add(testJobWorth2);
		jobworths.add(testJobWorth3);
	}
	
	@Test
	public void testConvertList() {
		
		assertNotNull(testSelector.convertList(testLocation, Direction.NORTH));
		assertEquals(testSelector.convertList(testLocation, Direction.NORTH), jobworths);
	}

	@Test
	public void testSelectBestJob() {
		
		assertNotNull(testSelector.selectBestJob(jobworths));
		assertEquals(testSelector.selectBestJob(jobworths), testJobWorth1);
		assertEquals(testSelector.selectBestJob(jobworths).getJob(), testJob1);
	}

	@Test
	public void testGetCurrentJob() {
		
		assertNotNull(testSelector.getCurrentJob());
		assertEquals(testSelector.getCurrentJob(), new AssignedJob(testJobWorth1.getJob(), testJobWorth1.getRoute(), testRobot));
	}

}
