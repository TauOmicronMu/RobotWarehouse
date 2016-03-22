package warehouse.jobselection.printtests;

import com.sun.xml.internal.bind.v2.runtime.*;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import warehouse.event.BeginAssigningEvent;
import warehouse.event.Event;
import warehouse.event.JobCompleteEvent;
import warehouse.job.Job;
import warehouse.jobselection.HasCurrentJobEvent;
import warehouse.jobselection.JobAssignerSingle;
import warehouse.util.*;
import warehouse.util.Location;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Owen on 22/03/2016.
 */
public class SingleTest  extends Thread{

    private List<Job> trainingJobs;
    private List<Job> actualJobs;
    private ArrayList<List<Job>> jobSet;
    private boolean hasCurrentJob;

    public static void main(String[] args) throws IOException{

        ArrayList<List<Job>> jobSet = new ArrayList<>();
        ArrayList<String[]> fileSet = new ArrayList<>();

        String filePath = "C:\\Users\\Aidan\\workspace\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test";

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

        
        String[] trainingFiles = new String[5];
        trainingFiles[0] = filePath + "\\actual\\locations.csv";
        trainingFiles[1] = filePath + "\\actual\\items.csv";
        trainingFiles[2] = filePath + "\\actual\\training_jobs.csv";
        trainingFiles[3] = filePath + "\\actual\\cancellations.csv";
        trainingFiles[4] = filePath + "\\actual\\drops.csv";
        fileSet.add(trainingFiles);

        String[] actualFiles = new String[5];
        actualFiles[0] = filePath + "\\actual\\locations.csv";
        actualFiles[1] = filePath + "\\actual\\items.csv";
        actualFiles[2] = filePath + "\\actual\\jobs.csv";
        actualFiles[3] = filePath + "\\actual\\cancellations.csv";
        actualFiles[4] = filePath + "\\actual\\drops.csv";
        fileSet.add(actualFiles);

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
                parseFile(fileNameArray[3], values -> {

                    if (values[values.length - 1].equals("Cancel")) {

                        jobs.get(values[0]).cancelledInTrainingSet = true;
                    }
                });
            }
            List<Location> dropLocations = new ArrayList<>();
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

        SingleTest tester = new SingleTest(trainingJobs, actualJobs);
    }

    public SingleTest(List<Job> trainingJobs, List<Job> actualJobs){

        this.trainingJobs = trainingJobs;
        this.actualJobs = actualJobs;

        EventDispatcher.subscribe2(this);
        
        this.start();
    }

    @Override
    public void run() {

            Robot robot = new Robot("testRobot", new Location(0, 0), Direction.NORTH);

            JobAssignerSingle assigner = new JobAssignerSingle(robot, new LinkedList<>(this.trainingJobs));

            EventDispatcher.onEvent2(new BeginAssigningEvent(this.actualJobs, new LinkedList<Location>()));

            for (int i = 0; i < actualJobs.size(); i++) {

                while(!this.hasCurrentJob) {

                    try {

                        //System.out.println("\nTEST THREAD: Sleeping");
                        Thread.sleep(3000);
                        //System.out.println("\nTEST THREAD: Woke up");
                    } catch (InterruptedException e) {
                        // Sleep was interrupted for some reason
                        e.printStackTrace();
                    }
                }
                
                    assert (this.hasCurrentJob == true);
                    assert (assigner.getCurrentJob() != null);

                    System.out.println("\nTEST THREAD: Sending Job Complete Event");
                    EventDispatcher.onEvent2(new JobCompleteEvent(assigner.getCurrentJob()));
                
                this.hasCurrentJob = false;
            }

            System.out.println("\nTEST THREAD: Telling assigner to stop");
            assigner.stopAssigning();


    }

    @Subscriber
    public void onHasCurrentJobEvent(HasCurrentJobEvent e){
        
        this.hasCurrentJob = true;
    }
    
    public static void parseFile(String filePath, Consumer<String[]> consumer) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filePath));
        while(in.hasNextLine()){
            String line = in.nextLine();
            if(!line.isEmpty()) consumer.accept(line.trim().split(","));
        }
    }

    public static void print(Object o){

        System.out.println(o);
    }
}
