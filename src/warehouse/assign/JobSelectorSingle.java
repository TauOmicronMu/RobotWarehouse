package warehouse.assign;

import java.util.ArrayList;

import warehouse.Robot;
import warehouse.job.AssignedJob;
import warehouse.job.Job;

/**
 * 
 * JOB SELECTION - (JobSelectorBasic class):
 * 
 * Created by Owen on 25/02/2016
 * 
 * Preliminary class to:
 * - Get list of jobs from job input
 * - Give the first job to the robot when available
 * - Wait for the robot to become available again and assign the next job to it
 * 
 * @author Owen
 *
 */
public class JobSelectorBasic {

	private Robot robot;
	private ArrayList<Job> jobs;
	private boolean run;
	
	/**
	 * Construct the Job Selector for an existing robot and an ArrayList of jobs,
	 * then run the selector in a loop assigning jobs to the robot until there are no
	 * more jobs in the list, or it is told to stop.
	 * 
	 * @param robot the existing robot
	 * @param jobs the ArrayList of jobs received from Job Input
	 */
	public JobSelectorBasic(Robot robot, ArrayList<Job> jobs){
		
		//Set this to run
		this.run = true;
		
		//Get the active robot
		this.robot = robot;
		
		//Get the list of jobs from job input
		this.jobs = jobs;
		
		//Loop until there are no more jobs, or told to stop
		while((this.jobs.size() != 0) && run){
		
			if(this.robot.getBusy() == false){
		
				//Assign first job to robot
				AssignedJob newJob = new AssignedJob(this.jobs.get(0), this.robot);
		
				//Remove first job from list
				this.jobs.remove(0);
		
				//Make robot busy
				this.robot.setBusy(true);
		
				//Send job to server to send to robot
				sendCommand(newJob);
	
			}
			
			//TODO make robot not busy when the job is completed
		}
	}
	
	/**
	 * Helper method to send a command to the server,
	 * with an assigned job object describing both the job selected for the
	 * robot, and the robot object itself.
	 * 
	 * Should turn the assigned job object into a form that can be sent
	 * to and understood by the server.
	 * 
	 * @param job the assigned job
	 */
	private void sendCommand(AssignedJob job){
		
		//TODO send assigned job to server
	}
	
	/**
	 * Method to allow the selector to be stopped before the list of jobs is empty.
	 */
	public void stopSelection(){
		
		this.run = false;
	}
}
