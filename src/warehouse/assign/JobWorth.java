package warehouse.assign;



import warehouse.ItemPickup;
import warehouse.Robot;
import warehouse.Route;
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
	
	private Route route;
	
	/**
	 * Create a new JobWorth object that contains a job and also describes
	 * how good it is to assign it to a robot.
	 * 
	 * @param job the given job
	 */
	public JobWorth(Job job, Robot robot){
		
		this.job = job;
		
		//this.route = new TSP.getShortestRoute(job , robot.position);
		
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
	 * Get method for the route
	 * 
	 * @return the route
	 */
	public Route getRoute(){
		
		return this.route;
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
		
		double sumReward = 0; 
		
		for(ItemPickup pickup : job.pickups){
			
			sumReward += (pickup.itemCount * pickup.reward);
		}
		
		int bestDistance = this.route.totalDistance;
		
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
		
		double sumReward = 0; 
		double sumWeight = 0;
		
		for(ItemPickup pickup : job.pickups){
			
			sumReward += (pickup.itemCount * pickup.reward);
			sumWeight += (pickup.itemCount * pickup.weight);
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
