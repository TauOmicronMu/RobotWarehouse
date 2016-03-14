package warehouse.jobselection.cancellation;

import warehouse.jobselection.JobWorth;

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

	/**
	 * Get the probability of a job being cancelled, given its jobworth object 
	 * 
	 * @param job the jobworth object
	 * @return the probability of the job being cancelled
	 */
	public double getProbability(JobWorth job);
}
