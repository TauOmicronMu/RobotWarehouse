package warehouse.jobselection;

import warehouse.event.BeginAssigningEvent;
import warehouse.event.JobAssignedEvent;
import warehouse.event.JobCancellationEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.job.AssignedJob;
import warehouse.job.Job;
import warehouse.jobselection.cancellation.Backup;
import warehouse.jobselection.cancellation.CancellationMachine;
import warehouse.jobselection.cancellation.NaiveBayes;
import warehouse.util.*;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * JOB SELECTION - (JobAssignerMultiple class):
 * 
 * Created by Owen on 07/03/2016
 * 
 * Preliminary class to: 
 * -Assign the best job in a list to a robot 
 * -Utilise the JobSelectorMultiple class to sort the jobs
 * 
 * @author Owen
 *
 */
public class JobAssignerMultiple extends Thread {

	private Robot robot1;
	private Robot robot2;
	private Robot robot3;

	private JobSelectorMultiple selector1;
	private JobSelectorMultiple selector2;
	private JobSelectorMultiple selector3;

	private boolean run;
	private boolean readyToStart;

	private boolean jobComplete1;
	private boolean jobComplete2;
	private boolean jobComplete3;

	private boolean robotGotLost1;
	private boolean robotGotLost2;
	private boolean robotGotLost3;

	private boolean jobCancelled1;
	private boolean jobCancelled2;
	private boolean jobCancelled3;

	private LinkedList<Job> jobs;

	private LinkedList<JobWorth> assignJobs1;
	private LinkedList<JobWorth> assignJobs2;
	private LinkedList<JobWorth> assignJobs3;

	private AssignedJob currentJob1;
	private AssignedJob currentJob2;
	private AssignedJob currentJob3;

	private CancellationMachine cancellationMachine;

	/**
	 * Create a new Job Assigner for a single robot based on a list of jobs
	 *
	 * @param robot
	 *            the robot
	 * @param jobs
	 *            the list of jobs
	 */
	public JobAssignerMultiple(Robot robot1, Robot robot2, Robot robot3, LinkedList<Job> jobs) {

		// Set the variables and create the selector
		this.robot1 = robot1;
		this.robot2 = robot2;
		this.robot3 = robot3;
		this.jobs = jobs;

		this.readyToStart = false;

		this.jobComplete1 = false;
		this.jobComplete2 = false;
		this.jobComplete3 = false;

		this.jobCancelled1 = false;
		this.jobCancelled2 = false;
		this.jobCancelled3 = false;

		this.robotGotLost1 = false;
		this.robotGotLost2 = false;
		this.robotGotLost3 = false;

		this.currentJob1 = null;
		this.currentJob2 = null;
		this.currentJob3 = null;

		try{
			
			this.cancellationMachine = new NaiveBayes(jobs);
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

		this.run = true;

		EventDispatcher.subscribe2(this);

		JobWorth jobToBeAssigned;

		while (this.run) {

			if (this.readyToStart) {

				ConcurrentHashMap<String, Job> sharedJobs = new ConcurrentHashMap<>();

				this.selector1 = new JobSelectorMultiple(this.robot1, this.jobs, sharedJobs, this.cancellationMachine);
				this.selector2 = new JobSelectorMultiple(this.robot2, this.jobs, sharedJobs, this.cancellationMachine);
				this.selector3 = new JobSelectorMultiple(this.robot3, this.jobs, sharedJobs,  this.cancellationMachine);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// Sleep was interrupted for some reason
					e.printStackTrace();
				}

				while (this.run && (this.jobs.size() > 0)) {

					//ROBOT 1

					// If the robot has completed a job and now has no assigned
					// job, give it a new one
					if (this.jobComplete1 || this.jobCancelled1) {

						// If the robot is not going to start its next job from
						// the drop location as it got lost or the job was cancelled
						if (this.robotGotLost1 || this.jobCancelled1) {

							this.selector1 = new JobSelectorMultiple(this.robot1, this.jobs, sharedJobs, this.cancellationMachine);
							this.robotGotLost1 = false;
							this.jobCancelled1 = false;

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}
						}

						this.assignJobs1 = this.selector1.getSelectedList();

						// Get the next job to be assigned
						jobToBeAssigned = this.assignJobs1.removeFirst();

						// Remove it from the list of jobs
						this.jobs.remove(jobToBeAssigned.getJob());

						// Create a new assigned job and set it as current
						this.currentJob1 = this.assign(this.robot1, jobToBeAssigned);

						// Tell subscribers
						JobAssignedEvent e = new JobAssignedEvent(this.currentJob1, this.robot1);
						EventDispatcher.onEvent2(e);

						this.jobComplete1 = false;
					}

					//ROBOT 2

					// If the robot has completed a job and now has no assigned
					// job, give it a new one
					if (this.jobComplete2 || this.jobCancelled2) {

						// If the robot is not going to start its next job from
						// the drop location as it got lost or the job was cancelled
						if (this.robotGotLost2 || this.jobCancelled2) {

							this.selector2 = new JobSelectorMultiple(this.robot2, this.jobs, sharedJobs, this.cancellationMachine);
							this.robotGotLost2 = false;
							this.jobCancelled2 = false;

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}
						}

						this.assignJobs2 = this.selector2.getSelectedList();

						// Get the next job to be assigned
						jobToBeAssigned = this.assignJobs2.removeFirst();

						// Remove it from the list of jobs
						this.jobs.remove(jobToBeAssigned.getJob());

						// Create a new assigned job and set it as current
						this.currentJob2 = this.assign(this.robot2, jobToBeAssigned);

						// Tell subscribers
						JobAssignedEvent e = new JobAssignedEvent(this.currentJob2, this.robot2);
						EventDispatcher.onEvent2(e);

						this.jobComplete2 = false;
					}

					//ROBOT 3

					// If the robot has completed a job and now has no assigned
					// job, give it a new one
					if (this.jobComplete3 || this.jobCancelled3) {

						// If the robot is not going to start its next job from
						// the drop location as it got lost or the job was cancelled
						if (this.robotGotLost3 || this.jobCancelled3) {

							this.selector3 = new JobSelectorMultiple(this.robot3, this.jobs, sharedJobs, this.cancellationMachine);
							this.robotGotLost3 = false;
							this.jobCancelled3 = false;

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}
						}

						this.assignJobs3 = this.selector3.getSelectedList();

						// Get the next job to be assigned
						jobToBeAssigned = this.assignJobs3.removeFirst();

						// Remove it from the list of jobs
						this.jobs.remove(jobToBeAssigned.getJob());

						// Create a new assigned job and set it as current
						this.currentJob3 = this.assign(this.robot3, jobToBeAssigned);

						// Tell subscribers
						JobAssignedEvent e = new JobAssignedEvent(this.currentJob3, this.robot3);
						EventDispatcher.onEvent2(e);

						this.jobComplete3 = false;
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

		Route improvedRoute = null;

		//Route improvedRoute = robot.equals(this.robot1) ? TODO.getImprovedRoute(robot, jobWorth.getJob(),
		// 													this.currentJob2, this.currentJob3) :
		// 					    (robot.equals(this.robot2) ? TODO.getImprovedRoute(robot, jobWorth.getJob(),
		// 													this.currentJob1, this.currentJob3 :
		// 						(robot.equals(this.robot3) ? TODO.getImprovedRoute(robot, JobWorth.getJob(),
		// 													this.currentJob1, this.currentJob2));

		return new AssignedJob(jobWorth.getJob(), improvedRoute, robot);
	}

	/**
	 * Method to get the current assigned job the selector has chosen
	 * 
	 * @return the current assigned job
	 */
	public AssignedJob getCurrentJob1() {

		return this.currentJob1;
	}

	public AssignedJob getCurrentJob2(){

		return this.currentJob2;
	}

	public AssignedJob getCurrentJob3(){

		return this.currentJob3;
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
	public void onRobotGotLostEvent(RobotGotLostEvent e) {

		if (e.robot.equals(this.robot1)) {

			this.robotGotLost1 = true;
		} else if(e.robot.equals(this.robot2)){

			this.robotGotLost2 = true;
		} else if(e.robot.equals(this.robot3)){

			this.robotGotLost3 = true;
		}
	}

	/**
	 * Listen for when the robot has completed its job
	 * 
	 * @param e
	 *            the job complete event
	 */
	@Subscriber
	public void onJobCompleteevent(JobCompleteEvent e) {

		if(e.robot.equals(this.robot1)) {

			this.jobComplete1 = true;
		} else if(e.robot.equals(this.robot2)){

			this.jobComplete2 = true;
		} else if(e.robot.equals(this.robot3)){

			this.jobComplete3 = true;
		}
	}

	/**
	 * Listen for when the robot's job was cancelled
	 * 
	 * @param e
	 *            the job cancel event
	 */
	@Subscriber
	public void onJobCancellationEvent(JobCancellationEvent e) {

		if(e.robot.equals(this.robot1)) {

			this.jobCancelled1 = true;
		} else if(e.robot.equals(this.robot2)){

			this.jobCancelled2 = true;
		} else if(e.robot.equals(this.robot3)){

			this.jobCancelled3 = true;
		}
	}

	/**
	 * Method to stop thread assigning jobs
	 */
	public void stopAssigning() {

		this.run = false;
	}
}
