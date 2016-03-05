package warehouse.assign.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import warehouse.assign.JobSelectorSingle;
import warehouse.assign.JobWorth;
import warehouse.job.AssignedJob;
import warehouse.job.Job;
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
	private JobWorth testJobWorth3;
	
	@Before
	public void setUp(){
		
		//TODO make 3 jobs, with the 3rd being the best
		testJob1 = new Job(null, null);
		testJob2 = new Job(null, null);
		testJob3 = new Job(null, null);
		
		testLocation = new Location(0, 0);
		testRobot = new Robot("testrobot", testLocation);
		LinkedList<Job> jobs = new LinkedList<Job>();
		
		jobs.add(testJob1);
		jobs.add(testJob2);
		jobs.add(testJob3);
		
		testSelector = new JobSelectorSingle(testRobot, jobs);
	}
	
	@Test
	public void testConvertList() {
		
		JobWorth testJobWorth1 = new JobWorth(testJob1, testRobot, testLocation);
		JobWorth testJobWorth2 = new JobWorth(testJob2, testRobot, testLocation);
		testJobWorth3 = new JobWorth(testJob3, testRobot, testLocation);
		
		jobworths = new LinkedList<JobWorth>();
		
		jobworths.add(testJobWorth1);
		jobworths.add(testJobWorth2);
		jobworths.add(testJobWorth3);
		
		assertEquals(testSelector.convertList(testLocation), jobworths);
	}

	@Test
	public void testSelectBestJob() {
		
		assertEquals(testSelector.selectBestJob(jobworths).getJob(), testJob3);
	}

	@Test
	public void testGetCurrentJob() {
		
		assertEquals(testSelector.getCurrentJob(), new AssignedJob(testJobWorth3.getJob(), testJobWorth3.getRoute(), testRobot));
	}

}
