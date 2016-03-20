package warehouse.jobselection.cancellation;

import warehouse.job.Job;

public class Backup implements CancellationMachine{

	@Override
	public double getProbability(Job job) {
		return 0;
	}

}
