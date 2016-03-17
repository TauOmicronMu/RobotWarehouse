package warehouse.jobselection.cancellation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import warehouse.job.Job;
import warehouse.jobselection.cancellation.enums.NumRange;
import warehouse.jobselection.cancellation.enums.RewardRange;
import warehouse.jobselection.cancellation.enums.WeightRange;
import warehouse.util.ItemPickup;

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

	private Distribution NumberPickupsGivenCancelled;
	private Distribution TotalRewardGivenCancelled;
	private Distribution TotalWeightGivenCancelled;
	
	private Distribution NumberPickupsGivenNotCancelled ;
	private Distribution TotalRewardGivenNotCancelled;
	private Distribution TotalWeightGivenNotCancelled;
	
	private Descriptor NumberPickups = new Descriptor("Number of Pickups", NumberPickupsGivenCancelled, NumberPickupsGivenNotCancelled);
	private Descriptor TotalReward = new Descriptor("Total Reward", TotalRewardGivenCancelled, TotalRewardGivenNotCancelled);
	private Descriptor TotalWeight = new Descriptor("Total Weight", TotalWeightGivenCancelled, TotalWeightGivenNotCancelled);
	
	/**
	 * Construct a NaiveBayes object with a training set and set up class
	 * descriptors
	 * 
	 * @param trainingSet
	 *            the set of jobs to learn from
	 */
	@SuppressWarnings("unchecked")
	public NaiveBayes(LinkedList<Job> trainingSet) {

		
		
		//INITIALISING DPAIRS FOR THE DISTRIBUTIONS
		
		//number of pickups
		ArrayList<DPair> numberPickupsList = new ArrayList<DPair>();
		numberPickupsList.add(new DPair(NumRange.OneToNine, 0.001));
		numberPickupsList.add(new DPair(NumRange.TenToNineteen, 0.001));
		numberPickupsList.add(new DPair(NumRange.TwentyToTwentyNine, 0.001));
		numberPickupsList.add(new DPair(NumRange.ThirtyPlus, 0.001));
		
		this.NumberPickupsGivenCancelled = new Distribution((ArrayList<DPair>) numberPickupsList.clone());
		this.NumberPickupsGivenNotCancelled = new Distribution((ArrayList<DPair>) numberPickupsList.clone());
		
		//total reward
		ArrayList<DPair> totalRewardList = new ArrayList<DPair>();
		totalRewardList.add(new DPair(RewardRange.ZeroToNineteen, 0.001));
		totalRewardList.add(new DPair(RewardRange.TwentyToThirtyNine, 0.001));
		totalRewardList.add(new DPair(RewardRange.FortyToFiftyNine, 0.001));
		totalRewardList.add(new DPair(RewardRange.SixtyToSeventyNine, 0.001));
		totalRewardList.add(new DPair(RewardRange.EightyPlus, 0.001));
		
		this.TotalRewardGivenCancelled = new Distribution((ArrayList<DPair>) totalRewardList.clone());
		this.TotalRewardGivenNotCancelled = new Distribution((ArrayList<DPair>) totalRewardList.clone());
		
		//total weight
		ArrayList<DPair> totalWeightList = new ArrayList<DPair>();
		totalWeightList.add(new DPair(WeightRange.ZeroToFour, 0.001));
		totalWeightList.add(new DPair(WeightRange.FiveToNine, 0.001));
		totalWeightList.add(new DPair(WeightRange.TenToFourteen, 0.001));
		totalWeightList.add(new DPair(WeightRange.FifteenToNineteen, 0.001));
		totalWeightList.add(new DPair(WeightRange.TwentyToTwentyFour, 0.001));
		totalWeightList.add(new DPair(WeightRange.TwentyFiveToTwentyNine, 0.001));
		totalWeightList.add(new DPair(WeightRange.ThirtyPlus, 0.001));
		
		this.TotalWeightGivenCancelled = new Distribution((ArrayList<DPair>) totalWeightList.clone());
		this.TotalWeightGivenNotCancelled = new Distribution((ArrayList<DPair>) totalWeightList.clone());
		
		//INTITALISING VARIABLES FOR CALCULATING DPAIRS LATER
		
		//Number of Pickups:
		double pdenominatorNumberPickups = 0;
		double qdenominatorNumberPickups = 0;
					
		//Total Reward:
		double pdenominatorTotalReward = 0;
		double qdenominatorTotalReward = 0;
					
		//Total Weight:
		double pdenominatorTotalWeight = 0;
		double qdenominatorTotalWeight = 0;
		
		//DYNAMICALLY CREATING DIFFERENT DESCRIPTOR OBJECTS BASED ON WHICH EXIST IN JOBS
		
		for (Job job : trainingSet){
			
			//sort out denominators
			if(job.cancelledInTrainingSet){
				
				pdenominatorNumberPickups++;
				pdenominatorTotalReward++;
				pdenominatorTotalWeight++;
			}
			else{
				
				qdenominatorNumberPickups++;
				qdenominatorTotalReward++;
				qdenominatorTotalWeight++;
			}
			
			//sort out a few other things hidden inside item pickups from the jobs
			
			int intPickups = 0;
			double doubleReward = 0;
			double doubleWeight = 0;
			
			NumRange numberPickups = NumRange.Error;
			RewardRange totalReward = RewardRange.Error;
			WeightRange totalWeight = WeightRange.Error;
			
			for(ItemPickup pickup : job.pickups){
				
				intPickups += pickup.itemCount;
				doubleReward += (pickup.reward * pickup.itemCount);
				doubleWeight += (pickup.weight * pickup.itemCount);
			}
			
			//setting up enums for number of pickups
			if((0 < intPickups) && (intPickups < 10)){
				
				numberPickups = NumRange.OneToNine;
			}
			else if((10 <= intPickups) && (intPickups < 20)){
				
				numberPickups = NumRange.TenToNineteen;
			}
			else if((20 <= intPickups) && (intPickups < 30)){
				
				numberPickups = NumRange.TwentyToTwentyNine;
			}
			else if(30 <= intPickups){
				
				numberPickups = NumRange.ThirtyPlus;
			}
			
			//setting up enums for total reward
			if((0 <= doubleReward) && (doubleReward < 20)){
				
				totalReward = RewardRange.ZeroToNineteen;
			}
			else if((20 <= doubleReward) && (doubleReward < 40)){
				
				totalReward = RewardRange.TwentyToThirtyNine;
			}
			else if((40 <= doubleReward) && (doubleReward < 60)){
				
				totalReward = RewardRange.FortyToFiftyNine;
			}
			else if((60 <= doubleReward) && (doubleReward < 80)){
				
				totalReward = RewardRange.SixtyToSeventyNine;
			}
			else if(80 <= doubleReward){
				
				totalReward = RewardRange.EightyPlus;
			}
			
			//setting up enums for total weight
			if((0 <= doubleWeight) && (doubleWeight < 5)){
				
				totalWeight = WeightRange.ZeroToFour;
			}
			else if((5 <= doubleWeight) && (doubleWeight < 10)){
				
				totalWeight = WeightRange.FiveToNine;
			}
			else if((10 <= doubleWeight) && (doubleWeight < 15)){
				
				totalWeight = WeightRange.TenToFourteen;
			}
			else if((15 <= doubleWeight) && (doubleWeight < 20)){
				
				totalWeight = WeightRange.FifteenToNineteen;
			}
			else if((20 <= doubleWeight) && (doubleWeight < 25)){
				
				totalWeight = WeightRange.TwentyToTwentyFour;
			}
			else if((25 <= doubleWeight) && (doubleWeight < 30)){
				
				totalWeight = WeightRange.TwentyFiveToTwentyNine;
			}
			else if(30 <= doubleWeight){
				
				totalWeight = WeightRange.ThirtyPlus;
			}
			
			assert(numberPickups != NumRange.Error);
			assert(totalReward != RewardRange.Error);
			assert(totalWeight != WeightRange.Error);
			
			//Number of pickups	
			if(this.NumberPickups.p.contains(numberPickups)){
				
				assert(this.NumberPickups.p.get(numberPickups).isPresent());
				this.NumberPickups.p.get(numberPickups).get().probability++;
			}
			
			//Total reward
			if(this.TotalReward.p.contains(totalReward)){
				
				assert(this.TotalReward.p.get(totalReward).isPresent());
				this.TotalReward.p.get(totalReward).get().probability++;
			}

			//Total weight
			if(this.TotalWeight.p.contains(totalWeight)){
				
				assert(this.TotalWeight.p.get(totalWeight).isPresent());
				this.TotalWeight.p.get(totalWeight).get().probability++;
			}
		}
		
		for(int i = 0; i < this.NumberPickups.p.probabilities.size(); i++){
			
			this.NumberPickups.p.probabilities.get(i).probability /= pdenominatorNumberPickups;
			this.NumberPickups.q.probabilities.get(i).probability /= qdenominatorNumberPickups;
		}
		
		for(int i = 0; i < this.TotalReward.p.probabilities.size(); i++){
			
			this.TotalReward.p.probabilities.get(i).probability /= pdenominatorTotalReward;
			this.TotalReward.q.probabilities.get(i).probability /= qdenominatorTotalReward;
		}
		
		for(int i = 0; i < this.TotalWeight.p.probabilities.size(); i++){
			
			this.TotalWeight.p.probabilities.get(i).probability /= pdenominatorTotalWeight;
			this.TotalWeight.q.probabilities.get(i).probability /= qdenominatorTotalWeight;
		}
	}

	/**
	 * See interface.
	 */
	@Override
	public double getProbability(Job job) {

		/*
		 * TODO CANCELLEDPROBABILITY = p(JOBCANCELLED) * ([sigmaproduct from 0 to NUMBER OF DESCRIPTORS] of p(DESCRIPTOR|JOBCANCELLED))
		 * 
		 */
		return 0;
	}

	//DESCRIBES AN ARRAY OF DPAIRS
	
	private class Distribution {

		private ArrayList<DPair> probabilities;

		private Distribution(ArrayList<DPair> set) {

			this.probabilities = set;
		}

		public boolean contains(Object descriptor){
			
			for(int i = 0; i < this.probabilities.size(); i++){
				
				if(this.probabilities.get(i).descriptor.equals(descriptor)){
					
					return true;
				}
			}
			
			return false;
		}
		
		public Optional<DPair> get(Object descriptor){
			
			for(int i = 0; i < this.probabilities.size(); i++){
				
				if(this.probabilities.get(i).descriptor.equals(descriptor)){
					
					return Optional.of(this.probabilities.get(i));
				}
			}
			
			return Optional.empty();
		}
		
		@Override
		public boolean equals(Object o){
			
			if((o instanceof Distribution) && (((Distribution)o).probabilities.equals(this.probabilities))){
				
				return true;
			}
			
			return false;
		}
		
		@Override
		public int hashCode(){
			
			return 0;
		}
		
		public String toString(){
			
			String s = "";
			
			for(DPair pair : this.probabilities){
			
				s += pair.toString() + ", ";
			}
			
			return "{" + s + "}";
		}
	}

	//DESCRIBES SOMETHING WE CAN CATEGORISE A JOB BY
	
	private class Descriptor {

		private String name;
		private Distribution p;
		private Distribution q;

		private Descriptor(String name, Distribution p, Distribution q) {

			this.name = name;
			this.p = p;
			this.q = q;
		}

		@Override
		public boolean equals(Object o){
			
			if((o instanceof Descriptor) && (((Descriptor)o).p.equals(this.p)) && (((Descriptor)o).q.equals(this.q))){
				
				return true;
			}
			
			return false;
		}
		
		@Override
		public int hashCode(){
			
			return 0;
		}
		
		public String toString(){
			
			return "For Descriptor: " + this.name + " Distibution of: cancelled = " + this.p + " not cancelled = " + this.q;
		}
	}

	//DESCRIBES A LINKED OBJECT AND PROBABILITY
	
	private class DPair {

		private Object descriptor;
		public double probability;

		private DPair(Object a, double b) {

			this.descriptor = a;
			this.probability = b;
		}

		@Override
		public boolean equals(Object o){
			
			if((o instanceof DPair) && ((DPair)o).descriptor.equals(this.descriptor)){
				
				return true;
			}
			
			return false;
		}
		
		@Override
		public int hashCode(){
			
			return 0;
		}
		
		public String toString(){
			
			return "[" + this.descriptor.toString() + " p = " + this.probability + "]";
		}
	}
}