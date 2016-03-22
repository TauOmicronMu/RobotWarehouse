package warehouse.jobselection;

import warehouse.job.Job;
import warehouse.routePlanning.TSP;
import warehouse.util.Direction;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Robot;
import warehouse.util.Route;

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
	private Direction facing;
	
	/**
	 * Create a new JobWorth object that contains a job and also describes
	 * how good it is to assign it to a robot.
	 * 
	 * @param job the given job
	 */
	public JobWorth(Job job, Robot robot, Location startLocation, Direction facing){
		
		this.job = job;
		this.facing = facing;
		

		TSP tsp = new TSP();

		this.route = tsp.getShortestRoute(job, startLocation, facing).get();
		this.facing = this.route.finalFacing;
		
		this.rewardTime = rewardPerTimeStep(job);
	
		this.metric = rewardTime;
	}
	
	/**
	 * Get method for the final facing.
	 * 
	 * @return the direction corresponding to the final facing
	 */
	public Direction getFinalFacing(){
		
		return this.facing;
	}
	
	/**
	 * Get method for the end location of the route
	 * 
	 * @return the location at the end of the route
	 */
	public Location getEndLocation(){
		
		return this.route.end;
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
	 * Get method for cancellation probability incorporated with reward values.
	 * 
	 * @return the average reward for the job
	 */
	public double getMetric() {
		return metric;
	}
	
	/**
	 * Set the metric for this jobworth
	 * 
	 * @param metric the metric for this job
	 */
	public void setMetric(double metric){
		
		this.metric = metric;
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
	
	@Override
	public boolean equals(Object o){
		
		if((o instanceof JobWorth) && (this.getMetric() == ((JobWorth)o).getMetric())){
			
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		
		return (int)this.getMetric();
	}
}
