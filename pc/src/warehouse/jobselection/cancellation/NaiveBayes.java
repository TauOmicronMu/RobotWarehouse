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

	private Distribution NumberPickupsGivenCancelled;
	private Distribution NumberPickupsGivenNotCancelled;

	private Distribution TotalRewardGivenCancelled;
	private Distribution TotalWeightGivenCancelled;

	private Distribution TotalRewardGivenNotCancelled;
	private Distribution TotalWeightGivenNotCancelled;

	private Map<String, Feature> NumberPickups;

	private Feature TotalReward;
	private Feature TotalWeight;

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

		// TODO Parse Items.csv from JobInput for this list!
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

		// number of pickups for each item

		this.NumberPickups = new HashMap<String, Feature>();

		for (String item : itemNames) {

			ArrayList<DPair> numberPickupsList1 = new ArrayList<DPair>();
			numberPickupsList1.add(new DPair(NumRange.Zero, 0));
			numberPickupsList1.add(new DPair(NumRange.OneToTwo, 0));
			numberPickupsList1.add(new DPair(NumRange.ThreeToFour, 0));
			numberPickupsList1.add(new DPair(NumRange.FivePlus, 0));

			ArrayList<DPair> numberPickupsList2 = new ArrayList<DPair>();
			numberPickupsList2.add(new DPair(NumRange.Zero, 0));
			numberPickupsList2.add(new DPair(NumRange.OneToTwo, 0));
			numberPickupsList2.add(new DPair(NumRange.ThreeToFour, 0));
			numberPickupsList2.add(new DPair(NumRange.FivePlus, 0));

			NumberPickupsGivenCancelled = new Distribution(numberPickupsList1);
			NumberPickupsGivenNotCancelled = new Distribution(numberPickupsList2);

			this.NumberPickups.put(item,
					new Feature(item, NumberPickupsGivenCancelled, NumberPickupsGivenNotCancelled));
		}

		// total reward
		ArrayList<DPair> totalRewardList1 = new ArrayList<DPair>();
		totalRewardList1.add(new DPair(RewardRange.ZeroToForty, 0));
		totalRewardList1.add(new DPair(RewardRange.FortyOneToEighty, 0));
		totalRewardList1.add(new DPair(RewardRange.EightyOnePlus, 0));

		ArrayList<DPair> totalRewardList2 = new ArrayList<DPair>();
		totalRewardList2.add(new DPair(RewardRange.ZeroToForty, 0));
		totalRewardList2.add(new DPair(RewardRange.FortyOneToEighty, 0));
		totalRewardList2.add(new DPair(RewardRange.EightyOnePlus, 0));

		this.TotalRewardGivenCancelled = new Distribution(totalRewardList1);
		this.TotalRewardGivenNotCancelled = new Distribution(totalRewardList2);

		this.TotalReward = new Feature("TR", this.TotalRewardGivenCancelled, this.TotalRewardGivenNotCancelled);

		// total weight
		ArrayList<DPair> totalWeightList1 = new ArrayList<DPair>();
		totalWeightList1.add(new DPair(WeightRange.ZeroToForty, 0));
		totalWeightList1.add(new DPair(WeightRange.FortyOneToEighty, 0));
		totalWeightList1.add(new DPair(WeightRange.EightyOnePlus, 0));

		ArrayList<DPair> totalWeightList2 = new ArrayList<DPair>();
		totalWeightList2.add(new DPair(WeightRange.ZeroToForty, 0));
		totalWeightList2.add(new DPair(WeightRange.FortyOneToEighty, 0));
		totalWeightList2.add(new DPair(WeightRange.EightyOnePlus, 0));

		this.TotalWeightGivenCancelled = new Distribution(totalWeightList1);
		this.TotalWeightGivenNotCancelled = new Distribution(totalWeightList2);

		this.TotalWeight = new Feature("TW", this.TotalWeightGivenCancelled, this.TotalWeightGivenNotCancelled);

		// Initialising a few variables for later so the probability of a job
		// being cancelled overall, and not being cancelled, can be used later
		// when working out the probability that an individual job will be
		// cancelled

		double pdenominator = 0;
		double qdenominator = 0;

		double numJobs = 0;
		double numCancelled = 0;
		double numNotCancelled = 0;

		for (Job job : trainingSet) {

			numJobs++;

			String itemName = "";
			int itemNumber = 0;
			double doubleReward = 0;
			double doubleWeight = 0;

			for (ItemPickup pickup : job.pickups) {

				itemName = pickup.itemName;
				itemNumber = pickup.itemCount;

				assert (itemName != null);
				assert (itemNumber > 0);

				assert (this.NumberPickups.containsKey(itemName));

				NumRange numberPickups = this.convertNumber(itemNumber);
				assert (numberPickups != NumRange.Error);

				// Increment the probabilities of number of pickups for each
				// item in the job if it was cancelled
				if ((job.cancelledInTrainingSet) && this.NumberPickups.get(itemName).p.contains(numberPickups)) {

					assert (this.NumberPickups.get(itemName).p.get(numberPickups).isPresent());

					this.NumberPickups.get(itemName).p.get(numberPickups).get().probability++;
				}

				// Increment the probabilities of number of pickups for each
				// item in the job if it was not cancelled
				else if ((!job.cancelledInTrainingSet) && this.NumberPickups.get(itemName).q.contains(numberPickups)) {

					assert (this.NumberPickups.get(itemName).q.get(numberPickups).isPresent());

					this.NumberPickups.get(itemName).q.get(numberPickups).get().probability++;
				}

				// Increase the total reward and weight counts
				doubleReward += (pickup.reward * pickup.itemCount);
				doubleWeight += (pickup.weight * pickup.itemCount);
			}

			// Convert the raw numbers from the job into general range enums so
			// they can be compared against easily later
			RewardRange totalReward = this.convertReward(doubleReward);
			WeightRange totalWeight = this.convertWeight(doubleWeight);

			assert (totalReward != RewardRange.Error);
			assert (totalWeight != WeightRange.Error);

			// If the job was cancelled, increment the numerators of the
			// probabilities in that distribution by one
			if (job.cancelledInTrainingSet) {

				numCancelled++;
				pdenominator++;

				// Increment the probabilities for the other two features, total
				// reward and total weight given the job was cancelled
				this.pincrementProbabilities(totalReward, totalWeight);

			}
			// If the job was not cancelled, increment the numerators of the
			// probabilities in the alternate distribution by one
			else if (!job.cancelledInTrainingSet) {

				numNotCancelled++;
				qdenominator++;

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
		this.denominate(pdenominator, qdenominator);
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

		if(intPickups == 0){

			numberPickups = NumRange.Zero;
		}else if ((intPickups >= 1) && (intPickups <= 2)) {

			numberPickups = NumRange.OneToTwo;
		} else if ((intPickups >= 3) && (intPickups <= 4)) {

			numberPickups = NumRange.ThreeToFour;
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

		if ((0 <= doubleReward) && (doubleReward <= 40)) {

			totalReward = RewardRange.ZeroToForty;
		} else if ((40 < doubleReward) && (doubleReward <= 80)) {

			totalReward = RewardRange.FortyOneToEighty;
		} else if (80 < doubleReward) {

			totalReward = RewardRange.EightyOnePlus;
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

		if ((0 <= doubleWeight) && (doubleWeight <= 40)) {

			totalWeight = WeightRange.ZeroToForty;
		} else if ((40 < doubleWeight) && (doubleWeight <= 80)) {

			totalWeight = WeightRange.FortyOneToEighty;
		} else if (80 < doubleWeight) {

			totalWeight = WeightRange.EightyOnePlus;
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

			assert (this.TotalReward.p.get(totalReward).isPresent());

			this.TotalReward.p.get(totalReward).get().probability++;
		}

		// Total weight
		if (this.TotalWeight.p.contains(totalWeight)) {

			assert (this.TotalWeight.p.get(totalWeight).isPresent());

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

			assert (this.TotalReward.q.get(totalReward).isPresent());

			this.TotalReward.q.get(totalReward).get().probability++;
		}

		// Total weight
		if (this.TotalWeight.q.contains(totalWeight)) {

			assert (this.TotalWeight.q.get(totalWeight).isPresent());

			this.TotalWeight.q.get(totalWeight).get().probability++;
		}
	}

	/**
	 * Divide the probabilities in the distributions by the calculated
	 * denominators for p (cancelled) and q (not cancelled)
	 * 
	 * @param pdenom
	 *            the denominator for p
	 * @param qdenom
	 *            the denominator for q
	 */
	private void denominate(double pdenom, double qdenom) {

		for (Feature feature : this.NumberPickups.values()) {

			for (int i = 0; i < feature.p.probabilities.size(); i++) {

				feature.p.probabilities.get(i).probability /= pdenom;
				feature.q.probabilities.get(i).probability /= qdenom;
			}
		}

		for (int i = 0; i < this.TotalReward.p.probabilities.size(); i++) {

			this.TotalReward.p.probabilities.get(i).probability /= pdenom;
			this.TotalReward.q.probabilities.get(i).probability /= qdenom;
		}

		for (int i = 0; i < this.TotalWeight.p.probabilities.size(); i++) {

			this.TotalWeight.p.probabilities.get(i).probability /= pdenom;
			this.TotalWeight.q.probabilities.get(i).probability /= qdenom;
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

		assert (this.NumberPickups.get(name).p.get(n).isPresent());

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

		assert (this.TotalReward.p.get(r).isPresent());

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

		assert (this.TotalWeight.p.get(w).isPresent());

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

		assert (this.NumberPickups.get(name).q.get(n).isPresent());

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

		assert (this.TotalReward.q.get(r).isPresent());

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

		assert (this.TotalWeight.q.get(w).isPresent());

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

		System.out.println("Starting with chances of p = " + p + " q = " + q);

		//HashMap<String, Integer> jobItemNames = new HashMap<>();

		for (ItemPickup pickup : job.pickups) {

			NumRange numberPickups = this.convertNumber(pickup.itemCount);

			p *= this.pgetProbability(pickup.itemName, numberPickups);
			q *= this.qgetProbability(pickup.itemName, numberPickups);

			System.out.println("For item " + pickup.itemName);
			System.out.println("p * " + this.pgetProbability(pickup.itemName, numberPickups) + ", p = " + p);
			System.out.println("q * " + this.qgetProbability(pickup.itemName, numberPickups) + ", q = " + q);

			//jobItemNames.put(pickup.itemName ,pickup.itemCount);

			// Increase this jobs total reward and weight values
			doubleReward += (pickup.reward * pickup.itemCount);
			doubleWeight += (pickup.weight * pickup.itemCount);
		}

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

		System.out.println("Considered reward/weight: p * " + this.pgetProbability(totalReward) + " * " + this.pgetProbability(totalWeight)
				+ ", q * " + this.qgetProbability(totalReward) + " * " + this.qgetProbability(totalWeight));

		// Normalise the probabilities so the actual probability that this job
		// will be cancelled can be returned
		double finalProbability = this.pnormalisedProbabilityOfCancellation(p, q);

		System.out.println("\nfinal q = " + this.qnormalisedProbabilityOfCancellation(p, q));
		// Check that it's a valid probability to avoid getting wrong numbers in
		// the main job selection code
		assert ((finalProbability >= 0) && (finalProbability <= 1));

		// Return the final probability calculated
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

		double total = p + q;

		double multiplier = 1 / total;

		double result = p * multiplier;

		return result;
	}

	private double qnormalisedProbabilityOfCancellation(double p, double q) {

		double total = p + q;

		double multiplier = 1 / total;

		double result = q * multiplier;

		return result;
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

		/**
		 * Create a new Distribution based on an arraylist of pairs
		 * 
		 * @param set
		 *            the arraylist of pairs
		 */
		private Distribution(ArrayList<DPair> set) {

			this.probabilities = set;
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

			return 0;
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

			return hash(this.name, (this.p.probabilities.size() + this.q.probabilities.size()) / 2);
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

			return 0;
		}

		/**
		 * To String method for debugging
		 */
		public String toString() {

			return "[" + this.descriptor.toString() + " p = " + this.probability + "]";
		}
	}

	// METHOD FOR HASHING FEATURES

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