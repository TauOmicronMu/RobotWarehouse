package warehouse.jobselection.cancellation;

import warehouse.job.Job;

public class Backup implements CancellationMachine{

	@Override
	public double getProbability(Job job) {
		return 0;
	}
	
	@Override
	public String toString(){
		
		return "Error when attempting to create Cancellation Machine, using Backup.";
	}
}
