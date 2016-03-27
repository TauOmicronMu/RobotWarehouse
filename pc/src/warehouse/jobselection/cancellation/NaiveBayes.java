package warehouse.jobselection.cancellation;

import java.util.*;
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

	//Defining the different features and their distributions
	private Distribution NumberPickupsGivenCancelled;
	private Distribution NumberPickupsGivenNotCancelled;

	private Distribution TotalRewardGivenCancelled;
	private Distribution TotalWeightGivenCancelled;

	private Distribution TotalRewardGivenNotCancelled;
	private Distribution TotalWeightGivenNotCancelled;

	private Map<String, Feature> NumberPickups;

	private Feature TotalReward;
	private Feature TotalWeight;

	//Doubles for the probability of a job being cancelled and nor being cancelled, independantly
	private double pCancelled;
	private double qCancelled;

	/**
	 * Construct a NaiveBayes object with no explicit item name list, use the
	 * default instead
	 * 
	 * @param trainingSet
	 *            the training set of jobs
	 */
	public NaiveBayes(List<Job> trainingSet) {

		//This is the default list of jobs 
		this(trainingSet,
				new ArrayList<String>(Arrays.asList("aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ba",
						"bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "ca", "cb", "cc", "cd", "ce", "cf", "cg",
						"ch", "ci", "cj")));
	}

	/**
	 * Construct a NaiveBayes object with a training set, and create probability
	 * distributions based on this training set.
	 * 
	 * @param trainingSet
	 *            the set of jobs to learn from
	 * @param itemNames
	 *            the names of all the items
	 */
	public NaiveBayes(List<Job> trainingSet, ArrayList<String> itemNames) {

		// Create Descriptor objects for the features of the training set - this
		// is assuming that we are only using Number of Items, Total Reward and
		// Total Weight of the job
		// Each Descriptor contains two Distribution objects - one for the
		// probabilities given cancelled, and one for given not cancelled

		// setting up features for the number of pickups for each item
		this.NumberPickups = new HashMap<String, Feature>();

		for (String item : itemNames) {

			//Create arraylist of descriptor-pairs for each range enum for the items
			ArrayList<DPair> numberPickupsList1 = new ArrayList<DPair>();
			numberPickupsList1.add(new DPair(NumRange.Zero, 0));
			numberPickupsList1.add(new DPair(NumRange.One, 0));
            numberPickupsList1.add(new DPair(NumRange.Two, 0));
			numberPickupsList1.add(new DPair(NumRange.Three, 0));
            numberPickupsList1.add(new DPair(NumRange.Four, 0));
			numberPickupsList1.add(new DPair(NumRange.FivePlus, 0));

			//Create arraylist of descriptor-pairs for each range enum for the items
			ArrayList<DPair> numberPickupsList2 = new ArrayList<DPair>();
			numberPickupsList2.add(new DPair(NumRange.Zero, 0));
            numberPickupsList2.add(new DPair(NumRange.One, 0));
            numberPickupsList2.add(new DPair(NumRange.Two, 0));
            numberPickupsList2.add(new DPair(NumRange.Three, 0));
            numberPickupsList2.add(new DPair(NumRange.Four, 0));
            numberPickupsList2.add(new DPair(NumRange.FivePlus, 0));

            //Initialise the two distributions for this particular itemname
			NumberPickupsGivenCancelled = new Distribution(numberPickupsList1, 0);
			NumberPickupsGivenNotCancelled = new Distribution(numberPickupsList2, 0);

			//add them to the hashmap of item features, with the key being the itemname
			this.NumberPickups.put(item,
					new Feature(item, NumberPickupsGivenCancelled, NumberPickupsGivenNotCancelled));
		}

		//Create arraylist of descriptor-pairs for each range enum for the total rewards
		ArrayList<DPair> totalRewardList1 = new ArrayList<DPair>();
		totalRewardList1.add(new DPair(RewardRange.ZeroToThirty, 0));
		totalRewardList1.add(new DPair(RewardRange.ThirtyOneToSixty, 0));
		totalRewardList1.add(new DPair(RewardRange.SixtyOnePlus, 0));

		//Create arraylist of descriptor-pairs for each range enum for the total rewards
		ArrayList<DPair> totalRewardList2 = new ArrayList<DPair>();
		totalRewardList2.add(new DPair(RewardRange.ZeroToThirty, 0));
		totalRewardList2.add(new DPair(RewardRange.ThirtyOneToSixty, 0));
		totalRewardList2.add(new DPair(RewardRange.SixtyOnePlus, 0));

		//Initialise the two distributions for total reward
		this.TotalRewardGivenCancelled = new Distribution(totalRewardList1, 0);
		this.TotalRewardGivenNotCancelled = new Distribution(totalRewardList2, 0);

		//add them to the overall feature
		this.TotalReward = new Feature("TR", this.TotalRewardGivenCancelled, this.TotalRewardGivenNotCancelled);

		//Create arraylist of descriptor-pairs for each range enum for the total weights
		ArrayList<DPair> totalWeightList1 = new ArrayList<DPair>();
		totalWeightList1.add(new DPair(WeightRange.ZeroToThirty, 0));
		totalWeightList1.add(new DPair(WeightRange.ThirtyOneToSixty, 0));
		totalWeightList1.add(new DPair(WeightRange.SixtyOnePlus, 0));

		//Create arraylist of descriptor-pairs for each range enum for the total weights
		ArrayList<DPair> totalWeightList2 = new ArrayList<DPair>();
		totalWeightList2.add(new DPair(WeightRange.ZeroToThirty, 0));
		totalWeightList2.add(new DPair(WeightRange.ThirtyOneToSixty, 0));
		totalWeightList2.add(new DPair(WeightRange.SixtyOnePlus, 0));

		//Initialise the two distributions for total weight
		this.TotalWeightGivenCancelled = new Distribution(totalWeightList1, 0);
		this.TotalWeightGivenNotCancelled = new Distribution(totalWeightList2, 0);

		//add them to the overall feature
		this.TotalWeight = new Feature("TW", this.TotalWeightGivenCancelled, this.TotalWeightGivenNotCancelled);

		// Initialising a few variables for later so the probability of a job
		// being cancelled overall, and not being cancelled, can be used later
		// when working out the probability that an individual job will be
		// cancelled

		double numJobs = 0;
		double numCancelled = 0;
		double numNotCancelled = 0;

		for (Job job : trainingSet) {

			//Increment the total number of jobs
			numJobs++;

			//Initialise variables for later
			String itemName = "";
			int itemNumber = 0;
			double doubleReward = 0;
			double doubleWeight = 0;

			for (ItemPickup pickup : job.pickups) {

				//Get the name and count of the item
				itemName = pickup.itemName;
				itemNumber = pickup.itemCount;

				//Check that the hashmap contains a feature for this item (it should!)
				assert (itemName != null);
				assert (itemNumber > 0);
				assert (this.NumberPickups.containsKey(itemName));

				//Convert the count of the item into a range enum
				NumRange numberPickups = this.convertNumber(itemNumber);
				
				//Check that it worked
				assert (numberPickups != NumRange.Error);

				// Increment the probabilities of number of pickups for each
				// item in the job if it was cancelled
				if ((job.cancelledInTrainingSet) && this.NumberPickups.get(itemName).p.contains(numberPickups)) {

					//Make sure the hashmap contains values for the item
					assert (this.NumberPickups.get(itemName).p.get(numberPickups).isPresent());

					//Increment the relevant numbers so the total probability can be calculated later
					this.NumberPickups.get(itemName).p.get(numberPickups).get().probability++;
					this.NumberPickups.get(itemName).p.denominator++;

					//DEBUG
//					System.out.println("P - Incrementing probability of " + itemName + " in " + numberPickups + ", now = " +
//							this.NumberPickups.get(itemName).p.get(numberPickups).get().probability);
				}

				// Increment the probabilities of number of pickups for each
				// item in the job if it was not cancelled
				else if ((!job.cancelledInTrainingSet) && this.NumberPickups.get(itemName).q.contains(numberPickups)) {

					//Make sure the hashmap contains values for the item
					assert (this.NumberPickups.get(itemName).q.get(numberPickups).isPresent());

					//Increment the relevant numbers so the total probability can be calculated later
					this.NumberPickups.get(itemName).q.get(numberPickups).get().probability ++;
					this.NumberPickups.get(itemName).q.denominator++;

					//DEBUG
//					System.out.println("Q - Incrementing probability of " + itemName + " in " + numberPickups + ", now = " +
//							this.NumberPickups.get(itemName).q.get(numberPickups).get().probability);
				}

				// Increase the total reward and weight counts
				doubleReward += (pickup.reward * pickup.itemCount);
				doubleWeight += (pickup.weight * pickup.itemCount);
			}

			// Convert the raw numbers from the job into general range enums so
			// they can be compared against easily later
			RewardRange totalReward = this.convertReward(doubleReward);
			WeightRange totalWeight = this.convertWeight(doubleWeight);

			//Check that the conversion worked
			assert (totalReward != RewardRange.Error);
			assert (totalWeight != WeightRange.Error);

			// If the job was cancelled, increment the numerators of the
			// probabilities in that distribution by one
			if (job.cancelledInTrainingSet) {

				//Increment the total number of cancelled jobs and the denominators for totalreward and totalweight
				numCancelled++;
				this.TotalReward.p.denominator++;
				this.TotalWeight.p.denominator++;

				// Increment the probabilities for the other two features, total
				// reward and total weight given the job was cancelled
				this.pincrementProbabilities(totalReward, totalWeight);

			}
			// If the job was not cancelled, increment the numerators of the
			// probabilities in the alternate distribution by one
			else if (!job.cancelledInTrainingSet) {

				//Increment the total number of not cancelled jobs and the denominators for totalreward and totalweight
				numNotCancelled++;
				this.TotalReward.q.denominator++;
				this.TotalWeight.q.denominator++;

				// Increment the probabilities for the other two features, total
				// reward and total weight given the job was not cancelled
				this.qincrementProbabilities(totalReward, totalWeight);
			}

		}

		// Calculate the overall probabilities that a job will or will not be
		// cancelled
		pCancelled = numCancelled / numJobs;
		qCancelled = numNotCancelled / numJobs;

		// Divide the values currently in the distributions by the correct
		// denominator
		this.denominate();
		
		//At this point, the Naive Bayes has finished being set up with values for each feature
	}

	/**
	 * Convert an int into a NumRange enum
	 * 
	 * @param intPickups
	 *            the int
	 * @return the appropriate enum
	 */
	private NumRange convertNumber(int intPickups) {

		// If this reaches the end, something went wrong (in regards to the
		// cancellation machine anyway)
		NumRange numberPickups = NumRange.Error;

		//Check which range enum the int falls into and set the return as that
		if(intPickups == 0){

			numberPickups = NumRange.Zero;
		}else if (intPickups == 1){

			numberPickups = NumRange.One;
		} else if (intPickups == 2){

			numberPickups = NumRange.Two;
		}else if (intPickups == 3) {

			numberPickups = NumRange.Three;
		}else if (intPickups == 4) {

			numberPickups = NumRange.Four;
		}else if (intPickups >= 5) {

			numberPickups = NumRange.FivePlus;
		}
		return numberPickups;
	}

	/**
	 * Convert a double into a RewardRange enum
	 * 
	 * @param doubleReward
	 *            the double
	 * @return the appropriate enum
	 */
	private RewardRange convertReward(double doubleReward) {

		// If this reaches the end, something went wrong (in regards to the
		// cancellation machine anyway)
		RewardRange totalReward = RewardRange.Error;

		//Check which range enum the double falls into and set the return as that
		if ((0 <= doubleReward) && (doubleReward <= 30)) {

			totalReward = RewardRange.ZeroToThirty;
		} else if ((30 < doubleReward) && (doubleReward <= 60)) {

			totalReward = RewardRange.ThirtyOneToSixty;
		} else if (60 < doubleReward) {

			totalReward = RewardRange.SixtyOnePlus;
		}

		return totalReward;
	}

	/**
	 * Convert a double into a WeightRange enum
	 * 
	 * @param doubleWeight
	 *            the double
	 * @return the appropriate enum
	 */
	private WeightRange convertWeight(double doubleWeight) {

		// If this reaches the end, something went wrong (in regards to the
		// cancellation machine anyway)
		WeightRange totalWeight = WeightRange.Error;

		//Check which range enum the double falls into and set the return as that
		if ((0 <= doubleWeight) && (doubleWeight <= 30)) {

			totalWeight = WeightRange.ZeroToThirty;
		} else if ((30 < doubleWeight) && (doubleWeight <= 60)) {

			totalWeight = WeightRange.ThirtyOneToSixty;
		} else if (60 < doubleWeight) {

			totalWeight = WeightRange.SixtyOnePlus;
		}

		return totalWeight;
	}

	/**
	 * Increment the probabilities of a distribution based on the enums a job
	 * has (when a job was cancelled)
	 * 
	 * @param totalReward
	 *            the enum for the total reward
	 * @param totalWeight
	 *            the enum for the total weight
	 */
	private void pincrementProbabilities(RewardRange totalReward, WeightRange totalWeight) {

		// Total reward
		if (this.TotalReward.p.contains(totalReward)) {

			//Check that there are values for it in the Naive Bayes
			assert (this.TotalReward.p.get(totalReward).isPresent());

			//Increment the probability of the total reward
			this.TotalReward.p.get(totalReward).get().probability++;
		}

		// Total weight
		if (this.TotalWeight.p.contains(totalWeight)) {

			//Check that there are values for it in the Naive Bayes
			assert (this.TotalWeight.p.get(totalWeight).isPresent());

			//Increment the probability of the total weight
			this.TotalWeight.p.get(totalWeight).get().probability++;
		}
	}

	/**
	 * Increment the probabilities of a distribution based on the enums a job
	 * has (when a job was not cancelled)
	 * 
	 * @param totalReward
	 *            the enum for the total reward
	 * @param totalWeight
	 *            the enum for the total weight
	 */
	private void qincrementProbabilities(RewardRange totalReward, WeightRange totalWeight) {

		// Total reward
		if (this.TotalReward.q.contains(totalReward)) {

			//Check that there are values for it in the Naive Bayes
			assert (this.TotalReward.q.get(totalReward).isPresent());

			//Increment the probability of the total reward
			this.TotalReward.q.get(totalReward).get().probability++;
		}

		// Total weight
		if (this.TotalWeight.q.contains(totalWeight)) {

			//Check that there are values for it in the Naive Bayes
			assert (this.TotalWeight.q.get(totalWeight).isPresent());

			//Increment the probability of the total weight
			this.TotalWeight.q.get(totalWeight).get().probability++;
		}
	}

	/**
	 * Divide the probabilities in the distributions by the calculated
	 * denominators for p (cancelled) and q (not cancelled)
	 */
	private void denominate() {

		//for each feature in the map, divide the probability by the final denominator we calculated
		for(Map.Entry<String, Feature> feature : this.NumberPickups.entrySet()){

			for (int i = 0; i < feature.getValue().p.probabilities.size(); i++) {

				if(feature.getValue().p.probabilities.get(i).probability > 0){

					//DEBUG
//					System.out.println(feature.getValue().p.probabilities.get(i).probability + "/" + feature.getValue().p.denominator);
//					System.out.println((feature.getValue().p.probabilities.get(i).probability > 0) && (feature.getValue().p.denominator == 0));

					feature.getValue().p.probabilities.get(i).probability /= feature.getValue().p.denominator;
				}

				if(feature.getValue().q.probabilities.get(i).probability > 0) {

					//DEBUG
//					System.out.println(feature.getValue().q.probabilities.get(i).probability + "/" + feature.getValue().q.denominator);
//					System.out.println((feature.getValue().q.probabilities.get(i).probability > 0) && (feature.getValue().q.denominator == 0));

					feature.getValue().q.probabilities.get(i).probability /= feature.getValue().q.denominator;
				}
			}
		}

		//for each value in the feature, divide the probability by the final denominator we calculated
		for (int i = 0; i < this.TotalReward.p.probabilities.size(); i++) {

            if(this.TotalReward.p.probabilities.get(i).probability > 0) {
                this.TotalReward.p.probabilities.get(i).probability /= this.TotalReward.p.denominator;
            }

            if(this.TotalReward.q.probabilities.get(i).probability > 0) {
                this.TotalReward.q.probabilities.get(i).probability /= this.TotalReward.q.denominator;
            }
        }

		//for each value in the feature, divide the probability by the final denominator we calculated
		for (int i = 0; i < this.TotalWeight.p.probabilities.size(); i++) {

            if(this.TotalWeight.p.probabilities.get(i).probability > 0) {
                this.TotalWeight.p.probabilities.get(i).probability /= this.TotalWeight.p.denominator;
            }

            if(this.TotalWeight.p.probabilities.get(i).probability > 0) {
                this.TotalWeight.q.probabilities.get(i).probability /= this.TotalWeight.q.denominator;
            }
        }
	}

	/**
	 * Get the probability that a job had this enum given it was cancelled
	 * 
	 * @param name
	 *            the item name
	 * @param n
	 *            the enum
	 * @return the probability as a double
	 */
	private double pgetProbability(String name, NumRange n) {

		//Check that the feature contains this value
		assert (this.NumberPickups.get(name).p.get(n).isPresent());

		//Get the value, or 0.0001 if the value is 0
		return this.NumberPickups.get(name).p.get(n).get().probability == 0 ? 0.0001
				: this.NumberPickups.get(name).p.get(n).get().probability;
	}

	/**
	 * Get the probability that a job had this enum given it was cancelled
	 * 
	 * @param r
	 *            the enum
	 * @return the probability as a double
	 */
	private double pgetProbability(RewardRange r) {

		//Check that the feature contains this value
		assert (this.TotalReward.p.get(r).isPresent());

		//Get the value, or 0.0001 if the value is 0
		return this.TotalReward.p.get(r).get().probability == 0 ? 0.0001 : this.TotalReward.p.get(r).get().probability;
	}

	/**
	 * Get the probability that a job had this enum given it was cancelled
	 * 
	 * @param w
	 *            the enum
	 * @return the probability as a double
	 */
	private double pgetProbability(WeightRange w) {

		//Check that the feature contains this value
		assert (this.TotalWeight.p.get(w).isPresent());

		//Get the value, or 0.0001 if the value is 0
		return this.TotalWeight.p.get(w).get().probability == 0 ? 0.0001 : this.TotalWeight.p.get(w).get().probability;
	}

	/**
	 * Get the probability that a job had this enum given it was not cancelled
	 * 
	 * @param name
	 *            the name of the item
	 * @param n
	 *            the enum
	 * @return the probability as a double
	 */
	private double qgetProbability(String name, NumRange n) {

		//Check that the feature contains this value
		assert (this.NumberPickups.get(name).q.get(n).isPresent());

		//Get the value, or 0.0001 if the value is 0
		return this.NumberPickups.get(name).q.get(n).get().probability == 0 ? 0.0001
				: this.NumberPickups.get(name).q.get(n).get().probability;
	}

	/**
	 * Get the probability that a job had this enum given it was not cancelled
	 * 
	 * @param r
	 *            the enum
	 * @return the probability as a double
	 */
	private double qgetProbability(RewardRange r) {

		//Check that the feature contains this value
		assert (this.TotalReward.q.get(r).isPresent());

		//Get the value, or 0.0001 if the value is 0
		return this.TotalReward.q.get(r).get().probability == 0 ? 0.0001 : this.TotalReward.q.get(r).get().probability;
	}

	/**
	 * Get the probability that a job had this enum given it was not cancelled
	 * 
	 * @param w
	 *            the enum
	 * @return the probability as a double
	 */
	private double qgetProbability(WeightRange w) {

		//Check that the feature contains this value
		assert (this.TotalWeight.q.get(w).isPresent());

		//Get the value, or 0.0001 if the value is 0
		return this.TotalWeight.q.get(w).get().probability == 0 ? 0.0001 : this.TotalWeight.q.get(w).get().probability;
	}

	/**
	 * See interface.
	 */
	@Override
	public double getProbability(Job job) {

		/*
		 * The equation I am working from: CANCELLEDPROBABILITY =
		 * p(JOBCANCELLED) * ([sigmaproduct from 0 to NUMBER OF DESCRIPTORS] of
		 * p(DESCRIPTOR|JOBCANCELLED))
		 * 
		 */

		double doubleReward = 0;
		double doubleWeight = 0;

		double p = this.pCancelled;
		double q = this.qCancelled;

		//DEBUG
		//System.out.println("Starting with chances of p = " + p + " q = " + q);

		//HashMap<String, Integer> jobItemNames = new HashMap<>();

		//For each itempickup in the job, multiple the probabilities by the relevant value in the classifier
		for (ItemPickup pickup : job.pickups) {

			//Convert the item count into a range enum
			NumRange numberPickups = this.convertNumber(pickup.itemCount);

			//Multiply the existing probabilities by those for the next item
			p *= this.pgetProbability(pickup.itemName, numberPickups);
			q *= this.qgetProbability(pickup.itemName, numberPickups);

			//DEBUG
//			System.out.println("For item " + pickup.itemName);
//			System.out.println("p * " + this.pgetProbability(pickup.itemName, numberPickups) + ", p = " + p);
//			System.out.println("q * " + this.qgetProbability(pickup.itemName, numberPickups) + ", q = " + q);

			//jobItemNames.put(pickup.itemName ,pickup.itemCount);

			// Increase this jobs total reward and weight values
			doubleReward += (pickup.reward * pickup.itemCount);
			doubleWeight += (pickup.weight * pickup.itemCount);
		}

		//DEBUG
//		for(Map.Entry<String, Feature> item : this.NumberPickups.entrySet()) {
//
//			NumRange numberPickups = NumRange.Error;
//
//			if (jobItemNames.containsKey(item.getKey())) {
//
//				// Convert the amount of the item into an enum to be compared later
//				numberPickups = this.convertNumber(jobItemNames.get(item.getKey()));
//
//			} else {
//
//				numberPickups = NumRange.Zero;
//			}
//
//
//			// Check the conversion worked (we got a usable value)
//			assert (numberPickups != NumRange.Error);
//
//			// Multiply the probabilities by the appropriate values for the
//			// items in this pickup in the range it contains
//			p *= this.pgetProbability(item.getKey(), numberPickups);
//			q *= this.qgetProbability(item.getKey(), numberPickups);
//
//			System.out.println("p * " + this.pgetProbability(item.getKey(), numberPickups));
//			System.out.println("q * " + this.qgetProbability(item.getKey(), numberPickups));
//		}

		//System.out.println("Considered items: p = " + p + " q = " + q);

		// Convert the jobs fields into the appropriate enum values
		RewardRange totalReward = this.convertReward(doubleReward);
		WeightRange totalWeight = this.convertWeight(doubleWeight);

		// Check that the conversion worked (we got a usable value)
		assert (totalReward != RewardRange.Error);
		assert (totalWeight != WeightRange.Error);

		// Find probability from each enum that matches and use them to
		// calculate the pre - normalised probabilities that the job will or
		// will not be cancelled
		p *= this.pgetProbability(totalReward) * this.pgetProbability(totalWeight);
		q *= this.qgetProbability(totalReward) * this.qgetProbability(totalWeight);

		//DEBUG
//		System.out.println("Considered reward/weight: p * " + this.pgetProbability(totalReward) + " * " + this.pgetProbability(totalWeight)
//				+ ", q * " + this.qgetProbability(totalReward) + " * " + this.qgetProbability(totalWeight));

		// Normalise the probabilities so the actual probability that this job
		// will be cancelled can be returned
		
		//DEBUG
//		System.out.println("\np = " + p + " q = " + q);
		
		//normalise the values obtained, and thereby get the final probability of cancellation
		double finalProbability = this.pnormalisedProbabilityOfCancellation(p, q);

		//DEBUG
//		System.out.println("\nfinal q = " + this.qnormalisedProbabilityOfCancellation(p, q));
		
		// Check that it's a valid probability to avoid getting wrong numbers in
		// the main job selection code
		assert ((finalProbability >= 0) && (finalProbability <= 1));

		// Return the final probability calculated for the job being cancelled
		return finalProbability;
	}

	/**
	 * To String method for debugging
	 */
	@Override
	public String toString() {

		return "Naive Bayes of: \n" + this.NumberPickups + ", \n" + this.TotalReward + ", \n" + this.TotalWeight;
	}

	/**
	 * Normalise two probabilities and return the normalised p value (as that's
	 * all we need for the cancellation machine)
	 * 
	 * @param p
	 *            the pre - normalisation p
	 * @param q
	 *            the pre - normalisation q
	 * @return the normalised p value as a double
	 */
	private double pnormalisedProbabilityOfCancellation(double p, double q) {

		//normalise the two probabilities and return the p
		double total = p + q;

		double multiplier = 1 / total;

		double result = p * multiplier;

		return result;
	}

	@Override
	public boolean equals(Object o){
		
		if(!(o instanceof NaiveBayes)){
			
			return false;
		}

		NaiveBayes n = (NaiveBayes)o;
		
		for(Map.Entry<String, Feature> thisEntry : this.NumberPickups.entrySet()){
			
			Feature nEntry = n.NumberPickups.get(thisEntry.getKey());
			
			for(int i = 0; i < thisEntry.getValue().p.probabilities.size(); i++){
				
				if(!(((thisEntry.getValue().p.probabilities.get(i).descriptor.equals(nEntry.p.probabilities.get(i).descriptor))
						&& (thisEntry.getValue().p.probabilities.get(i).probability == nEntry.p.probabilities.get(i).probability)))){
					
					return false;
				}
			}
			
			for(int i = 0; i < thisEntry.getValue().q.probabilities.size(); i++){
				
				if(!(((thisEntry.getValue().q.probabilities.get(i).descriptor.equals(nEntry.q.probabilities.get(i).descriptor))
						&& (thisEntry.getValue().q.probabilities.get(i).probability == nEntry.q.probabilities.get(i).probability)))){
					
					return false;
				}
			}
		}
		
		for(int i = 0; i < this.TotalReward.p.probabilities.size(); i++){
			
			if(!(((TotalReward.p.probabilities.get(i).descriptor.equals(n.TotalReward.p.probabilities.get(i).descriptor))
					&& (TotalReward.p.probabilities.get(i).probability == n.TotalReward.p.probabilities.get(i).probability)))){
				
				return false;
			}
			
			if(!(((TotalReward.q.probabilities.get(i).descriptor.equals(n.TotalReward.q.probabilities.get(i).descriptor))
					&& (TotalReward.q.probabilities.get(i).probability == n.TotalReward.q.probabilities.get(i).probability)))){
				
				return false;
			}
		}
		
		for(int i = 0; i < this.TotalWeight.p.probabilities.size(); i++){
			
			if(!(((TotalWeight.p.probabilities.get(i).descriptor.equals(n.TotalWeight.p.probabilities.get(i).descriptor))
					&& (TotalWeight.p.probabilities.get(i).probability == n.TotalWeight.p.probabilities.get(i).probability)))){
				
				return false;
			}
			
			if(!(((TotalWeight.q.probabilities.get(i).descriptor.equals(n.TotalWeight.q.probabilities.get(i).descriptor))
					&& (TotalWeight.q.probabilities.get(i).probability == n.TotalWeight.q.probabilities.get(i).probability)))){
				
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int hashCode(){
		
		return 0;
	}
	
	// DESCRIBES AN ARRAY OF DPAIRS

	/**
	 * Class that describes an arraylist of probability pairs
	 * 
	 * @author Owen
	 *
	 */
	private class Distribution {

		private ArrayList<DPair> probabilities;
		public int denominator;

		/**
		 * Create a new Distribution based on an arraylist of pairs
		 * 
		 * @param set
		 *            the arraylist of pairs
		 */
		private Distribution(ArrayList<DPair> set, int denominator) {

			this.probabilities = set;
			this.denominator = denominator;
		}

		/**
		 * Check if this Distribution contains a pair wit the same descriptor
		 * 
		 * @param descriptor
		 *            the descriptor to check
		 * @return true if it contains a pair with that descriptor, else false
		 */
		public boolean contains(Object descriptor) {

			for (int i = 0; i < this.probabilities.size(); i++) {

				if (this.probabilities.get(i).descriptor.equals(descriptor)) {

					return true;
				}
			}

			return false;
		}

		/**
		 * Get the pair related to a descriptor
		 * 
		 * @param descriptor
		 *            the descriptor
		 * @return an Optional of the pair if it exists, or an empty Optional
		 */
		public Optional<DPair> get(Object descriptor) {

			for (int i = 0; i < this.probabilities.size(); i++) {

				if (this.probabilities.get(i).descriptor.equals(descriptor)) {

					return Optional.of(this.probabilities.get(i));
				}
			}

			return Optional.empty();
		}

		@Override
		public boolean equals(Object o) {

			if ((o instanceof Distribution) && (((Distribution) o).probabilities.equals(this.probabilities))) {

				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {
			
			int hashCode = 0;
			
			for(DPair pair : this.probabilities){
				
				hashCode += pair.hashCode();
			}
			
			return hashCode;
		}

		public String toString() {

			String s = "";

			for (DPair pair : this.probabilities) {

				s += pair.toString() + ", ";
			}

			return "{" + s + "}";
		}
	}

	// DESCRIBES SOMETHING WE CAN CATEGORISE A JOB BY

	/**
	 * Class that describes a feature of a job, with a p distribution (for
	 * cancelled) and a q distribution (for not cancelled)
	 * 
	 * @author Owen
	 *
	 */
	private class Feature {

		private String name;
		private Distribution p;
		private Distribution q;

		/**
		 * Create a new Feature, with a name for debugging and two distributions
		 * 
		 * @param name
		 *            the name of the Feature
		 * @param p
		 *            the distribution for cancelled
		 * @param q
		 *            the distribution for not cancelled
		 */
		private Feature(String name, Distribution p, Distribution q) {

			this.name = name;
			this.p = p;
			this.q = q;
		}

		@Override
		public boolean equals(Object o) {

			if ((o instanceof Feature) && ((Feature) o).name.equals(this.name)) {

				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {

			return this.p.hashCode() + this.q.hashCode();
		}

		/**
		 * To String method for debugging
		 */
		public String toString() {

			return "For Feature: '" + this.name + "' Distibution of: cancelled = " + this.p + " not cancelled = "
					+ this.q;
		}
	}

	// DESCRIBES A LINKED OBJECT AND PROBABILITY

	/**
	 * Class that describes a pair of an object descriptor and a double
	 * 
	 * @author Owen
	 *
	 */
	private class DPair {

		private Object descriptor;
		public double probability;

		/**
		 * Create a new DPair
		 * 
		 * @param a
		 *            the object
		 * @param b
		 *            the double
		 */
		private DPair(Object a, double b) {

			this.descriptor = a;
			this.probability = b;
		}

		@Override
		public boolean equals(Object o) {

			if ((o instanceof DPair) && ((DPair) o).descriptor.equals(this.descriptor)) {

				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {

			if(!(this.descriptor instanceof String)){
			
				return 0;
			}
			
			return hash((String)this.descriptor, (int)this.probability);
		}

		/**
		 * To String method for debugging
		 */
		public String toString() {

			return "[" + this.descriptor.toString() + " p = " + this.probability + "]";
		}
	}

	// METHOD FOR HASHING

	/**
	 * Method for generating a hash for a hashmap based on a string and a number
	 * to modulus by
	 * 
	 * @param string
	 *            the string to base the hashcode on
	 * @return the hashcode generated
	 */
	public static int hash(String string, int mod) {

		int stringValue = 0;

		int size = string.length();

		for (int i = 0; i < size; i++) {

			char c = string.toLowerCase().charAt(i);

			switchCase: switch (c) {

			case ('a'):
				stringValue += 0;
				break switchCase;
			case ('b'):
				stringValue += 1;
				break switchCase;
			case ('c'):
				stringValue += 2;
				break switchCase;
			case ('d'):
				stringValue += 3;
				break switchCase;
			case ('e'):
				stringValue += 4;
				break switchCase;
			case ('f'):
				stringValue += 5;
				break switchCase;
			case ('g'):
				stringValue += 6;
				break switchCase;
			case ('h'):
				stringValue += 7;
				break switchCase;
			case ('i'):
				stringValue += 8;
				break switchCase;
			case ('j'):
				stringValue += 9;
				break switchCase;
			case ('k'):
				stringValue += 10;
				break switchCase;
			case ('l'):
				stringValue += 11;
				break switchCase;
			case ('m'):
				stringValue += 12;
				break switchCase;
			case ('n'):
				stringValue += 13;
				break switchCase;
			case ('o'):
				stringValue += 14;
				break switchCase;
			case ('p'):
				stringValue += 15;
				break switchCase;
			case ('q'):
				stringValue += 16;
				break switchCase;
			case ('r'):
				stringValue += 17;
				break switchCase;
			case ('s'):
				stringValue += 18;
				break switchCase;
			case ('t'):
				stringValue += 19;
				break switchCase;
			case ('u'):
				stringValue += 20;
				break switchCase;
			case ('v'):
				stringValue += 21;
				break switchCase;
			case ('w'):
				stringValue += 22;
				break switchCase;
			case ('x'):
				stringValue += 23;
				break switchCase;
			case ('y'):
				stringValue += 24;
				break switchCase;
			case ('z'):
				stringValue += 25;
				break switchCase;
			}
		}

		return stringValue % mod;
	}
}