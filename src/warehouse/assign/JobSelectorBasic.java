package warehouse.assign;

import java.util.ArrayList;

import warehouse.Robot;
import warehouse.job.AssignedJob;
import warehouse.job.Job;

public class JobSelectorBasic {

	private Robot robot;
	private ArrayList<Job> jobs;
	
	public JobSelectorBasic(Robot robot, ArrayList<Job> jobs){
		
		//Get the active robot
		this.robot = robot;
		
		//Get the list of jobs from job input
		this.jobs = jobs;
		
		while(this.jobs.size() != 0){
		
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
	
	public void sendCommand(AssignedJob job){
		
		//TODO send assigned job to server
	}
}
