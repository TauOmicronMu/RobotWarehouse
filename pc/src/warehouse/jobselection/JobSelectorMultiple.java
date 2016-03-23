package warehouse.jobselection;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import warehouse.job.Job;
import warehouse.jobselection.cancellation.CancellationMachine;
import warehouse.jobselection.event.AddedToSelectedListEvent;
import warehouse.jobselection.event.FinishedSelectionEvent;
import warehouse.util.Direction;
import warehouse.util.EventDispatcher;
import warehouse.util.Location;
import warehouse.util.Robot;

/**
 * 
 * JOB SELECTION - (JobSelectorMultiple class):
 * 
 * Created by Owen on 28/02/2016
 * 
 * Class to: Select the best job for multiple robots, Base selection
 * on the 'worth' of a job
 * 
 * @author Owen
 *
 */
public class JobSelectorMultiple extends Thread {

	private Robot robot;
	private boolean run;
	private LinkedList<Job> jobsClone;
	private Location robotStartLocation;
	private Direction robotFacing;

	private LinkedList<JobWorth> convertedList;
	private LinkedList<JobWorth> selectedList;

	private CancellationMachine cancellationMachine;
	private final int id;
	private ConcurrentLinkedQueue<Job> checkList;

	/**
	 * Create a job selector that chooses jobs for a single robot based on a
	 * list of available jobs.
	 * 
	 * @param number
	 *            the id of this selector thread
	 * @param robot
	 *            the robot
	 * @param jobs
	 *            the list of available jobs
	 * @param cancellationMachine
	 *            the cancellation machine that this selector can use for its
	 *            probabilities
	 * @param checList the shared list to check if other selectors have selected a job or not
	 */
	public JobSelectorMultiple(int number, Robot robot, LinkedList<Job> jobs, CancellationMachine cancellationMachine, ConcurrentLinkedQueue<Job> checkList) {

		EventDispatcher.subscribe2(this);

		this.checkList = checkList;
		
		// Set the fields
		this.robot = robot;
		this.robotStartLocation = this.robot.position;
		this.robotFacing = this.robot.rotation;

		this.cancellationMachine = cancellationMachine;

		// Get a copy of the list of available jobs
		this.jobsClone = new LinkedList<Job>();

		for (Job job : jobs) {

			this.jobsClone.add(job);
		}

		// Set the id of the thread for debugging
		this.id = number;

		// start the selector thread
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
		this.selectedList = new LinkedList<>();

		runLoop: while (run) {

			// convert it into a list of jobworths

			// If the entire list has been selected, stop
			if (this.jobsClone.size() <= 0) {

				FinishedSelectionEvent e2 = new FinishedSelectionEvent();
				EventDispatcher.onEvent2(e2);
				break runLoop;
			}

			// Work out how good each job is by creating a jobworth object for
			// each
			this.convertedList = this.convertList(this.robotStartLocation, this.robotFacing);

			assert (this.convertedList != null);

			// get the best one
			bestJob = this.selectBestJob(this.convertedList);

			this.checkList.add(bestJob.getJob());
			
			// remove it from the reference job list
			this.jobsClone.remove(bestJob.getJob());

			// add it to the list of selected jobs
			this.selectedList.add(bestJob);

			// Dispatch an event to let subscribers know we have added an item
			// to the list
			AddedToSelectedListEvent e = new AddedToSelectedListEvent();

			EventDispatcher.onEvent2(e);

			// update the start location and facing to be that which it will be
			// once this route ends
			this.robotStartLocation = bestJob.getRoute().end;

			this.robotFacing = bestJob.getRoute().finalFacing;
		}
	}

	/**
	 * Convert the list of jobs (that this selector contains) into a list of
	 * jobworth objects based on the location given
	 * 
	 * @param startLocation
	 *            the start location to use for each job
	 * @param startFacing
	 *            the start facing to use for each job
	 * @return the converted list of jobs
	 */
	public LinkedList<JobWorth> convertList(Location startLocation, Direction startFacing) {

		// check that there are still jobs to convert
		assert (this.jobsClone.size() > 0);

		// make an empty list of jobworths
		LinkedList<JobWorth> jobworths = new LinkedList<JobWorth>();

		// Calculate the worth of each job and add them to the list
		for (Job job : this.jobsClone) {

			// System.out.println("\nSELECTOR THREAD " + this.id + ": Converting
			// job: " + job);
			JobWorth jobworth = new JobWorth(job, this.robot, startLocation, startFacing);
			// System.out.println("\nSELECTOR THREAD " + this.id + ": Got
			// jobWorth of: " + jobworth);

			// If the chance of a job being cancelled is 50% or above, make it
			// practically unpickable. If it is below that, incorporate the
			// probability of it being cancelled into the worth of a job
			double metric = jobworth.getMetric();
			double p = this.cancellationMachine.getProbability(jobworth.getJob());

			if (p >= 0.5) {
				jobworth.setMetric(0.000001 * metric);
			} else {

				jobworth.setMetric((1 - p) * metric);
			}

			// add the jobworth object to the list to be returned
			jobworths.add(jobworth);
		}

		return jobworths;
	}

	/**
	 * Get the best job worth from a given list
	 * 
	 * @param jobworths
	 *            the list of job worths
	 * @return the best one in the list
	 */
	public JobWorth selectBestJob(LinkedList<JobWorth> jobworths) {

		// check that there are elements in the list
		assert (jobworths.size() > 0);
		
		LinkedList<JobWorth> jobworthsclone = new LinkedList<JobWorth>();
		
		for(JobWorth jobworth : jobworths){
			
			jobworthsclone.add(jobworth);
		}
		
		JobWorth best = Collections.max(jobworthsclone);
		
		if(this.checkList.contains(best)){
			
			jobworthsclone.remove(best);
			best = Collections.max(jobworthsclone);
		}
		
		return best;
	}

	/**
	 * Method to allow the selector to be stopped before the list of jobs is
	 * empty
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
