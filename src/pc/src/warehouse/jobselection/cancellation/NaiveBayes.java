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
	
	private Descriptor NumberPickups;
	private Descriptor TotalReward;
	private Descriptor TotalWeight;
	
	/**
	 * Construct a NaiveBayes object with a training set and set up class
	 * descriptors
	 * 
	 * @param trainingSet
	 *            the set of jobs to learn from
	 */
	public NaiveBayes(LinkedList<Job> trainingSet) {
		
		//INITIALISING DPAIRS FOR THE DISTRIBUTIONS
		
		//number of pickups
		ArrayList<DPair> numberPickupsList1 = new ArrayList<DPair>();
		numberPickupsList1.add(new DPair(NumRange.OneToNine, 0));
		numberPickupsList1.add(new DPair(NumRange.TenToNineteen, 0));
		numberPickupsList1.add(new DPair(NumRange.TwentyToTwentyNine, 0));
		numberPickupsList1.add(new DPair(NumRange.ThirtyPlus, 0));
		
		ArrayList<DPair> numberPickupsList2 = new ArrayList<DPair>();
		numberPickupsList2.add(new DPair(NumRange.OneToNine, 0));
		numberPickupsList2.add(new DPair(NumRange.TenToNineteen, 0));
		numberPickupsList2.add(new DPair(NumRange.TwentyToTwentyNine, 0));
		numberPickupsList2.add(new DPair(NumRange.ThirtyPlus, 0));
		
		this.NumberPickupsGivenCancelled = new Distribution(numberPickupsList1);
		this.NumberPickupsGivenNotCancelled = new Distribution(numberPickupsList2);
		
		//total reward
		ArrayList<DPair> totalRewardList1 = new ArrayList<DPair>();
		totalRewardList1.add(new DPair(RewardRange.ZeroToNineteen, 0));
		totalRewardList1.add(new DPair(RewardRange.TwentyToThirtyNine, 0));
		totalRewardList1.add(new DPair(RewardRange.FortyToFiftyNine, 0));
		totalRewardList1.add(new DPair(RewardRange.SixtyToSeventyNine, 0));
		totalRewardList1.add(new DPair(RewardRange.EightyPlus, 0));
		
		ArrayList<DPair> totalRewardList2 = new ArrayList<DPair>();
		totalRewardList2.add(new DPair(RewardRange.ZeroToNineteen, 0));
		totalRewardList2.add(new DPair(RewardRange.TwentyToThirtyNine, 0));
		totalRewardList2.add(new DPair(RewardRange.FortyToFiftyNine, 0));
		totalRewardList2.add(new DPair(RewardRange.SixtyToSeventyNine, 0));
		totalRewardList2.add(new DPair(RewardRange.EightyPlus, 0));
		
		this.TotalRewardGivenCancelled = new Distribution(totalRewardList1);
		this.TotalRewardGivenNotCancelled = new Distribution(totalRewardList2);
		
		//total weight
		ArrayList<DPair> totalWeightList1 = new ArrayList<DPair>();
		totalWeightList1.add(new DPair(WeightRange.ZeroToFour, 0));
		totalWeightList1.add(new DPair(WeightRange.FiveToNine, 0));
		totalWeightList1.add(new DPair(WeightRange.TenToFourteen, 0));
		totalWeightList1.add(new DPair(WeightRange.FifteenToNineteen, 0));
		totalWeightList1.add(new DPair(WeightRange.TwentyToTwentyFour, 0));
		totalWeightList1.add(new DPair(WeightRange.TwentyFiveToTwentyNine, 0));
		totalWeightList1.add(new DPair(WeightRange.ThirtyPlus, 0));
		
		ArrayList<DPair> totalWeightList2 = new ArrayList<DPair>();
		totalWeightList2.add(new DPair(WeightRange.ZeroToFour, 0));
		totalWeightList2.add(new DPair(WeightRange.FiveToNine, 0));
		totalWeightList2.add(new DPair(WeightRange.TenToFourteen, 0));
		totalWeightList2.add(new DPair(WeightRange.FifteenToNineteen, 0));
		totalWeightList2.add(new DPair(WeightRange.TwentyToTwentyFour, 0));
		totalWeightList2.add(new DPair(WeightRange.TwentyFiveToTwentyNine, 0));
		totalWeightList2.add(new DPair(WeightRange.ThirtyPlus, 0));
		
		this.TotalWeightGivenCancelled = new Distribution(totalWeightList1);
		this.TotalWeightGivenNotCancelled = new Distribution(totalWeightList2);
		
		this.NumberPickups = new Descriptor("Number of Pickups", this.NumberPickupsGivenCancelled, this.NumberPickupsGivenNotCancelled);
		this.TotalReward = new Descriptor("Total Reward", this.TotalRewardGivenCancelled, this.TotalRewardGivenNotCancelled);
		this.TotalWeight = new Descriptor("Total Weight", this.TotalWeightGivenCancelled, this.TotalWeightGivenNotCancelled);
		
		//DEBUG
//		System.out.println(numberPickupsList);
//		System.out.println(numberPickupsList.clone());
//		
//		System.out.println(totalRewardList);
//		System.out.println(totalRewardList.clone());
//		
//		System.out.println(totalWeightList);
//		System.out.println(totalWeightList.clone());
//		
//		System.out.println(this.NumberPickupsGivenCancelled);
//		System.out.println(this.NumberPickupsGivenNotCancelled);
//		
//		System.out.println(this.NumberPickups);
//		System.out.println(this.TotalReward);
//		System.out.println(this.TotalWeight);
		
		//INTITALISING VARIABLES FOR CALCULATING DPAIRS LATER
		
		double pdenominator = 0;
		double qdenominator = 0;
		
		for (Job job : trainingSet){
			
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
			
			//DEBUG:
//			System.out.println(numberPickups);
//			System.out.println(totalReward);
//			System.out.println(totalWeight);
			
			//sort out denominators and numerators
			if(job.cancelledInTrainingSet){
				
				pdenominator++;
				
				//Number of pickups	
				if(this.NumberPickups.p.contains(numberPickups)){
					
					assert(this.NumberPickups.p.get(numberPickups).isPresent());
						
//					System.out.println("p = " + this.NumberPickups.p);
//					System.out.println("q = " + this.NumberPickups.q);
					
					this.NumberPickups.p.get(numberPickups).get().probability++;
					
//					System.out.println("p = " + this.NumberPickups.p);
//					System.out.println("q = " + this.NumberPickups.q);
				}
				
				//Total reward
				if(this.TotalReward.p.contains(totalReward)){
					
					assert(this.TotalReward.p.get(totalReward).isPresent());
						
//					System.out.println("p = " + this.TotalReward.p);
//					System.out.println("q = " + this.TotalReward.q);
					
					this.TotalReward.p.get(totalReward).get().probability++;
					
//					System.out.println("p = " + this.TotalReward.p);
//					System.out.println("q = " + this.TotalReward.q);
				}

				//Total weight
				if(this.TotalWeight.p.contains(totalWeight)){
					
					assert(this.TotalWeight.p.get(totalWeight).isPresent());
						
//					System.out.println("p = " + this.TotalWeight.p);
//					System.out.println("q = " + this.TotalWeight.q);
					
					this.TotalWeight.p.get(totalWeight).get().probability++;
					
//					System.out.println("p = " + this.TotalWeight.p);
//					System.out.println("q = " + this.TotalWeight.q);
				}
			}
			else if(!job.cancelledInTrainingSet){
				
				qdenominator++;
				
				//Number of pickups	
				if(this.NumberPickups.q.contains(numberPickups)){
					
					assert(this.NumberPickups.q.get(numberPickups).isPresent());
						
//					System.out.println("p = " + this.NumberPickups.p);
//					System.out.println("q = " + this.NumberPickups.q);
					
					this.NumberPickups.q.get(numberPickups).get().probability++;
					
//					System.out.println("p = " + this.NumberPickups.p);
//					System.out.println("q = " + this.NumberPickups.q);
				}
				
				//Total reward
				if(this.TotalReward.q.contains(totalReward)){
					
					assert(this.TotalReward.q.get(totalReward).isPresent());
					
//					System.out.println("p = " + this.TotalReward.p);
//					System.out.println("q = " + this.TotalReward.q);
					
					this.TotalReward.q.get(totalReward).get().probability++;
					
//					System.out.println("p = " + this.TotalReward.p);
//					System.out.println("q = " + this.TotalReward.q);
				}

				//Total weight
				if(this.TotalWeight.q.contains(totalWeight)){
					
					assert(this.TotalWeight.q.get(totalWeight).isPresent());
						
//					System.out.println("p = " + this.TotalWeight.p);
//					System.out.println("q = " + this.TotalWeight.q);
					
					this.TotalWeight.q.get(totalWeight).get().probability++;
					
//					System.out.println("p = " + this.TotalWeight.p);
//					System.out.println("q = " + this.TotalWeight.q);
				}
			}
			
			
		}
		
		//DEBUG:
//		System.out.println("Number of jobs cancelled: " + pdenominator);
//		System.out.println("Number of jobs not cancelled: " + qdenominator);
		
		for(int i = 0; i < this.NumberPickups.p.probabilities.size(); i++){
			
			this.NumberPickups.p.probabilities.get(i).probability /= pdenominator;
			this.NumberPickups.q.probabilities.get(i).probability /= qdenominator;
		}
		
		for(int i = 0; i < this.TotalReward.p.probabilities.size(); i++){
			
			this.TotalReward.p.probabilities.get(i).probability /= pdenominator;
			this.TotalReward.q.probabilities.get(i).probability /= qdenominator;
		}
		
		for(int i = 0; i < this.TotalWeight.p.probabilities.size(); i++){
			
			this.TotalWeight.p.probabilities.get(i).probability /= pdenominator;
			this.TotalWeight.q.probabilities.get(i).probability /= qdenominator;
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

	@Override
	public String toString(){
		
		return "Naive Bayes of: \n" + this.NumberPickups + ", \n" + this.TotalReward + ", \n" + this.TotalWeight;
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
			
			return "For Descriptor: '" + this.name + "' Distibution of: cancelled = " + this.p + " not cancelled = " + this.q;
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