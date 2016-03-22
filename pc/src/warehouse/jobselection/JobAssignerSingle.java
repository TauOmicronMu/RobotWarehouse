package warehouse.jobselection;

import java.util.LinkedList;

import warehouse.event.BeginAssigningEvent;
import warehouse.event.JobAssignedEvent;
import warehouse.event.JobCancellationEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.job.AssignedJob;
import warehouse.job.Job;
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

	private boolean gotList;
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
	 */
	public JobAssignerSingle(Robot robot, LinkedList<Job> trainingJobs) {

		EventDispatcher.subscribe2(this);

		// Set the variables and create the selector
		this.robot = robot;
		this.assignJobs = new LinkedList<>();
		this.readyToStart = false;

		this.jobCancelled = false;
		this.robotGotLost = false;

		this.jobComplete = true;

		this.gotList = false;

		try{

			this.cancellationMachine = new NaiveBayes(trainingJobs);
			System.out.println("\nASSIGNER THREAD: Made a Naive Bayes!");
		}
		catch(NullPointerException e){

			this.cancellationMachine = new Backup();
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

		System.out.println("\nASSIGNER THREAD: Starting Single Robot Assigner");

		this.run = true;

		JobWorth jobToBeAssigned;

		System.out.println("\nASSIGNER THREAD: Waiting for BeginAssigningEvent");

		while (this.run) {

			if (this.readyToStart) {

				System.out.println("\nASSIGNER THREAD: Received BeginAssigningEvent, sorting jobs");

				this.selector = new JobSelectorSingle(this.robot, this.jobs, this.cancellationMachine);

				System.out.println("\nASSIGNER THREAD: Created Single Robot Selector, assigning jobs");

				while (this.run) {

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// Sleep was interrupted for some reason
						e.printStackTrace();
					}

					// If the robot has completed a job and now has no assigned
					// job, give it a new one
					if (this.jobComplete || this.jobCancelled) {

						System.out.println("\nASSIGNER THREAD: Job was completed, assigning another");

						// If the robot is not going to start its next job from
						// the drop location as it got lost or the job was cancelled
						if (this.robotGotLost || this.jobCancelled) {

							System.out.println("\nASSIGNER THREAD: Robot got lost or job was cancelled");

							this.selector = new JobSelectorSingle(this.robot, this.jobs, this.cancellationMachine);

							this.gotList = false;

							System.out.println("\nASSIGNER THREAD: Created new Single Robot Selector, assigning jobs");

							this.robotGotLost = false;
							this.jobCancelled = false;

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}
						}

						if (gotList) {

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}

							//System.out.println("\nASSIGNER THREAD: Got Converted List");

							this.assignJobs = this.selector.getSelectedList();

							// Get the next job to be assigned
							jobToBeAssigned = this.assignJobs.removeFirst();

							//System.out.println("\nASSIGNER THREAD: The chosen best job is: " + jobToBeAssigned);

							// Remove it from the list of jobs
							this.jobs.remove(jobToBeAssigned.getJob());

							// Create a new assigned job and set it as current
							this.currentJob = this.assign(this.robot, jobToBeAssigned);
							EventDispatcher.onEvent2(new HasCurrentJobEvent());

							System.out.println("\nASSIGNER THREAD: The current job is: " + this.currentJob);

							// Tell subscribers
							JobAssignedEvent e = new JobAssignedEvent(this.currentJob);

							//System.out.println("\nASSIGNER THREAD: Dispatched JobAssignedEvent");

							EventDispatcher.onEvent2(e);

							this.jobComplete = false;

							//System.out.println("\nASSIGNER THREAD: Waiting for JobCompleteEvent");
						}

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// sleep was interrupted for some reason
							e.printStackTrace();
						}
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// sleep was interrupted for some reason
						e.printStackTrace();
					}

					if(this.assignJobs.size() <= 0){

						this.run = false;
					}

				}
			}
		}

		// If it reaches this point,
		// it has been told to stop.
		System.out.println("\nASSIGNER THREAD: Reached end of job list, or was told to stop - Finished");
	}

	/**
	 * Helper method to create a new assigned job for the robot.
	 * 
	 * @param robot
	 *            the robot
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

		this.jobs = new LinkedList<>(e.jobs);
		this.readyToStart = true;
	}

	@Subscriber
	public void onAddedToSelectedListEvent(AddedToSelectedListEvent e){

		this.gotList = true;
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

		System.out.println("\nASSIGNER THREAD: -------------------------------");
		System.out.println("\nASSIGNER THREAD: Job Complete, assigning new job");

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
