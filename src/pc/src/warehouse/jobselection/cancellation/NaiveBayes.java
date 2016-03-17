package warehouse.jobselection.cancellation;

import java.util.ArrayList;
import java.util.LinkedList;

import warehouse.job.Job;

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

	private Distribution DropLocationGivenCancelled = new Distribution(null);
	private Distribution NumberPickupsGivenCancelled = new Distribution(null);
	private Distribution NumberDifferentItemsGivenCancelled = new Distribution(null);
	private Distribution TotalRewardGivenCancelled = new Distribution(null);
	private Distribution TotalWeightGivenCancelled = new Distribution(null);
	
	private Distribution DropLocationGivenNotCancelled = new Distribution(null);
	private Distribution NumberPickupsGivenNotCancelled = new Distribution(null);
	private Distribution NumberDifferentItemsGivenNotCancelled = new Distribution(null);
	private Distribution TotalRewardGivenNotCancelled = new Distribution(null);
	private Distribution TotalWeightGivenNotCancelled = new Distribution(null);
	
	private Descriptor DropLocation = new Descriptor("Drop Location", DropLocationGivenCancelled, DropLocationGivenNotCancelled);
	private Descriptor NumberPickups = new Descriptor("Number of Pickups", NumberPickupsGivenCancelled, NumberPickupsGivenNotCancelled);
	private Descriptor NumberDifferentItems = new Descriptor("Number of different Items", NumberDifferentItemsGivenCancelled, NumberDifferentItemsGivenNotCancelled);
	private Descriptor TotalReward = new Descriptor("Total Reward", TotalRewardGivenCancelled, TotalRewardGivenNotCancelled);
	private Descriptor TotalWeight = new Descriptor("Total Weight", TotalWeightGivenCancelled, TotalWeightGivenNotCancelled);
	
	/**
	 * Construct a NaiveBayes object with a training set and set up class
	 * descriptors
	 * 
	 * @param trainingSet
	 *            the set of jobs to learn from
	 */
	public NaiveBayes(LinkedList<Job> trainingSet) {

		this.trainingSet = trainingSet;
		
		//Drop Location:
		
		double pnumeratorDropLocation = 0.001;
		double qnumeratorDropLocation = 0.001;
		double pdenominatorDropLocation = 0;
		double qdenominatorDropLocation = 0;
		double pDropLocation;
		double qDropLocation;
		
		for (Job job : trainingSet){
			
			if((this.DropLocation.p.probabilities == null) || this.DropLocation.p.contains(job.dropLocation)){
				
				this.DropLocation.p.add(new DPair(job.dropLocation, -1));
				
				if(job.cancelledInTrainingSet){
					
					qdenominatorDropLocation++;
				}
				else{
					
					pdenominatorDropLocation++;
				}
			}		
		}

		for(int i = 0; i < this.DropLocation.p.probabilities.size(); i++){
			
			pDropLocation = pnumeratorDropLocation/pdenominatorDropLocation;
			qDropLocation = qnumeratorDropLocation/qdenominatorDropLocation;
			
			this.DropLocation.p.probabilities.get(i).probability = pDropLocation;
			this.DropLocation.q.probabilities.get(i).probability = qDropLocation;
		}
		
		//Number of Pickups:
			
		//Number of Different Items:
			
		//Total Reward:
			
		//Total Weight:
			
		
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

		public ArrayList<DPair> probabilities() {

			return this.probabilities;
		}
		
		public void add(DPair pair){
			
			this.probabilities.add(pair);
		}
		
		public boolean contains(Object descriptor){
			
			for(int i = 0; i < this.probabilities.size(); i++){
				
				if(this.probabilities.get(i).descriptor.equals(descriptor)){
					
					return true;
				}
			}
			
			return false;
		}
		
		public int getIndexOf(Object descriptor){
			
			for(int i = 0; i < this.probabilities.size(); i++){
				
				if(this.probabilities.get(i).descriptor.equals(descriptor)){
					
					return i;
				}
			}
			
			return -1;
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

		public String getName() {

			return this.name;
		}

		public Distribution p() {

			return this.p;
		}

		public Distribution q() {

			return this.q;
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
	}

	//DESCRIBES A LINKED OBJECT AND PROBABILITY
	
	private class DPair {

		private Object descriptor;
		private double probability;

		private DPair(Object a, double b) {

			this.descriptor = a;
			this.probability = b;
		}

		public Object descriptor() {

			return this.descriptor;
		}

		public double probability() {

			return this.probability;
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
	}
}