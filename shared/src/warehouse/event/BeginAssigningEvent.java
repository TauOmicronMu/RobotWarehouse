package warehouse.event;

import warehouse.job.Job;
import warehouse.util.*;

import java.util.*;

/**
 * Event to be passed when the robot is ready to be assigned jobs
 * 
 * @author Owen
 *
 */
public class BeginAssigningEvent {
	
	public List<Job> jobs;
	public List<Location> dropLocations;

	/**
	 * Create a new event
	 */
	public BeginAssigningEvent(List<Job> jobs, List<Location> dropLocations){
		this.jobs = jobs;
		this.dropLocations = dropLocations;
	}
}
