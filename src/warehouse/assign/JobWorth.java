package warehouse.assign;



import java.util.ArrayList;
import java.util.List;

import warehouse.ItemPickup;
import warehouse.job.Job;

/**
 * JOB SELECTION - (JobWorth class):
 * 
 * Created by Owen on 28/02/2016
 * 
 * Preliminary class to:
 * -calculate the 'worth' of a job
 * -provide fields for reward per timestep/weight
 * 
 * @author Owen
 *
 */
public class JobWorth implements Comparable<JobWorth>{

	private Job job;
	private double rewardTime;
	private double rewardWeight;
	private double metric;
	
	/**
	 * Create a new JobWorth object that contains a job and also describes
	 * how good it is to assign it to a robot.
	 * 
	 * @param job the given job
	 */
	public JobWorth(Job job){
		
		this.job = job;
		
		//Factor in the cancellation probability
		double p = 1 - findCancellationProbability(job);
		
		this.rewardTime = p * rewardPerTimeStep(job);
		this.rewardWeight = p * rewardPerWeight(job);
		
		this.metric = (rewardTime + rewardWeight) / 2;
	}
	
	/**
	 * Get method for the job.
	 * 
	 * @return the job
	 */
	public Job getJob(){
		
		return this.job;
	}
	
	/**
	 * Get method for reward per time step.
	 * 
	 * @return the reward per time step
	 */
	public double getRewardTime() {
		return rewardTime;
	}

	/**
	 * Get method for reward per weight.
	 * 
	 * @return the reward per weight
	 */
	public double getRewardWeight() {
		return rewardWeight;
	}

	/**
	 * Get method for average of the two reward values.
	 * 
	 * @return the average reward for the job
	 */
	public double getMetric() {
		return metric;
	}

	/**
	 * To String method.
	 */
	public String toString(){
		
		return "Job of time reward: " + this.rewardTime + " and weight reward: " + this.rewardWeight + " metric of: " + this.metric;
	}

	/**
	 * Helper method to calculate the reward per time step.
	 * 
	 * @param job the given job
	 * @return the reward per time step
	 */
	private double rewardPerTimeStep(Job job){
		
		/*reward per time step = 
		 * 
		 *  sum from 1 to k (number of pickups) of:
		 *  	
		 *  	number of items * reward per item
		 * 	
		 * Divided by:
		 * 
		 * 	total time to execute all pickups
		 */
		
		List<ItemPickup> pickups = new ArrayList<ItemPickup>(job.pickups);
		
		double sumReward = 0; 
		
		for(ItemPickup pickup : pickups){
			
			double itemReward = 1; //TODO Need method to get reward for items.
			
			sumReward += (pickup.itemCount * itemReward);
		}
		
		int bestDistance = 1; //TODO Need way to get optimum distance from route planning.
		
		return (sumReward/bestDistance);
	}
	
	/**
	 * Helper method to calculate the reward per weight.
	 * 
	 * @param job the given job
	 * @return the reward per weight
	 */
	private double rewardPerWeight(Job job){
		
		/*reward per weight = 
		 * 
		 *  sum from 1 to k (number of pickups) of:
		 *  	
		 *  	number of items * reward per item
		 * 	
		 * Divided by:
		 * 
		 * 	number of items * item weight
		 */
		
		List<ItemPickup> pickups = new ArrayList<ItemPickup>(job.pickups);
		
		double sumReward = 0; 
		double sumWeight = 0;
		
		for(ItemPickup pickup : pickups){
			
			double itemReward = 1; //TODO Need method to get reward for items.
			double itemWeight = 1; //TODO Need method to get weight of items.
			
			sumReward += (pickup.itemCount * itemReward);
			sumWeight += (pickup.itemCount * itemWeight);
		}
		
		return (sumReward/sumWeight);
	}
	
	/**
	 * Helper method to find the cancellation probability
	 * 
	 * @param job the given job
	 * @return the cancellation probability
	 */
	private double findCancellationProbability(Job job){
		
		//TODO calculate the cancellation probability for a job
		return 0;
	}

	@Override
	public int compareTo(JobWorth jobworth) {
		
		if(this.getMetric() > jobworth.getMetric()){
			
			return 1;
		}
		else if(this.getMetric() < jobworth.getMetric()){
			
			return -1;
		}
		
		return 0;
	}
}
