package warehouse.jobselection;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

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
import warehouse.util.Robot;
import warehouse.util.Route;
import warehouse.util.Subscriber;

/**
 * 
 * JOB SELECTION - (JobAssignerMultiple class):
 * 
 * Created by Owen on 07/03/2016
 * 
 * Class to: 
 * Assign the best job in a list to multiple robot,
 * Utilise the JobSelectorMultiple class to sort the jobs
 * 
 * @author Owen
 *
 */
public class JobAssignerMultiple extends Thread {

	private boolean assignedJobsFinished1, assignedJobsFinished2, assignedJobsFinished3;
	
	private boolean gotList1, gotList2, gotList3;
	
	private Robot robot1, robot2, robot3;
	
	private JobSelectorMultiple selector1, selector2, selector3;
	
	private boolean run;
	
	private boolean readyToStart1, readyToStart2, readyToStart3;
	
	private boolean jobComplete1, jobComplete2, jobComplete3;
	
	private boolean robotGotLost1, robotGotLost2, robotGotLost3;
	
	private LinkedList<Job> jobs;
	
	private LinkedList<JobWorth> assignJobs1, assignJobs2, assignJobs3;
	
	private AssignedJob currentJob1, currentJob2, currentJob3;
	
	private boolean jobCancelled1, jobCancelled2, jobCancelled3;
	
	private CancellationMachine cancellationMachine;
	
	private ConcurrentLinkedQueue<Job> checkList;

	/**
	 * Create a new Job Assigner for multiple robot based on a list of jobs
	 * 
	 * @param robot1
	 *            the first robot
	 * @param robot2 the second robot
	 * @param robot3 the third robot
	 * @param trainingJobs the list of training jobs for the cancellation machine to use
	 */
	public JobAssignerMultiple(Robot robot1, Robot robot2, Robot robot3, LinkedList<Job> trainingJobs){

		EventDispatcher.subscribe2(this);

		this.checkList = new ConcurrentLinkedQueue<Job>();
		
		// Set the variables and create the selector
		this.robot1 = robot1;
		this.robot2 = robot2;
		this.robot3 = robot3;
		
		this.assignJobs1 = new LinkedList<>();
		this.assignJobs2 = new LinkedList<>();
		this.assignJobs3 = new LinkedList<>();
		
		this.readyToStart1 = false;
		this.readyToStart2 = false;
		this.readyToStart3 = false;

		this.jobCancelled1 = false;
		this.jobCancelled2 = false;
		this.jobCancelled3 = false;
		
		this.robotGotLost1 = false;
		this.robotGotLost2 = false;
		this.robotGotLost3 = false;

		this.jobComplete1 = true;
		this.jobComplete2 = true;
		this.jobComplete3 = true;

		this.gotList1 = false;
		this.gotList2 = false;
		this.gotList3 = false;
		
		this.assignedJobsFinished1 = false;
		this.assignedJobsFinished2 = false;
		this.assignedJobsFinished3 = false;

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

		this.run = true;

		JobWorth jobToBeAssigned;

		int number = 0;
		
		while (this.run) {

			//ROBOT 1
			
			if (this.readyToStart1) {

				this.selector1 = new JobSelectorMultiple(number, this.robot1, this.jobs, this.cancellationMachine, this.checkList);

				while (this.run) {

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// Sleep was interrupted for some reason
						e.printStackTrace();
					}

					// If the robot has completed a job and now has no assigned
					// job, give it a new one
					if (this.jobComplete1 || this.jobCancelled1) {

						// If the robot is not going to start its next job from
						// the drop location as it got lost or the job was cancelled
						if (this.robotGotLost1 || this.jobCancelled1) {

							number++;
							this.selector1 = new JobSelectorMultiple(number, this.robot1, this.jobs, this.cancellationMachine, this.checkList);

							this.gotList1 = false;

							this.robotGotLost1 = false;
							this.jobCancelled1 = false;

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}
						}

						if (gotList1) {

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}
							
							this.assignJobs1 = this.selector1.getSelectedList();

							// Get the next job to be assigned
							jobToBeAssigned = this.assignJobs1.removeFirst();

							// Remove it from the list of jobs
							this.jobs.remove(jobToBeAssigned.getJob());

							// Create a new assigned job and set it as current
							this.currentJob1 = this.assign(this.robot1, jobToBeAssigned);
							EventDispatcher.onEvent2(new SelectorHasCurrentJobEvent());

							// Tell subscribers
							JobAssignedEvent e = new JobAssignedEvent(this.currentJob1);

							EventDispatcher.onEvent2(e);

							this.jobComplete1 = false;
						}

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// sleep was interrupted for some reason
							e.printStackTrace();
						}

						if((assignedJobsFinished1) && (this.assignJobs1.size() <= 0)){

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
			
			//ROBOT 2
			
			if (this.readyToStart2) {

				this.selector2 = new JobSelectorMultiple(number, this.robot2, this.jobs, this.cancellationMachine, this.checkList);

				while (this.run) {

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// Sleep was interrupted for some reason
						e.printStackTrace();
					}

					// If the robot has completed a job and now has no assigned
					// job, give it a new one
					if (this.jobComplete2 || this.jobCancelled2) {

						// If the robot is not going to start its next job from
						// the drop location as it got lost or the job was cancelled
						if (this.robotGotLost2 || this.jobCancelled2) {

							number++;
							this.selector2 = new JobSelectorMultiple(number, this.robot1, this.jobs, this.cancellationMachine, this.checkList);

							this.gotList2 = false;

							this.robotGotLost2 = false;
							this.jobCancelled2 = false;

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}
						}

						if (gotList2) {

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}
							
							this.assignJobs2 = this.selector2.getSelectedList();

							// Get the next job to be assigned
							jobToBeAssigned = this.assignJobs2.removeFirst();

							// Remove it from the list of jobs
							this.jobs.remove(jobToBeAssigned.getJob());

							// Create a new assigned job and set it as current
							this.currentJob2 = this.assign(this.robot2, jobToBeAssigned);
							EventDispatcher.onEvent2(new SelectorHasCurrentJobEvent());

							// Tell subscribers
							JobAssignedEvent e = new JobAssignedEvent(this.currentJob2);

							EventDispatcher.onEvent2(e);

							this.jobComplete2 = false;
						}

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// sleep was interrupted for some reason
							e.printStackTrace();
						}

						if((assignedJobsFinished2) && (this.assignJobs2.size() <= 0)){

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
			
			//ROBOT 3
			
			if (this.readyToStart3) {

				this.selector3 = new JobSelectorMultiple(number, this.robot3, this.jobs, this.cancellationMachine, this.checkList);

				while (this.run) {

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// Sleep was interrupted for some reason
						e.printStackTrace();
					}

					// If the robot has completed a job and now has no assigned
					// job, give it a new one
					if (this.jobComplete3 || this.jobCancelled3) {

						// If the robot is not going to start its next job from
						// the drop location as it got lost or the job was cancelled
						if (this.robotGotLost3 || this.jobCancelled3) {

							number++;
							this.selector3 = new JobSelectorMultiple(number, this.robot3, this.jobs, this.cancellationMachine, this.checkList);

							this.gotList3 = false;

							this.robotGotLost3 = false;
							this.jobCancelled3 = false;

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}
						}

						if (gotList3) {

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// Sleep was interrupted for some reason
								e.printStackTrace();
							}
							
							this.assignJobs3 = this.selector3.getSelectedList();

							// Get the next job to be assigned
							jobToBeAssigned = this.assignJobs3.removeFirst();

							// Remove it from the list of jobs
							this.jobs.remove(jobToBeAssigned.getJob());

							// Create a new assigned job and set it as current
							this.currentJob3 = this.assign(this.robot3, jobToBeAssigned);
							EventDispatcher.onEvent2(new SelectorHasCurrentJobEvent());

							// Tell subscribers
							JobAssignedEvent e = new JobAssignedEvent(this.currentJob3);

							EventDispatcher.onEvent2(e);

							this.jobComplete3 = false;
						}

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// sleep was interrupted for some reason
							e.printStackTrace();
						}

						if((assignedJobsFinished3) && (this.assignJobs3.size() <= 0)){

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
		EventDispatcher.onEvent2(new FinishedAssigningEvent());
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

		Route improvedRoute = null;
		
		if(robot.equals(this.robot1)){
				
			// TODO improvedRoute = this.TSP.improvedRoute(robot, jobWorth.getRoute(), this.currentJob2, this.currentJob3); 
		} else if(robot.equals(this.robot2)){
			
			// TODO improvedRoute = this.TSP.improvedRoute(robot, jobWorth.getRoute(), this.currentJob1, this.currentJob3); 
		} else if(robot.equals(this.robot3)){
			
			// TODO improvedRoute = this.TSP.improvedRoute(robot, jobWorth.getRoute(), this.currentJob1, this.currentJob2); 
		}
		
		return new AssignedJob(jobWorth.getJob(), improvedRoute, robot);
	}

	/**
	 * Method to get the current assigned job the selector has chosen
	 * for a particular robot
	 * 
	 * @param robot the robot
	 * @return the current assigned job it has
	 */
	public Optional<AssignedJob> getCurrentJob(Robot robot) {

		if(robot.equals(this.robot1)){
		
			return Optional.of(this.currentJob1);
		} else if(robot.equals(this.robot2)){
			
			return Optional.of(this.currentJob2);
		} else if(robot.equals(this.robot3)){
			
			return Optional.of(this.currentJob3);
		} else{
			
			return Optional.empty();
		}
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
		
		if(e.robot.equals(this.robot1)){
			
			this.readyToStart1 = true;
		} else if(e.robot.equals(this.robot2)){
			
			this.readyToStart2 = true;
		} else if(e.robot.equals(this.robot3)){
			
			this.readyToStart3 = true;
		}
	}

	/**
	 * Listen for when we are told to start taking jobs from the selector's list
	 * 
	 * @param e the added to selected list event
	 */
	@Subscriber
	public void onAddedToSelectedListEvent(AddedToSelectedListEvent e){

		if(e.robot == null){
			
			//Do nothing
		}
		else if(e.robot.equals(this.robot1)){
			this.gotList1 = true;
		} else if(e.robot.equals(this.robot2)){
			this.gotList2 = true;
		} else if(e.robot.equals(this.robot3)){
			this.gotList3 = true;
		}
	}

	/**
	 * Listen for when we the selector has finished selecting jobs
	 * 
	 * @param e the finished selection event
	 */
	@Subscriber
	public void onFinishedSelectionEvent(FinishedSelectionEvent e){

		if(e.robot == null){
			
			//Do nothing
		}
		else if(e.robot.equals(this.robot1)){
			this.assignedJobsFinished1 = true;
		} else if(e.robot.equals(this.robot2)){
			this.assignedJobsFinished2 = true;
		} else if(e.robot.equals(this.robot3)){
			this.assignedJobsFinished3 = true;
		}
	}

	/**
	 * Listen for if the robot gets lost.
	 * 
	 * @param e the robot lost event
	 */
	@Subscriber
	public void onRobotLostEvent(RobotLostEvent e) {

		if(e.robot == null){
			
			//Do nothing
		}
		else if(e.robot.equals(this.robot1)){
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
	public void onJobCompleteEvent(JobCompleteEvent e) {
		
		if(e.robot == null){
			
			//Do nothing
		}
		else if(e.robot.equals(this.robot1)){
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
	 *            the job cancellation event
	 */
	@Subscriber
	public void onJobCancellationEvent(JobCancellationEvent e) {
		
		if(e.robot == null){
			
			//Do nothing
		}
		else if(e.robot.equals(this.robot1)){
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
