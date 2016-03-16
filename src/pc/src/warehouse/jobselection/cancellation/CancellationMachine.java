package warehouse.jobselection.cancellation;

import warehouse.job.Job;

/**
 * JOB SELECTION - (CancellationMachine class)
 * 
 * Created by Owen on 14/03/2016:
 * 
 * Interface that describes a learning machine for job cancellation
 * 
 * @author Owen
 *
 */
public interface CancellationMachine {

	public double getProbability(Job job);
}
