package warehouse.jobselection;

import java.util.Collections;
import java.util.LinkedList;

import warehouse.job.Job;
import warehouse.jobselection.cancellation.CancellationMachine;
import warehouse.util.Direction;
import warehouse.util.EventDispatcher;
import warehouse.util.Location;
import warehouse.util.Robot;

/**
 * 
 * JOB SELECTION - (JobSelectorSingle class):
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
public class JobSelectorSingle extends Thread {

	private Robot robot;
	private boolean run;
	private LinkedList<Job> jobsClone;
	private Location robotStartLocation;
	private Direction robotFacing;

	private LinkedList<JobWorth> convertedList;
	private LinkedList<JobWorth> selectedList;
	
	private CancellationMachine cancellationMachine;
	private final int id;

	/**
	 * Create a job selector that chooses jobs for a single robot based on a
	 * list of available jobs.
	 * 
	 * @param robot
	 *            the robot
	 * @param jobs
	 *            the list of available jobs
	 */
	public JobSelectorSingle(int number, Robot robot, LinkedList<Job> jobs, CancellationMachine cancellationMachine) {

		EventDispatcher.subscribe2(this);

		// Set the fields
		this.robot = robot;
		this.robotStartLocation = this.robot.position;
		this.robotFacing = this.robot.rotation;

		this.cancellationMachine = cancellationMachine;
		
		this.jobsClone = new LinkedList<Job>();
		
		for(Job job : jobs){
			
			this.jobsClone.add(job);
		}
		
		this.id = number;
		
		// Start the thread
		System.out.println("\nSELECTOR THREAD " + this.id + ": starting thread");
		
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

		System.out.println("\nSELECTOR THREAD " + this.id + ": ready to run with " + this.jobsClone.size() + " jobs");
		
		while (run) {

			// convert it into a list of jobworths

			System.out.println("\nSELECTOR THREAD " + this.id + ": Converting list NOW");
			this.convertedList = this.convertList(this.robotStartLocation, this.robotFacing);

			assert(this.convertedList != null);

			System.out.println("\nSELECTOR THREAD " + this.id + ": Converted list, sending event...");

			// get the best one
			bestJob = this.selectBestJob(this.convertedList);

			System.out.println("\nSELECTOR THREAD " + this.id + ": The best Job is: " + bestJob);

			// remove it from the reference job list
			this.jobsClone.remove(bestJob.getJob());

			// add it to the list of selected jobs
			this.selectedList.add(bestJob);

			AddedToSelectedListEvent e = new AddedToSelectedListEvent();

			EventDispatcher.onEvent2(e);

			System.out.println("\nSELECTOR THREAD " + this.id + ": Adding : " + bestJob + " to the list");
			//System.out.println("\nSELECTOR THREAD " + this.id + ": Current list of selected jobs: " + this.selectedList);

			this.robotStartLocation = bestJob.getRoute().end;

			this.robotFacing = bestJob.getRoute().finalFacing;
			
            if(this.jobsClone.size() <= 0){

            	System.out.println("\nSELECTOR THREAD " + this.id + ": FINISHED ALL SELECTIONS -----> WAITING TO DIE x_x");
                FinishedSelectionEvent e2 = new FinishedSelectionEvent();
                EventDispatcher.onEvent2(e2);
                
                try {
					this.join();
				} catch (InterruptedException e1) {
					//Got interrupted for some reason
					e1.printStackTrace();
				}
            }
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
		for (Job job : this.jobsClone) {

			//System.out.println("\nSELECTOR THREAD " + this.id + ": Converting job: " + job);
			JobWorth jobworth = new JobWorth(job, this.robot, startLocation, startFacing);
			//System.out.println("\nSELECTOR THREAD " + this.id + ": Got jobWorth of: " + jobworth);

			double metric = jobworth.getMetric();
			double p = this.cancellationMachine.getProbability(jobworth.getJob());
			
			if(p >= 0.5){
				jobworth.setMetric(0.000001 * metric);
			}
			else{
				
				jobworth.setMetric((1 - p) * metric);
			}
				
			jobworths.add(jobworth);
		}

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

		return Collections.max(jobworths);
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
