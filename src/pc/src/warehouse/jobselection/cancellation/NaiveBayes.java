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

	private Distribution NumberPickupsGivenNotCancelled;
	private Distribution TotalRewardGivenNotCancelled;
	private Distribution TotalWeightGivenNotCancelled;

	private Descriptor NumberPickups;
	private Descriptor TotalReward;
	private Descriptor TotalWeight;

	private double pCancelled;
	private double qCancelled;

	/**
	 * Construct a NaiveBayes object with a training set, and create probability
	 * distributions based on this training set.
	 * 
	 * @param trainingSet
	 *            the set of jobs to learn from
	 */
	public NaiveBayes(LinkedList<Job> trainingSet) {

		// Create Descriptor objects for the features of the training set - this
		// is assuming that we are only using Number of Items, Total Reward and
		// Total Weight of the job
		// Each Descriptor contains two Distribution objects - one for the
		// probabilities given cancelled, and one for given not cancelled

		// number of pickups
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

		// total reward
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

		// total weight
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

		this.NumberPickups = new Descriptor("Number of Pickups", this.NumberPickupsGivenCancelled,
				this.NumberPickupsGivenNotCancelled);
		this.TotalReward = new Descriptor("Total Reward", this.TotalRewardGivenCancelled,
				this.TotalRewardGivenNotCancelled);
		this.TotalWeight = new Descriptor("Total Weight", this.TotalWeightGivenCancelled,
				this.TotalWeightGivenNotCancelled);

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

			int intPickups = 0;
			double doubleReward = 0;
			double doubleWeight = 0;

			for (ItemPickup pickup : job.pickups) {

				intPickups += pickup.itemCount;
				doubleReward += (pickup.reward * pickup.itemCount);
				doubleWeight += (pickup.weight * pickup.itemCount);
			}

			// Convert the raw numbers from the job into general range enums so
			// they can be compared against easily later
			NumRange numberPickups = this.convertNumber(intPickups);
			RewardRange totalReward = this.convertReward(doubleReward);
			WeightRange totalWeight = this.convertWeight(doubleWeight);

			assert (numberPickups != NumRange.Error);
			assert (totalReward != RewardRange.Error);
			assert (totalWeight != WeightRange.Error);

			// If the job was cancelled, increment the numerators of the
			// probabilities in that distribution by one
			if (job.cancelledInTrainingSet) {

				numCancelled++;
				pdenominator++;

				this.pincrementProbabilities(numberPickups, totalReward, totalWeight);

			}
			// If the job was not cancelled, increment the numerators of the
			// probabilities in the alternate distribution by one
			else if (!job.cancelledInTrainingSet) {

				numNotCancelled++;
				qdenominator++;

				this.qincrementProbabilities(numberPickups, totalReward, totalWeight);
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

		if ((0 < intPickups) && (intPickups < 10)) {

			numberPickups = NumRange.OneToNine;
		} else if ((10 <= intPickups) && (intPickups < 20)) {

			numberPickups = NumRange.TenToNineteen;
		} else if ((20 <= intPickups) && (intPickups < 30)) {

			numberPickups = NumRange.TwentyToTwentyNine;
		} else if (30 <= intPickups) {

			numberPickups = NumRange.ThirtyPlus;
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

		if ((0 <= doubleReward) && (doubleReward < 20)) {

			totalReward = RewardRange.ZeroToNineteen;
		} else if ((20 <= doubleReward) && (doubleReward < 40)) {

			totalReward = RewardRange.TwentyToThirtyNine;
		} else if ((40 <= doubleReward) && (doubleReward < 60)) {

			totalReward = RewardRange.FortyToFiftyNine;
		} else if ((60 <= doubleReward) && (doubleReward < 80)) {

			totalReward = RewardRange.SixtyToSeventyNine;
		} else if (80 <= doubleReward) {

			totalReward = RewardRange.EightyPlus;
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

		if ((0 <= doubleWeight) && (doubleWeight < 5)) {

			totalWeight = WeightRange.ZeroToFour;
		} else if ((5 <= doubleWeight) && (doubleWeight < 10)) {

			totalWeight = WeightRange.FiveToNine;
		} else if ((10 <= doubleWeight) && (doubleWeight < 15)) {

			totalWeight = WeightRange.TenToFourteen;
		} else if ((15 <= doubleWeight) && (doubleWeight < 20)) {

			totalWeight = WeightRange.FifteenToNineteen;
		} else if ((20 <= doubleWeight) && (doubleWeight < 25)) {

			totalWeight = WeightRange.TwentyToTwentyFour;
		} else if ((25 <= doubleWeight) && (doubleWeight < 30)) {

			totalWeight = WeightRange.TwentyFiveToTwentyNine;
		} else if (30 <= doubleWeight) {

			totalWeight = WeightRange.ThirtyPlus;
		}

		return totalWeight;
	}

	/**
	 * Increment the probabilities of a distribution based on the enums a job
	 * has (when a job was cancelled)
	 * 
	 * @param numberPickups
	 *            the enum for the number of pickups
	 * @param totalReward
	 *            the enum for the total reward
	 * @param totalWeight
	 *            the enum for the total weight
	 */
	private void pincrementProbabilities(NumRange numberPickups, RewardRange totalReward, WeightRange totalWeight) {

		// Number of pickups
		if (this.NumberPickups.p.contains(numberPickups)) {

			assert (this.NumberPickups.p.get(numberPickups).isPresent());

			this.NumberPickups.p.get(numberPickups).get().probability++;
		}

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
	 * @param numberPickups
	 *            the enum for the number of pickups
	 * @param totalReward
	 *            the enum for the total reward
	 * @param totalWeight
	 *            the enum for the total weight
	 */
	private void qincrementProbabilities(NumRange numberPickups, RewardRange totalReward, WeightRange totalWeight) {

		// Number of pickups
		if (this.NumberPickups.q.contains(numberPickups)) {

			assert (this.NumberPickups.q.get(numberPickups).isPresent());

			this.NumberPickups.q.get(numberPickups).get().probability++;
		}

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

		for (int i = 0; i < this.NumberPickups.p.probabilities.size(); i++) {

			this.NumberPickups.p.probabilities.get(i).probability /= pdenom;
			this.NumberPickups.q.probabilities.get(i).probability /= qdenom;
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
	 * @param n
	 *            th enum
	 * @return the probability as a double
	 */
	private double pgetProbability(NumRange n) {

		assert (this.NumberPickups.p.get(n).isPresent());

		return this.NumberPickups.p.get(n).get().probability == 0 ? 0.01
				: this.NumberPickups.p.get(n).get().probability;
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

		return this.TotalReward.p.get(r).get().probability == 0 ? 0.01 : this.TotalReward.p.get(r).get().probability;
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

		return this.TotalWeight.p.get(w).get().probability == 0 ? 0.01 : this.TotalWeight.p.get(w).get().probability;
	}

	/**
	 * Get the probability that a job had this enum given it was not cancelled
	 * 
	 * @param n
	 *            the enum
	 * @return the probability as a double
	 */
	private double qgetqrobability(NumRange n) {

		assert (this.NumberPickups.q.get(n).isPresent());

		return this.NumberPickups.q.get(n).get().probability == 0 ? 0.01
				: this.NumberPickups.q.get(n).get().probability;
	}

	/**
	 * Get the probability that a job had this enum given it was not cancelled
	 * 
	 * @param r
	 *            the enum
	 * @return the probability as a double
	 */
	private double qgetqrobability(RewardRange r) {

		assert (this.TotalReward.q.get(r).isPresent());

		return this.TotalReward.q.get(r).get().probability == 0 ? 0.01 : this.TotalReward.q.get(r).get().probability;
	}

	/**
	 * Get the probability that a job had this enum given it was not cancelled
	 * 
	 * @param w
	 *            the enum
	 * @return the probability as a double
	 */
	private double qgetqrobability(WeightRange w) {

		assert (this.TotalWeight.q.get(w).isPresent());

		return this.TotalWeight.q.get(w).get().probability == 0 ? 0.01 : this.TotalWeight.q.get(w).get().probability;
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

		int intPickups = 0;
		double doubleReward = 0;
		double doubleWeight = 0;

		for (ItemPickup pickup : job.pickups) {

			intPickups += pickup.itemCount;
			doubleReward += (pickup.reward * pickup.itemCount);
			doubleWeight += (pickup.weight * pickup.itemCount);
		}

		// Convert the jobs fields into the appropriate enum values
		NumRange numberPickups = this.convertNumber(intPickups);
		RewardRange totalReward = this.convertReward(doubleReward);
		WeightRange totalWeight = this.convertWeight(doubleWeight);

		assert (numberPickups != NumRange.Error);
		assert (totalReward != RewardRange.Error);
		assert (totalWeight != WeightRange.Error);

		// Find probability from each enum that matches and use them to
		// calculate the pre - normalised probabilities that the job will or
		// will not be cancelled

		double p = this.pCancelled * this.pgetProbability(numberPickups) * this.pgetProbability(totalReward)
				* this.pgetProbability(totalWeight);
		double q = this.qCancelled * this.qgetqrobability(numberPickups) * this.qgetqrobability(totalReward)
				* this.qgetqrobability(totalWeight);

		// Normalise the probabilities so the actual probability that this job
		// will be cancelled can be calculated
		double finalProbability = this.normalisedProbabilityOfCancellation(p, q);

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
	private double normalisedProbabilityOfCancellation(double p, double q) {

		// print(""+p);
		// print(""+q);

		double total = p + q;

		// print(""+total);

		double multiplier = 1 / total;

		// print(""+multiplier);

		double result = p * multiplier;

		// print(""+result);

		return result;
	}

	// DESCRIBES AN ARRAY OF DPAIRS

	private class Distribution {

		private ArrayList<DPair> probabilities;

		private Distribution(ArrayList<DPair> set) {

			this.probabilities = set;
		}

		public boolean contains(Object descriptor) {

			for (int i = 0; i < this.probabilities.size(); i++) {

				if (this.probabilities.get(i).descriptor.equals(descriptor)) {

					return true;
				}
			}

			return false;
		}

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
		public boolean equals(Object o) {

			if ((o instanceof Descriptor) && (((Descriptor) o).p.equals(this.p))
					&& (((Descriptor) o).q.equals(this.q))) {

				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {

			return 0;
		}

		public String toString() {

			return "For Descriptor: '" + this.name + "' Distibution of: cancelled = " + this.p + " not cancelled = "
					+ this.q;
		}
	}

	// DESCRIBES A LINKED OBJECT AND PROBABILITY

	private class DPair {

		private Object descriptor;
		public double probability;

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

		public String toString() {

			return "[" + this.descriptor.toString() + " p = " + this.probability + "]";
		}
	}
}