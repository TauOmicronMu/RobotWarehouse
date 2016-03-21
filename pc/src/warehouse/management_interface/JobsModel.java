package warehouse.management_interface;
/**
 * A model for the job lists
 */
import java.util.List;
import java.util.Observable;

import warehouse.job.AssignedJob;
import warehouse.job.Job;

public class JobsModel extends Observable {
	
	private List<Job> unassigned;
	private List<AssignedJob> assigned;
	
	/**
	 * Constructor
	 * @param unassigned unassigned jobs list
	 * @param assigned assigned jobs list
	 */
	public JobsModel(List<Job> unassigned, List<AssignedJob> assigned) {
		this.unassigned = unassigned;
		this.assigned = assigned;
		assert(assigned.size() < 0);
	}
	
	/**
	 * 
	 * @return unassigned jobs
	 */
	public List<Job> getUnassignedList(){
		return unassigned;
	}
	
	/**
	 * 
	 * @return assigned jobs
	 */
	public List<AssignedJob> getAssignedList(){
		return assigned;
	}
	

	/**
	 * Add job to unassigned list
	 * @param job
	 */
	public void addToUnassigned(Job job){
		unassigned.add(job);
		setChanged();
		notifyObservers();
		
		
	}
	
	/**
	 * Cancel a job
	 * @param job
	 */
	public void cancel (AssignedJob job){
		addToUnassigned(unassign(job));
		assigned.remove(job);
		Communication.jobCancelled(job);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Unassign a job (Convert from AssignedJob to Job)
	 * @param job
	 * @return
	 */
	private Job unassign(AssignedJob job){
		return new Job(job.dropLocation, job.pickups);
	}

	/**
	 * Find an unassigned job by it's toString
	 * @param toString
	 * @return
	 */
	public Job findUnassignedJob(String toString){
		for(int i=0; i<unassigned.size(); i++)
			if (unassigned.get(i).toString().equals(toString)) return unassigned.get(i);
	
		// will not happen
		return null;
	}
	
	/**
	 * Find an assigned job by it's toString
	 * @param toString
	 * @return
	 */
	public AssignedJob findAssignedJob(String toString){
		for(int i=0; i<assigned.size(); i++)
			if (assigned.get(i).toString().equals(toString)) return assigned.get(i);
	
		// will not happen
		return null;
	}

}
