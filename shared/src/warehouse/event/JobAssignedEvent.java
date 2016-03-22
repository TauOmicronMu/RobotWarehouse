package warehouse.event;

import warehouse.job.AssignedJob;
import warehouse.util.Robot;

/**
 * Event to send to subscribers when a job is assigned
 * 
 * @author Owen
 *
 */
public class JobAssignedEvent extends Event {

	public final AssignedJob assignedJob;

	/**
	 * Create a new event
	 * 
	 * @param assigned the assigned job
	 */
	public JobAssignedEvent(AssignedJob assigned) {
		super(assigned.robot);
		this.assignedJob = assigned;
	}


}
