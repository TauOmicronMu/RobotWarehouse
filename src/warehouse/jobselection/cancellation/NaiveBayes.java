package warehouse.jobselection.cancellation;

import java.util.LinkedList;

import warehouse.job.Job;
import warehouse.jobselection.JobWorth;

/**
 * JOB SELECTION - (NaiveBayes class)
 * 
 * Created by Owen on 14/03/2016:
 * 
 * Naive Bayes Classifier implementation of a Cancellation Machine
 * 
 * @author Owen
 *
 */
public class NaiveBayes implements CancellationMachine {

	private LinkedList<Job> trainingSet;

	/**
	 * Construct a NaiveBayes object with a training set and set up class descriptors
	 * 
	 * @param trainingSet the set of jobs to learn from
	 */
	public NaiveBayes(LinkedList<Job> trainingSet) {

		this.trainingSet = trainingSet;

		for(Job job : trainingSet){
			
			
		}
		
	
	}

	/**
	 * See interface.
	 */
	@Override
	public double getProbability(JobWorth job) {
		
		/*TODO
		 * CANCELLEDPROBABILITY = p(JOBCANCELLED) * ([sigmaproduct from 0 to NUMBER OF DESCRIPTORS] of p(DESCRIPTOR|JOBCANCELLED))
		 * 
		 */
		return 0;
	}
}
