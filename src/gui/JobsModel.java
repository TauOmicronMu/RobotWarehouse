package gui;

import java.util.List;
import java.util.Observable;

import javafx.util.Pair;
import warehouse.job.AssignedJob;
import warehouse.job.Job;
import warehouse.util.Robot;

public class JobsModel extends Observable {
	
	private List<Job> unassigned;
	private List<AssignedJob> assigned;

	public JobsModel(List<Job> unassigned, List<AssignedJob> assigned) {
		this.unassigned = unassigned;
		this.assigned = assigned;
	}
	
	public List<Job> getUnassignedList(){
		return unassigned;
	}
	
	public List<AssignedJob> getAssignedList(){
		return assigned;
	}
	
	
//	public void assign(Job job, Robot robot){
//		AssignedJob newAssignedJob = new AssignedJob();
//		addToAssigned(job);
//		unassigned.remove(job);
//		Communication.jobAssigned(job, robot);
//		setChanged();
//		notifyObservers();
//		
//	}
	
	public void addToUnassigned(Job job){
		unassigned.add(job);
		setChanged();
		notifyObservers();
		
		
	}
	
	public void cancel (Job job){
		addToUnassigned(job);
		assigned.remove(job);
		Communication.jobCancelled(job);
		setChanged();
		notifyObservers();
	}

//	public void addToAssigned(AssignedJob job) {
//		assigned.add(job);
//		setChanged();
//		notifyObservers();
//		
//	}
	
	public Job findUnassignedJob(String toString){
		for(int i=0; i<unassigned.size(); i++)
			if (unassigned.get(i).toString().equals(toString)) return unassigned.get(i);
	
		// will not happen
		return null;
	}
	
	public AssignedJob findAssignedJob(String toString){
		for(int i=0; i<assigned.size(); i++)
			if (assigned.get(i).toString().equals(toString)) return assigned.get(i);
	
		// will not happen
		return null;
	}

}
