package warehouse.jobselection.printtests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import warehouse.event.BeginAssigningEvent;
import warehouse.event.JobCancellationEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.job.AssignedJob;
import warehouse.job.Job;
import warehouse.jobselection.JobAssignerSingle;
import warehouse.jobselection.event.SelectorHasCurrentJobEvent;
import warehouse.util.Direction;
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Robot;
import warehouse.util.Subscriber;

/**
 * Created by Owen on 22/03/2016.
 */
public class SingleTest  extends Thread{

    private List<Job> trainingJobs;
    private List<Job> actualJobs;
    private boolean hasCurrentJob;
	private int cancelledJobs;

    public static void main(String[] args) throws IOException, URISyntaxException{

        ArrayList<URI[]> fileSet = new ArrayList<>();

//        String[] files1 = new String[5];
//        files1[0] = filePath + "\\1\\locations.csv";
//        files1[1] = filePath + "\\1\\items.csv";
//        files1[2] = filePath + "\\1\\jobs.csv";
//        files1[3] = filePath + "\\1\\cancellations.csv";
//        files1[4] = filePath + "\\1\\drops.csv";
//        fileSet.add(files1);
//
//        String[] files2 = new String[5];
//        files2[0] = filePath + "\\2\\locations.csv";
//        files2[1] = filePath + "\\2\\items.csv";
//        files2[2] = filePath + "\\2\\jobs.csv";
//        files2[3] = filePath + "\\2\\cancellations.csv";
//        files2[4] = filePath + "\\2\\drops.csv";
//        fileSet.add(files2);
//
//        String[] files3 = new String[5];
//        files3[0] = filePath + "\\3\\locations.csv";
//        files3[1] = filePath + "\\3\\items.csv";
//        files3[2] = filePath + "\\3\\jobs.csv";
//        files3[3] = filePath + "\\3\\cancellations.csv";
//        files3[4] = filePath + "\\3\\drops.csv";
//        fileSet.add(files3);
//
//        String[] files4 = new String[5];
//        files4[0] = filePath + "\\4\\locations.csv";
//        files4[1] = filePath + "\\4\\items.csv";
//        files4[2] = filePath + "\\4\\jobs.csv";
//        files4[3] = filePath + "\\4\\cancellations.csv";
//        files4[4] = filePath + "\\4\\drops.csv";
//        fileSet.add(files4);
//
//        String[] files5 = new String[5];
//        files5[0] = filePath + "\\5\\locations.csv";
//        files5[1] = filePath + "\\5\\items.csv";
//        files5[2] = filePath + "\\5\\jobs.csv";
//        files5[3] = filePath + "\\5\\cancellations.csv";
//        files5[4] = filePath + "\\5\\drops.csv";
//        fileSet.add(files5);

        
        URI[] trainingFiles = new URI[5];
        trainingFiles[0] = SingleTest.class.getResource("\\actual\\locations.csv").toURI();
        trainingFiles[1] = SingleTest.class.getResource("\\actual\\items.csv").toURI();
        trainingFiles[2] = SingleTest.class.getResource("\\actual\\training_jobs.csv").toURI();
        trainingFiles[3] = SingleTest.class.getResource("\\actual\\cancellations.csv").toURI();
        trainingFiles[4] = SingleTest.class.getResource("\\actual\\drops.csv").toURI();
        fileSet.add(0,trainingFiles);

        URI[] actualFiles = new URI[5];
        actualFiles[0] = SingleTest.class.getResource("\\actual\\locations.csv").toURI();
        actualFiles[1] = SingleTest.class.getResource("\\actual\\items.csv").toURI();
        actualFiles[2] = SingleTest.class.getResource("\\actual\\jobs.csv").toURI();
        actualFiles[3] = SingleTest.class.getResource("\\actual\\marking_file.csv").toURI();
        actualFiles[4] = SingleTest.class.getResource("\\actual\\drops.csv").toURI();
        fileSet.add(1,actualFiles);

        int counter = 0;
        List<Job> trainingJobs = new Vector<>();
        List<Job> actualJobs = new Vector<>();

        for(URI[] fileNameArray : fileSet) {

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
                List<ItemPickup> jobPickups = new Vector<>();
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

        @SuppressWarnings("unused")
		SingleTest tester = new SingleTest(trainingJobs, actualJobs);
        
        
    }

    public SingleTest(List<Job> trainingJobs, List<Job> actualJobs){

    	int cancelled = 0;
    	
    	for(Job job : actualJobs){
    		
    		if(job.cancelledInTrainingSet){
    			
    			cancelled++;
    		}
    	}
    	
    	this.cancelledJobs = cancelled;
    	
        this.trainingJobs = trainingJobs;
        this.actualJobs = actualJobs;

        EventDispatcher.subscribe2(this);
        
        this.start();
    }

    @SuppressWarnings("deprecation")
	@Override
    public void run() {

    	int cancelled = 0;
    	
    	for(Job job : actualJobs){
    		
    		if(job.cancelledInTrainingSet){
    			
    			cancelled++;
    		}
    	}
    	
    	int numberJobs = actualJobs.size();
    	int numberSuccessful = actualJobs.size();
    	boolean firstCancellation = true;
    	
    	
    	System.out.println("\n" + cancelled + " cancelled jobs");
    	
    		System.out.println("Starting test. There are " + this.actualJobs.size() + " to choose from, and " + this.cancelledJobs + " were cancelled.");
    	
            Robot robot = new Robot("testRobot", new Location(0, 0), Direction.NORTH, 0);

            JobAssignerSingle assigner = new JobAssignerSingle(robot, new Vector<>(this.trainingJobs));

            EventDispatcher.onEvent2(new BeginAssigningEvent(this.actualJobs, new Vector<Location>()));

            int cancellationCounter = 0;
            int jobCounter = 0;
            
            for (int i = 0; i < actualJobs.size(); i++) {

            	jobCounter++;
            	
            	System.out.println("\nJob Number: " + jobCounter);
            	
                while(!this.hasCurrentJob) {

                    try {

                        //System.out.println("\nTEST THREAD: Sleeping");
                        Thread.sleep(1000);
                        //System.out.println("\nTEST THREAD: Woke up");
                    } catch (InterruptedException e) {
                        // Sleep was interrupted for some reason
                        e.printStackTrace();
                    }
                }
                
                    assert (this.hasCurrentJob == true);
                    assert (assigner.getCurrentJob() != null);

                    
                    if(assigner.getCurrentJob().cancelledInTrainingSet){
                    	
                    	System.out.println("\nTEST THREAD: Sending Job CANCELLATION Event");
                        
                    	EventDispatcher.onEvent2(new JobCancellationEvent(assigner.getCurrentJob()));
                    	cancellationCounter++;
                    	
                    	if(firstCancellation){
                    		numberSuccessful = jobCounter - 1;
                    	}
                    	
                    	firstCancellation = false;
                    }else{
                    
                    	System.out.println("\nTEST THREAD: Sending Job Complete Event");
                        
                    	EventDispatcher.onEvent2(new JobCompleteEvent(assigner.getCurrentJob()));
                    }
                    
                this.hasCurrentJob = false;
            }

            System.out.println("\nTEST THREAD: Telling assigner to stop");
            System.out.println("\nTEST THREAD: We managed to assign " + numberSuccessful + " / " + (numberJobs - cancellationCounter) + " before getting a cancellation!");
            assigner.stopAssigning();
            
            try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            System.out.println("\nTEST THREAD: Final List of assigned jobs in order: ");
            for(AssignedJob job : assigner.getFinalList()){
            	
            	System.out.println("\nID: " + job.id + " - " + job);
            }
            
            assert(assigner.getCutOffPoint() == numberSuccessful + 1);
            
            System.out.println("\nTEST THREAD: Cut off point = " + assigner.getCutOffPoint());
    }

    @Subscriber
    public void onHasCurrentJobEvent(SelectorHasCurrentJobEvent e){
        
        this.hasCurrentJob = true;
    }
    
    public static void parseFile(URI filePath, Consumer<String[]> consumer) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filePath));
        while(in.hasNextLine()){
            String line = in.nextLine();
            if(!line.isEmpty()) consumer.accept(line.trim().split(","));
        }
        
        in.close();
    }
    
    public static void print(Object o){

        System.out.println(o);
    }
}
