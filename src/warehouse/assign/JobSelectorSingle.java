package warehouse.assign;

import java.util.Collections;
import java.util.LinkedList;

import warehouse.job.AssignedJob;
import warehouse.job.Job;
import warehouse.util.EventDispatcher;
import warehouse.util.Location;
import warehouse.util.Robot;
import warehouse.util.Subscriber;


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
public class JobSelectorSingle extends Thread{

	private Robot robot;
	private boolean run;
	private AssignedJob currentJob;
	private boolean robotGotLost;
	private boolean jobComplete;
	private LinkedList<Job> jobs;
	private Location robotStartLocation;
	
	/**
	 * Create a job selector that chooses jobs for a single robot based on a list 
	 * of available jobs.
	 * 
	 * @param robot the robot
	 * @param jobs the list of available jobs
	 */
	public JobSelectorSingle(Robot robot, LinkedList<Job> jobs){
		
		this.robot = robot;
		this.jobs = jobs;
	}
	
	/**
	 * Method to run when the thread starts - continuously picks jobs for the robot.
	 */
	@Override
	public void run(){
		
		EventDispatcher.subscribe2(this);
		
		this.run = true;
		
		//Sign up to the event dispatcher
		EventDispatcher dispatcher = EventDispatcher.INSTANCE;
		
		JobWorth bestJob;
		
		//TODO Add localisation?
		Location startLocation = this.robot.position;
		
		//Get the list of job worths
		LinkedList<JobWorth> jobworths = convertList(startLocation);
		
		//get the best job
		bestJob = selectBestJob(jobworths);
		
		//get rid of the best job from the list of jobs
		this.jobs.remove(bestJob.getJob());

		//make a new assigned job
		AssignedJob assigned = assign(this.robot, bestJob);
		this.currentJob = assigned;
		
		//send the assigned job to subscribers
		JobAssignedEvent e = new JobAssignedEvent(assigned, this.robot);
		dispatcher.onEvent(e);

		//TODO multiple drop locations?
		Location dropLocation = new Location(4, 7);
		
		//Clear the list and calculate job worths again based on the new location
		jobworths = convertList(dropLocation);
		
		//continuously give the best job left to the robot until there are no more jobs
		//or it is told to stop
		while(run && (this.jobs.size() > 0)){
			
			//If the robot has completed a job and now has no assigned job, give it a new one
			if(this.jobComplete){
				
				//If the robot is not going to start its next job from the drop location as it got lost
				if(robotGotLost){
			
					//TODO wait for localisation event?
					
					//Clear the list and calculate job worths again based on the new location
					startLocation = this.robot.position;
				
					jobworths = convertList(startLocation);
				
					this.robotGotLost = false;
				}
			
				//get the best job
				bestJob = selectBestJob(jobworths);
			
				//get rid of the best job from the list of jobs
				this.jobs.remove(bestJob);
	
				//make a new assigned job
				assigned = assign(this.robot, bestJob);
				this.currentJob = assigned;
			
				//send the assigned job to route execution?
				e = new JobAssignedEvent(assigned, this.robot);
				dispatcher.onEvent(e);
				
				this.jobComplete = false;
				
			}
			
			try {
				
				Thread.sleep(100);
				
			} catch (InterruptedException e1) {
				
				//Sleep was interrupted for some reason
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * Convert the list of jobs into a list of jobworth objects
	 * based on the location given
	 * 
	 * @param the location the worth is based on
	 * @return the converted list
	 */
	public LinkedList<JobWorth> convertList(Location startLocation){
		
		//make an empty list of jobworths
		LinkedList<JobWorth> jobworths = new LinkedList<JobWorth>();
		
		//Calculate the worth of each job and add them to the list
		for(Job job : this.jobs){
					
			jobworths.add(new JobWorth(job, this.robot, startLocation));
		}
		
		return jobworths;
	} 
	
	/**
	 * Get the best job worth based on a list of job worths
	 * 
	 * @param jobworths the list of job worths
	 * @return the best job worth
	 */
	public JobWorth selectBestJob(LinkedList<JobWorth> jobworths){
		
		return Collections.max(jobworths);
	}
	
	/**
	 * Method to listen for robot getting lost, and localising to a new location
	 * 
	 * @param l the new location
	 */
	@Subscriber
	private void onRobotLost(Location l){
		
		//TODO integrate with localisation
		this.robotStartLocation = l;
		this.robotGotLost = true;
	}
	
	/**
	 * Method to listen for robot having finished last job
	 * 
	 * @param e the job finished event
	 */
	@Subscriber
	private void onJobComplete(JobCompleteEvent e){
		
		this.jobComplete = true;
		this.robotStartLocation = e.getLocation();
	}
	
	/**
	 * Helper method to create a new assigned job for the robot.
	 * 
	 * @param robot the robot
	 * @param job the job to be assigned
	 * @return an AssignedJob object
	 */
	private AssignedJob assign(Robot robot, JobWorth jobWorth){
		
		return new AssignedJob(jobWorth.getJob(), jobWorth.getRoute(), robot);
	}
	
	/**
	 * Method to allow the selector to be stopped before the list of jobs is empty.
	 */
	public void stopSelection(){
		
		this.run = false;
	}
	
	/**
	 * Method to get the current assigned job the selector has chosen
	 * 
	 * @return the current assigned job
	 */
	public AssignedJob getCurrentJob(){
		
		return this.currentJob;
	}
}
