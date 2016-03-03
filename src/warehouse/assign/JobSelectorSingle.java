package warehouse.assign;

import java.util.Collections;
import java.util.LinkedList;

import warehouse.job.AssignedJob;
import warehouse.job.Job;
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
public class JobSelectorSingle{

	private Robot robot;
	private boolean run;
	
	/**
	 * Create a job selector that chooses jobs for a single robot based on a list 
	 * of available jobs.
	 * 
	 * @param robot the robot
	 * @param jobs the list of available jobs
	 */
	public JobSelectorSingle(Robot robot, ArrayList<Job> jobs){
		
		this.robot = robot;
		this.run = true;
		
		LinkedList<JobWorth> jobworths = new LinkedList<JobWorth>();
		
		JobWorth bestJob;
		
		//Calculate the worth of each job and add them to the list
		for(Job job : jobs){

			jobworths.add(new JobWorth(job, this.robot));
			
		}
		
		//continuously give the best job left to the robot until there are no more jobs
		//or it is told to stop
		while(run && (jobworths.size() > 0)){

			bestJob = Collections.max(jobworths);
			
			jobworths.remove(bestJob);
			jobs.remove(bestJob.getJob());
	
			AssignedJob assigned = assign(this.robot, bestJob);
			
			//TODO Robot performs job
			
			//Clear the list so new best jobs can be found based on the robots new location
			jobworths = new LinkedList<JobWorth>();
			
			//Calculate the worth of each job and add them to the list
			for(Job job : jobs){
				jobworths.add(new JobWorth(job, this.robot));
			}
		}
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
}
