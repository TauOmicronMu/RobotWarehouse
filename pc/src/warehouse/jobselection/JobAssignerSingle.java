package warehouse.jobselection;

import java.util.LinkedList;

import warehouse.event.BeginAssigningEvent;
import warehouse.event.JobAssignedEvent;
import warehouse.event.JobCancellationEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.event.RobotLostEvent;
import warehouse.job.AssignedJob;
import warehouse.job.Job;
import warehouse.jobselection.cancellation.Backup;
import warehouse.jobselection.cancellation.CancellationMachine;
import warehouse.jobselection.cancellation.NaiveBayes;
import warehouse.jobselection.event.AddedToSelectedListEvent;
import warehouse.jobselection.event.FinishedAssigningEvent;
import warehouse.jobselection.event.FinishedSelectionEvent;
import warehouse.jobselection.event.SelectorHasCurrentJobEvent;
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;
import warehouse.util.Robot;
import warehouse.util.Subscriber;

/**
 * 
 * JOB SELECTION - (JobAssignerSingle class):
 * 
 * Created by Owen on 07/03/2016
 * 
 * Class to: 
 * Assign the best job in a list to a single robot,
 * Utilise the JobSelectorSingle class to sort the jobs
 * 
 * @author Owen
 *
 */
public class JobAssignerSingle extends Thread {

	private boolean assignedJobsFinished;
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
	private double totalReward;
	
	private LinkedList<AssignedJob> finalList;
	private int cutOffPoint;

	/**
	 * Create a new Job Assigner for a single robot based on a list of jobs
	 * 
	 * @param robot
	 *            the robot
	 * @param trainingJobs the list of training jobs for the cancellation machine to use
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
		this.assignedJobsFinished = false;

		//Create a cancellation machine based on the training data
		try{

			this.cancellationMachine = new NaiveBayes(trainingJobs);
		}
		catch(NullPointerException e){

			this.cancellationMachine = new Backup();
		}
		catch(AssertionError e){

			this.cancellationMachine = new Backup();
		}
		
		System.out.println(this.cancellationMachine);
			
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

		int selectorNumber = 0;
		int jobNumber = 0;
		
		this.finalList = new LinkedList<>();
		this.cutOffPoint = 0;	
		
		boolean firstCancelled = true;
		
		System.out.println("\nASSIGNER THREAD: Waiting for BeginAssigningEvent");

		while (this.run) {

			if (this.readyToStart) {

				System.out.println("\nASSIGNER THREAD: Received BeginAssigningEvent, sorting jobs");

				this.selector = new JobSelectorSingle(selectorNumber, this.robot, this.jobs, this.cancellationMachine);

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
						
						// If the robot is not going to start its next job from
						// the drop location as it got lost or the job was cancelled
						if (this.robotGotLost || this.jobCancelled) {

							if(firstCancelled){
							
								System.out.println("\nASSIGNER THREAD: Setting cut off point to " + jobNumber);
								this.cutOffPoint = jobNumber;
								firstCancelled = false;
							}
							
							System.out.println("\nASSIGNER THREAD: Robot got lost or job was cancelled");
							
							this.selector.stopSelection();
							
							selectorNumber++;
							this.selector = new JobSelectorSingle(selectorNumber, this.robot, this.jobs, this.cancellationMachine);

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

							jobNumber++;
							System.out.println("\nASSIGNER THREAD: " + jobNumber);
							
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}

							System.out.println("\nASSIGNER THREAD: Got Converted List");

							this.assignJobs = this.selector.getSelectedList();

							assert(this.assignJobs.size() > 0);
							
							// Get the next job to be assigned
							jobToBeAssigned = this.assignJobs.removeFirst();

							System.out.println("\nASSIGNER THREAD: The chosen best job is ID " + jobToBeAssigned.getJob().id + ": " + jobToBeAssigned);

							// Remove it from the list of jobs
							this.jobs.remove(jobToBeAssigned.getJob());

							System.out.println("\nASSIGNER THREAD: The list of jobs now has " + this.jobs.size() + " elements");
							
							// Create a new assigned job and set it as current
							this.currentJob = this.assign(this.robot, jobToBeAssigned);
							EventDispatcher.onEvent2(new SelectorHasCurrentJobEvent());

							this.finalList.add(this.currentJob);
							
							System.out.println("\nASSIGNER THREAD: The current job is: " + this.currentJob);

							// Tell subscribers
							JobAssignedEvent e = new JobAssignedEvent(this.currentJob);

							System.out.println("\nASSIGNER THREAD: Dispatched JobAssignedEvent");

							EventDispatcher.onEvent2(e);

							this.jobComplete = false;

							System.out.println("\nASSIGNER THREAD: Waiting for JobCompleteEvent");
						}

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// sleep was interrupted for some reason
							e.printStackTrace();
						}

						if((assignedJobsFinished) && (this.assignJobs.size() <= 0)){

							this.run = false;
						}
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// sleep was interrupted for some reason
						e.printStackTrace();
					}
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// sleep was interrupted for some reason
				e.printStackTrace();
			}
		}

		// If it reaches this point, it has assigned every job
		// it has been told to stop.
		
		//DEBUG
		System.out.println("\nASSIGNER THREAD: Reached end of job list, or was told to stop ----> WAITING TO DIE X_X");
		
		EventDispatcher.onEvent2(new FinishedAssigningEvent());
		
		System.out.println("Total Reward = " + this.totalReward);
	}

	/**
	 * Helper method to create a new assigned job for the robot.
	 * 
	 * @param robot
	 *            the robot
	 * @param jobWorth the jobWorth of this job
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
	 * Method to get the current cancellation machine being used by the assigner
	 * 
	 * @return the current cancellation machine
	 */
	public CancellationMachine getCancellationMachine(){
		
		return this.cancellationMachine;
	}
	
	/**
	 * Method to get the final list of assigned jobs that this assigner generated before it stopped
	 * 
	 * @return the final linked list of assigned jobs
	 */
	public LinkedList<AssignedJob> getFinalList(){
		
		return this.finalList;
	}
	
	/**
	 * Get the index of the first cancelled job in the final list.
	 *
	 * @return the index of the first cancelled job in the final list
	 */
	public int getCutOffPoint(){
		
		return this.cutOffPoint;
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

	/**
	 * Listen for when we are told to start taking jobs from the selector's list
	 * 
	 * @param e the added to selected list event
	 */
	@Subscriber
	public void onAddedToSelectedListEvent(AddedToSelectedListEvent e){

		this.gotList = true;
	}

	/**
	 * Listen for when we the selector has finished selecting jobs
	 * 
	 * @param e the finished selection event
	 */
	@Subscriber
	public void onFinishedSelectionEvent(FinishedSelectionEvent e){

		this.assignedJobsFinished = true;
	}

	/**
	 * Listen for if the robot gets lost.
	 * 
	 * @param e the robot lost event
	 */
	@Subscriber
	public void onRobotLostEvent(RobotLostEvent e) {

		this.robotGotLost = true;
	}

	/**
	 * Listen for when the robot has completed its job
	 * 
	 * @param e
	 *            the job complete event
	 */
	@Subscriber
	public void onJobCompleteEvent(JobCompleteEvent e) {

		double reward = 0;
		
		for(ItemPickup pickup : e.job.pickups){
			
			reward += pickup.reward * pickup.itemCount;
		}
		
		this.totalReward += reward;
		
		//DEBUG
		System.out.println("\nASSIGNER THREAD: JOB COMPLETED ----> REWARD OF " + reward + " ACQUIRED. TOTAL REWARD = " + this.totalReward);
		//System.out.println("\nASSIGNER THREAD: Job Complete, assigning new job");

		this.jobComplete = true;
	}

	/**
	 * Listen for when the robot's job was cancelled
	 * 
	 * @param e
	 *            the job cancellation event
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
