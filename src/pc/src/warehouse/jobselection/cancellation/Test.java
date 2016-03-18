package warehouse.jobselection.cancellation;

import java.util.LinkedList;

import warehouse.job.Job;
import warehouse.util.ItemPickup;
import warehouse.util.Location;

public class Test {

	public static void main(String[] args){
		
	//make 3 jobs, with the 1st being the best
	
		Job testJob1;
		Job testJob2;
		Job testJob3;
		
		//JOB 1
			LinkedList<ItemPickup> pickups = new LinkedList<ItemPickup>();
			
			ItemPickup testitem1 = new ItemPickup("testitem1", new Location(4, 0), 5);
			testitem1.reward = 10; testitem1.weight = 1;
			pickups.add(testitem1);
			
			ItemPickup testitem2 = new ItemPickup("testitem2", new Location(5, 2), 10);
			testitem2.reward = 5; testitem2.weight = 1.2;
			pickups.add(testitem2);
			
			ItemPickup testitem3 = new ItemPickup("testitem3", new Location(5, 4), 1);
			testitem3.reward = 2; testitem3.weight = 3;
			pickups.add(testitem3);
			
			testJob1 = new Job(new Location(5, 5), pickups);
			
			testJob1.cancelledInTrainingSet = false;
			
			//JOB 2
			LinkedList<ItemPickup> pickups2 = new LinkedList<ItemPickup>();
			
			ItemPickup testitem4 = new ItemPickup("testitem4", new Location(0, 1), 1);
			testitem4.reward = 1; testitem4.weight = 1;
			pickups2.add(testitem4);
			
			ItemPickup testitem5 = new ItemPickup("testitem5", new Location(0, 3), 2);
			testitem5.reward = 0.5; testitem5.weight = 10;
			pickups2.add(testitem5);
			
			testJob2 = new Job(new Location(0, 5), pickups2);
			
			testJob2.cancelledInTrainingSet = true;
			
			//JOB 3
			LinkedList<ItemPickup> pickups3 = new LinkedList<ItemPickup>();
			
			ItemPickup testitem6 = new ItemPickup("testitem6", new Location(2, 1), 40);
			testitem6.reward = 1; testitem6.weight = 1;
			pickups3.add(testitem6);
			
			testJob3 = new Job(new Location(2, 2), pickups3);
			
			testJob3.cancelledInTrainingSet = true;
			
			LinkedList<Job> jobs = new LinkedList<Job>();
			
			jobs.add(testJob1);
			jobs.add(testJob2);
			jobs.add(testJob3);
		
	CancellationMachine testMachine = new NaiveBayes(jobs);
	
	System.out.println(testMachine);
	
	LinkedList<ItemPickup> pickups4 = new LinkedList<ItemPickup>();
	pickups4.add(testitem5);
	pickups4.add(testitem3);
	pickups4.add(testitem1);
	
	System.out.println(testMachine.getProbability(new Job(new Location(4, 7), pickups4)));
	
	}
}
