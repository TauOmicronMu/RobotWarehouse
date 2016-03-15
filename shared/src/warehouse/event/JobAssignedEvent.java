package warehouse.event;

import warehouse.job.AssignedJob;
import warehouse.util.Robot;

/**
 * Event to send to subscribers when a job is assigned
 * 
 * @author Owen
 *
 */
public class JobAssignedEvent {

	private AssignedJob assignedJob;
	private Robot robot;
	
	/**
	 * Create a new event
	 * 
	 * @param assigned the assigned job
	 */
	public JobAssignedEvent(AssignedJob assigned, Robot robot) {
		
		this.robot = robot;
		this.assignedJob = assigned;
	}

	/**
	 * Get the assigned job object
	 * 
	 * @return the assigned job object
	 */
	public AssignedJob getAssignedJob(){
		
		return this.assignedJob;
	}
	
	/**
	 * Get the robot the job was assigned to
	 * 
	 * @return the robot object
	 */
	public Robot getRobot(){
		
		return this.robot;
	}
}
