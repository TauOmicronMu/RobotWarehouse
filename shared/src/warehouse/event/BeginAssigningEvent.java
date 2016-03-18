package warehouse.event;

import java.util.*;

/**
 * Event to be passed when the robot is ready to be assigned jobs
 * 
 * @author Owen
 *
 */
public class BeginAssigningEvent {
	
	public List<Job> jobs;

	/**
	 * Create a new event
	 */
	public BeginAssigningEvent(List<Job> jobs){
		this.jobs = jobs;
	}
}
