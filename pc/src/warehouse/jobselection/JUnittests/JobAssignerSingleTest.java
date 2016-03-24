package warehouse.jobselection.JUnittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import warehouse.event.BeginAssigningEvent;
import warehouse.event.JobCancellationEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.job.Job;
import warehouse.jobselection.JobAssignerSingle;
import warehouse.jobselection.JobSelectorSingle;
import warehouse.jobselection.JobWorth;
import warehouse.jobselection.cancellation.Backup;
import warehouse.jobselection.cancellation.CancellationMachine;
import warehouse.jobselection.cancellation.NaiveBayes;
import warehouse.jobselection.event.FinishedAssigningEvent;
import warehouse.util.Direction;
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Robot;
import warehouse.util.Subscriber;

public class JobAssignerSingleTest {

	private LinkedList<Job> trainingSet;
	private LinkedList<Job> actualSet;
	private boolean checking;
	private JobSelectorSingle testSelector;
	private CancellationMachine testCancellationMachine;
	private LinkedList<Job> finalList;
	private LinkedList<Job> actualSetClone;

	@Before
	public void setUp() throws Exception {
	
		ArrayList<String[]> fileSet = new ArrayList<>();

        String filePath = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test";

        String[] trainingFiles = new String[5];
        trainingFiles[0] = filePath + "\\actual\\locations.csv";
        trainingFiles[1] = filePath + "\\actual\\items.csv";
        trainingFiles[2] = filePath + "\\actual\\training_jobs.csv";
        trainingFiles[3] = filePath + "\\actual\\cancellations.csv";
        trainingFiles[4] = filePath + "\\actual\\drops.csv";
        fileSet.add(0,trainingFiles);

        String[] actualFiles = new String[5];
        actualFiles[0] = filePath + "\\actual\\locations.csv";
        actualFiles[1] = filePath + "\\actual\\items.csv";
        actualFiles[2] = filePath + "\\actual\\jobs.csv";
        actualFiles[3] = filePath + "\\actual\\marking_file.csv";
        actualFiles[4] = filePath + "\\actual\\drops.csv";
        fileSet.add(1,actualFiles);

        int counter = 0;
        List<Job> trainingJobs = new LinkedList<>();
        List<Job> actualJobs = new LinkedList<>();

        for(String[] fileNameArray : fileSet) {

            assert (fileNameArray.length == 5);

            // Parse locations
            HashMap<String, Location> itemLocations = new HashMap<>();
            parseFile(fileNameArray[0], values -> itemLocations.put(values[2], new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1]))));

            // Parse items file
            HashMap<String, ItemPickup> itemPickups = new HashMap<>();
            parseFile(fileNameArray[1], values -> itemPickups.put(values[0], new ItemPickup(values[0], itemLocations.get(values[0]), 0, Double.parseDouble(values[1]), Double.parseDouble(values[2]))));

            // Parse jobs file
            HashMap<String, Job> jobs = new HashMap<>();
            parseFile(fileNameArray[2], values -> {
                List<ItemPickup> jobPickups = new LinkedList<>();
                for (int i = 1; i < values.length; i += 2) {
                    ItemPickup p = (ItemPickup) itemPickups.get(values[i]).clone();
                    p.itemCount = Integer.parseInt(values[i + 1]);
                    jobPickups.add(p);
                }
                jobs.put(values[0], new Job(null, jobPickups, values[0]));
            });

            if(counter == 0) {

                // Parse cancellations file (I'm not sure of the actual file name)
                parseFile(fileNameArray[3], values -> jobs.get(values[0]).cancelledInTrainingSet = values[1].equals("0") ? false : true);

            }
            else{
            	
            	parseFile(fileNameArray[3], values -> {

                    if(values[values.length - 1].equals("Cancel")) {

                        jobs.get(values[0]).cancelledInTrainingSet = true;
                    }
                });
            }

            parseFile(fileNameArray[4], values -> jobs.values().forEach(job -> job.dropLocation = new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1].trim()))));

            // Convert the job map to a list

            List<Job> jobList = jobs.values().stream().collect(Collectors.toList());

            if(counter == 0) {

                trainingJobs = jobList;
            } else{

                actualJobs = jobList;
            }

            counter++;
        }

		this.trainingSet = new LinkedList<>(trainingJobs);
		this.actualSet = new LinkedList<>(actualJobs);
		
		this.actualSetClone = new LinkedList<>();
		
		for(Job job : this.actualSet){
			
			this.actualSetClone.add(job);
		}
		
		try{
			this.testCancellationMachine = new NaiveBayes(this.trainingSet); 
		}
		catch(NullPointerException e){
			this.testCancellationMachine = new Backup();
		}
		catch(AssertionError e){
			this.testCancellationMachine = new Backup();
		}
		
		
		this.checking = false;
		
		EventDispatcher.subscribe2(this);
	}

	@Test
	public void test() throws InterruptedException {
		
		LinkedList<Job> finalAssignedJobsList = new LinkedList<>();
		
		Robot robot = new Robot("testRobot", new Location(0, 0), Direction.NORTH, 0);
		
		JobAssignerSingle testAssigner = new JobAssignerSingle(robot, this.trainingSet);
		
		Thread.sleep(1000);
		
		assertEquals(this.testCancellationMachine, testAssigner.getCancellationMachine());
		
		int number = 0;
		
		System.out.println("\nUNIT TEST THREAD: Our cancellation machine: \n" + this.testCancellationMachine);
		
		this.testSelector = new JobSelectorSingle(number, robot, this.actualSet, this.testCancellationMachine);
		
		System.out.println("\nUNIT TEST THREAD: Sending Event");
		EventDispatcher.onEvent2(new BeginAssigningEvent(this.actualSetClone, new LinkedList<Location>()));
		
		Thread.sleep(3000);
		
		this.checking = true;
		
		String id = testAssigner.getCurrentJob().id;
		
		while(this.checking){
			
			Thread.sleep(1000);
			
			System.out.println("\nUNIT TEST THREAD: Our list is length " + this.testSelector.getSelectedList().size());
			System.out.println("\nUNIT TEST THREAD: Our list: " + this.testSelector.getSelectedList());
			System.out.println("\nUNIT TEST THREAD: Their list is length " + testAssigner.getAssignJobs().size());
			System.out.println("\nUNIT TEST THREAD: Their list: " + testAssigner.getAssignJobs());
			
			JobWorth bestJob = this.testSelector.getSelectedList().removeFirst();
			
			System.out.println("\nUNIT TEST THREAD: Our best job is ID " + bestJob.getJob().id + ": " + bestJob);
			
			this.actualSet.remove(bestJob);
			
			assertNotNull(bestJob);
			assertNotNull(testAssigner.getCurrentJob());
			
			System.out.println("\nUNIT TEST THREAD: Expected Job ID of   " + bestJob.getJob().id);
			System.out.println("\nUNIT TEST THREAD: Actual   Job ID of   " + testAssigner.getCurrentJob().id);
			
			assertEquals(testAssigner.getCurrentJob().id, id);
			
			id = testAssigner.getAssignJobs().getFirst().getJob().id;
			System.out.println("\nUNIT TEST THREAD: First Job in list of ID " + testAssigner.getAssignJobs().getFirst().getJob().id);
			
			assertEquals(testAssigner.getCurrentJob().id, bestJob.getJob().id);
			
			if(testAssigner.getCurrentJob().cancelledInTrainingSet){
            	
				number++;
				
				this.testSelector = new JobSelectorSingle(number, robot, this.actualSet, this.testCancellationMachine);
				
				Thread.sleep(1000);
				
            	EventDispatcher.onEvent2(new JobCancellationEvent(testAssigner.getCurrentJob()));
            	
            }else{
            
            	EventDispatcher.onEvent2(new JobCompleteEvent(testAssigner.getCurrentJob()));
            }
			
			//assertEquals(testAssigner.getFinalList(), this.finalList);
		}
	}

	@Subscriber
	public void stopChecking(FinishedAssigningEvent e){
		
		this.checking = false;
	}
	
	public static void parseFile(String filePath, Consumer<String[]> consumer) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filePath));
        while(in.hasNextLine()){
            String line = in.nextLine();
            if(!line.isEmpty()) consumer.accept(line.trim().split(","));
        }
        
        in.close();
    }
}
