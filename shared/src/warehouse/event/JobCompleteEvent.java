package warehouse.event;

import warehouse.job.*;

/**
 * Event to send to subscribers when a job is completed.
 * 
 * @author Owen
 *
 */
public class JobCompleteEvent extends Event {

	public final AssignedJob job;

	public JobCompleteEvent(AssignedJob job){
		super(job.robot);
		this.job = job;
	}

}
