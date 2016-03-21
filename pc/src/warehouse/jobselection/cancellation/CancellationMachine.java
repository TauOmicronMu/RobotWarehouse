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

	/**
	 * Get the probability that a job will be cancelled, based on the training
	 * set that the Naive Bayes classifier was given in its constructor.
	 * 
	 * @param job
	 *            the job to have its cancellation probability cancelled
	 * @return the probability that it will be cancelled, as a double
	 */
	public double getProbability(Job job);
}
