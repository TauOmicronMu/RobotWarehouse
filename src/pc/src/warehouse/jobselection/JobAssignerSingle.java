package warehouse.jobselection;

import java.util.LinkedList;

import warehouse.event.BeginAssigningEvent;
import warehouse.event.JobAssignedEvent;
import warehouse.event.JobCancellationEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.job.AssignedJob;
import warehouse.job.Job;
import warehouse.jobselection.JobSelectorSingle;
import warehouse.jobselection.JobWorth;
import warehouse.jobselection.cancellation.Backup;
import warehouse.jobselection.cancellation.CancellationMachine;
import warehouse.jobselection.cancellation.NaiveBayes;
import warehouse.util.EventDispatcher;
import warehouse.util.Location;
import warehouse.util.Robot;
import warehouse.util.Subscriber;

/**
 * 
 * JOB SELECTION - (JobAssignerSingle class):
 * 
 * Created by Owen on 07/03/2016
 * 
 * Preliminary class to: 
 * -Assign the best job in a list to a robot 
 * -Utilise the JobSelectorSingle class to sort the jobs
 * 
 * @author Owen
 *
 */
public class JobAssignerSingle extends Thread {

	private Robot robot;
	private JobSelectorSingle selector;
	private boolean run;
	private boolean readyToStart;
	private boolean jobComplete;
	private boolean robotGotLost;
	private LinkedList<Job> jobs;
	private LinkedList<JobWorth> assignJobs;
	private AssignedJob currentJob;
	private boolean jobCancelled;
	
	private CancellationMachine cancellationMachine;

	/**
	 * Create a new Job Assigner for a single robot based on a list of jobs
	 * 
	 * @param robot
	 *            the robot
	 * @param jobs
	 *            the list of jobs
	 */
	public JobAssignerSingle(Robot robot, LinkedList<Job> jobs) {

		// Set the variables and create the selector
		this.robot = robot;
		this.jobs = jobs;
		this.readyToStart = false;
		this.jobComplete = false;
		this.jobCancelled = false;
		this.robotGotLost = false;

		try{
			
			this.cancellationMachine = new NaiveBayes(jobs);
		}
		catch(AssertionError e){
			
			this.cancellationMachine = new Backup();
		}
			
			
		// Begin the thread
		this.start();
	}

	/**
	 * What to do when the thread is started
	 */
	@Override
	public void run() {

		this.run = true;

		// Sign up to the event dispatcher
		EventDispatcher dispatcher = EventDispatcher.INSTANCE;

		EventDispatcher.subscribe2(this);

		JobWorth jobToBeAssigned;

		while (this.run) {

			if (this.readyToStart) {

				this.selector = new JobSelectorSingle(this.robot, this.jobs, this.cancellationMachine);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// Sleep was interrupted for some reason
					e.printStackTrace();
				}

				while (this.run && (this.jobs.size() > 0)) {

					// If the robot has completed a job and now has no assigned
					// job, give it a new one
					if (this.jobComplete || this.jobCancelled) {

						// If the robot is not going to start its next job from
						// the drop location as it got lost or the job was cancelled
						if (this.robotGotLost || this.jobCancelled) {

							this.selector = new JobSelectorSingle(this.robot, this.jobs, this.cancellationMachine);
							this.robotGotLost = false;
							this.jobCancelled = false;

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}
						}

						this.assignJobs = this.selector.getSelectedList();

						// Get the next job to be assigned
						jobToBeAssigned = this.assignJobs.removeFirst();

						// Remove it from the list of jobs
						this.jobs.remove(jobToBeAssigned.getJob());

						// Create a new assigned job and set it as current
						this.currentJob = this.assign(this.robot, jobToBeAssigned);

						// Tell subscribers
						JobAssignedEvent e = new JobAssignedEvent(this.currentJob, this.robot);
						dispatcher.onEvent(e);

						this.jobComplete = false;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// sleep was interrupted for some reason
						e.printStackTrace();
					}
				}

				// If it reaches this point, the jobs have all been exhausted or
				// it has been told to stop.

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// sleep was interrupted for some reason
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Helper method to create a new assigned job for the robot.
	 * 
	 * @param robot
	 *            the robot
	 * @param job
	 *            the job to be assigned
	 * @return an AssignedJob object
	 */
	private AssignedJob assign(Robot robot, JobWorth jobWorth) {

		return new AssignedJob(jobWorth.getJob(), jobWorth.getRoute(), robot);
	}

	/**
	 * Method to get the current assigned job the selector has chosen
	 * 
	 * @return the current assigned job
	 */
	public AssignedJob getCurrentJob() {

		return this.currentJob;
	}

	/**
	 * Listen for when we are told to start assigning jobs.
	 * 
	 * @param e
	 *            the begin assigning event
	 */
	@Subscriber
	public void onBeginAssigningEvent(BeginAssigningEvent e) {

		this.readyToStart = true;
	}

	/**
	 * Listen for if the robot gets lost.
	 * 
	 * @param l
	 *            the new location
	 */
	@Subscriber
	public void onRobotGotLostEvent(Location l) {

		this.robotGotLost = true;
	}

	/**
	 * Listen for when the robot has completed its job
	 * 
	 * @param e
	 *            the job complete event
	 */
	@Subscriber
	public void onJobCompleteevent(JobCompleteEvent e) {

		this.jobComplete = true;
	}

	/**
	 * Listen for when the robot's job was cancelled
	 * 
	 * @param e
	 *            the job cancel event
	 */
	@Subscriber
	public void onJobCancelllationEvent(JobCancellationEvent e) {

		this.jobCancelled = true;
	}

	/**
	 * Method to stop thread assigning jobs
	 */
	public void stopAssigning() {

		this.run = false;
	}
}
