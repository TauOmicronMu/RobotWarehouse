package warehouse.jobselection;

import warehouse.job.Job;
import warehouse.jobselection.cancellation.CancellationMachine;
import warehouse.util.Direction;
import warehouse.util.Location;
import warehouse.util.Robot;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * JOB SELECTION - (JobSelectorMultiple class):
 * 
 * Created by Owen on 28/02/2016
 * 
 * Preliminary class to: 
 * -Select the best job for a single robot 
 * -Base selection on the 'worth' of a job
 * 
 * @author Owen
 *
 */
public class JobSelectorMultiple extends Thread {

	private ConcurrentHashMap<String, Job> sharedJobs;
	private Robot robot;
	private boolean run;
	private LinkedList<Job> jobs;
	private Location robotStartLocation;
	private Direction robotFacing;

	private LinkedList<JobWorth> convertedList;
	private LinkedList<JobWorth> selectedList;

	private CancellationMachine cancellationMachine;

	/**
	 * Create a job selector that chooses jobs for a single robot based on a
	 * list of available jobs.
	 *
	 * @param robot
	 *            the robot
	 * @param jobs
	 *            the list of available jobs
	 */
	public JobSelectorMultiple(Robot robot, LinkedList<Job> jobs, ConcurrentHashMap<String, Job> sharedJobs,
							   CancellationMachine cancellationMachine) {

		// Set the fields
		this.robot = robot;
		this.robotStartLocation = this.robot.position;
		this.robotFacing = this.robot.rotation;

		this.cancellationMachine = cancellationMachine;
		
		this.jobs = jobs;

		this.sharedJobs = sharedJobs;

		// Start the thread
		this.start();
	}

	/**
	 * Method to run when the thread starts - continuously picks jobs for the
	 * robot.
	 */
	@Override
	public void run() {

		this.run = true;

		JobWorth bestJob;

		while (run && (this.jobs.size() > 0)) {

			// convert it into a list of jobworths
			this.convertedList = this.convertList(this.robotStartLocation, this.robotFacing);

			// get the best one
			bestJob = this.selectBestJob(this.convertedList);

			// remove it from the reference job list
			this.jobs.remove(bestJob.getJob());

			// add it to the list of selected jobs
			this.selectedList.add(bestJob);

			this.robotStartLocation = bestJob.getRoute().end;

			this.robotFacing = bestJob.getRoute().finalFacing;
		}
	}

	/**
	 * Convert the list of jobs (that this selector contains) into a list of jobworth objects based on the
	 * location given
	 * 
	 * @return the converted list
	 */
	public LinkedList<JobWorth> convertList(Location startLocation, Direction startFacing) {

		// make an empty list of jobworths
		LinkedList<JobWorth> jobworths = new LinkedList<JobWorth>();

		// Calculate the worth of each job and add them to the list
		this.jobs.stream().filter(job -> !this.sharedJobs.containsKey(job.id)).forEach(job -> {

			JobWorth jobworth = new JobWorth(job, this.robot, startLocation, startFacing);

			double metric = jobworth.getMetric();
			double p = 1 - this.cancellationMachine.getProbability(jobworth.getJob());

			jobworth.setMetric(p * metric);

			jobworths.add(jobworth);
		});

		return jobworths;
	}

	/**
	 * Get the best job worth from a given list
	 * 
	 * @param jobworths
	 *            the list of job worths
	 * @return the best one
	 */
	public JobWorth selectBestJob(LinkedList<JobWorth> jobworths) {

		JobWorth bestJob = Collections.max(jobworths);

		this.sharedJobs.put(bestJob.getJob().id, bestJob.getJob());

		return bestJob;
	}

	/**
	 * Method to allow the selector to be stopped before the list of jobs is
	 * empty.
	 */
	public void stopSelection() {

		this.run = false;
	}

	/**
	 * Method to get the current list of jobs to perform
	 * 
	 * @return the current list of selected jobs
	 */
	public LinkedList<JobWorth> getSelectedList() {

		return this.selectedList;
	}
}
