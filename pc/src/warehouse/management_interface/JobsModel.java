package warehouse.management_interface;

/**
 * A model for the job lists
 */
import java.util.List;
import java.util.Observable;

import warehouse.event.JobAssignedEvent;
import warehouse.event.JobCancellationEvent;
import warehouse.job.AssignedJob;
import warehouse.job.Job;
import warehouse.util.EventDispatcher;
import warehouse.util.Subscriber;

public class JobsModel extends Observable {

	private List<Job> unassigned;
	private List<AssignedJob> assigned;

	/**
	 * Constructor
	 * 
	 * @param unassigned
	 *            unassigned jobs list
	 * @param assigned
	 *            assigned jobs list
	 */
	public JobsModel(List<Job> unassigned, List<AssignedJob> assigned) {
		this.unassigned = unassigned;
		this.assigned = assigned;
		assert (assigned.size() < 0);
		EventDispatcher.subscribe2(this);
	}

	/**
	 * 
	 * @return unassigned jobs
	 */
	public List<Job> getUnassignedList() {
		return unassigned;
	}

	/**
	 * 
	 * @return assigned jobs
	 */
	public List<AssignedJob> getAssignedList() {
		return assigned;
	}

	/**
	 * Add job to unassigned list
	 * 
	 * @param job
	 */
	public void addToUnassigned(Job job) {
		unassigned.add(job);
		setChanged();
		notifyObservers();

	}
	
	/**
	 * Add job to assigned list
	 * 
	 * @param job
	 */
	public void addToAssigned(AssignedJob job) {
		assigned.add(job);
		setChanged();
		notifyObservers();

	}
	
	/**
	 * Remove job from unassigned list
	 * 
	 * @param job
	 */
	public void removeFromUnassigned(Job job) {
		unassigned.remove(job);
		setChanged();
		notifyObservers();

	}
	
	/**
	 * Remove job from assigned list
	 * 
	 * @param job
	 */
	public void removeFromAssigned(AssignedJob job) {
		assigned.remove(job);
		setChanged();
		notifyObservers();

	}

	/**
	 * Cancel a job
	 * 
	 * @param job
	 */
	public void cancel(AssignedJob job) {
		addToUnassigned(unassign(job));
		assigned.remove(job);
		jobCancelled(job);
		setChanged();
		notifyObservers();
	}

	public void jobCancelled(AssignedJob assignedJob) {
		Job job = unassign(assignedJob);
		// Generate JobCancelledEvent
		EventDispatcher.onEvent2(new JobCancellationEvent(job)); //TODO
		
	}

	/**
	 * Unassign a job (Convert from AssignedJob to Job)
	 * 
	 * @param job
	 * @return
	 */
	private Job unassign(AssignedJob job) {
		return new Job(job.dropLocation, job.pickups);
	}

	/**
	 * Find an unassigned job by it's toString
	 * 
	 * @param toString
	 * @return
	 */
	public Job findUnassignedJob(String toString) {
		for (int i = 0; i < unassigned.size(); i++)
			if (unassigned.get(i).toString().equals(toString))
				return unassigned.get(i);

		// will not happen
		return null;
	}

	/**
	 * Find an assigned job by it's toString
	 * 
	 * @param toString
	 * @return
	 */
	public AssignedJob findAssignedJob(String toString) {
		for (int i = 0; i < assigned.size(); i++)
			if (assigned.get(i).toString().equals(toString))
				return assigned.get(i);

		// will not happen
		return null;
	}

	/**
	 * Communication methods = self-explanatory so I won't document each one
	 */

	@Subscriber
	public void onJobCancelled(JobCancellationEvent e) {
		Job toAddToUnassigned = e.getJob(); //TODO
		AssignedJob toDeleteFromAssigned = findAssignedJob(toAddToUnassigned.toString());
		
		addToUnassigned(toAddToUnassigned);
		removeFromAssigned(toDeleteFromAssigned);

	}
	
	@Subscriber
	public void onJobAssigned(JobAssignedEvent e) {
		AssignedJob toAddToAssigned = e.getAssignedJob();
		Job toDeleteFromUnassigned = findUnassignedJob(toAddToAssigned.toString());

		addToAssigned(toAddToAssigned);
		removeFromUnassigned(toDeleteFromUnassigned);
	}
	
	@Subscriber
	public void onJobFinished(JobFinishedEvent e) { //TODO
		Job job = e.getJob();
		
		if (assigned.contains(job))
			removeFromAssigned((AssignedJob) job);
		
		if (unassigned.contains(job))
			removeFromUnassigned(job);

	}

}
