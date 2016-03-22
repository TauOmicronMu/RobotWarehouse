package warehouse.jobselection.printtests;

import com.sun.xml.internal.bind.v2.runtime.*;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import warehouse.event.BeginAssigningEvent;
import warehouse.event.Event;
import warehouse.event.JobCompleteEvent;
import warehouse.job.Job;
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

    private ArrayList<List<Job>> jobSet;

    public static void main(String[] args) throws IOException{

        ArrayList<List<Job>> jobSet = new ArrayList<>();
        ArrayList<String[]> fileSet = new ArrayList<>();

        String filePath = "TODO";

        String[] files1 = new String[5];
        files1[0] = filePath + "\\1\\locations.csv";
        files1[1] = filePath + "\\1\\items.csv";
        files1[2] = filePath + "\\1\\jobs.csv";
        files1[3] = filePath + "\\1\\cancellations.csv";
        files1[4] = filePath + "\\1\\drops.csv";
        fileSet.add(files1);

        String[] files2 = new String[5];
        files2[0] = filePath + "\\2\\locations.csv";
        files2[1] = filePath + "\\2\\items.csv";
        files2[2] = filePath + "\\2\\jobs.csv";
        files2[3] = filePath + "\\2\\cancellations.csv";
        files2[4] = filePath + "\\2\\drops.csv";
        fileSet.add(files2);

        String[] files3 = new String[5];
        files3[0] = filePath + "\\3\\locations.csv";
        files3[1] = filePath + "\\3\\items.csv";
        files3[2] = filePath + "\\3\\jobs.csv";
        files3[3] = filePath + "\\3\\cancellations.csv";
        files3[4] = filePath + "\\3\\drops.csv";
        fileSet.add(files3);

        String[] files4 = new String[5];
        files4[0] = filePath + "\\4\\locations.csv";
        files4[1] = filePath + "\\4\\items.csv";
        files4[2] = filePath + "\\4\\jobs.csv";
        files4[3] = filePath + "\\4\\cancellations.csv";
        files4[4] = filePath + "\\4\\drops.csv";
        fileSet.add(files4);

        String[] files5 = new String[5];
        files5[0] = filePath + "\\5\\locations.csv";
        files5[1] = filePath + "\\5\\items.csv";
        files5[2] = filePath + "\\5\\jobs.csv";
        files5[3] = filePath + "\\5\\cancellations.csv";
        files5[4] = filePath + "\\5\\drops.csv";
        fileSet.add(files5);

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

            // Parse cancellations file (I'm not sure of the actual file name)
            parseFile(fileNameArray[3], values -> jobs.get(values[0]).cancelledInTrainingSet = values[1].equals("0") ? false : true);

            List<Location> dropLocations = new ArrayList<>();
            parseFile(fileNameArray[4], values -> jobs.values().forEach(job -> job.dropLocation = new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1]))));

            // Convert the job map to a list

            List<Job> jobList = jobs.values().stream().collect(Collectors.toList());

            jobSet.add(jobList);
        }

        SingleTest tester = new SingleTest(jobSet);
    }

    public SingleTest(ArrayList<List<Job>> jobSet){

        this.jobSet = jobSet;
        this.start();
    }

    @Override
    public void run(){

        for(List<Job> jobs : this.jobSet){

            Robot robot = new Robot("testRobot", new Location(0, 0), Direction.NORTH);

            EventDispatcher.subscribe2(this);

            JobAssignerSingle assigner = new JobAssignerSingle(robot);

            EventDispatcher.onEvent2(new BeginAssigningEvent(jobs, new LinkedList<Location>()));

            for(int i = 0; i < 10; i++) {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // Sleep was interrupted for some reason
                    e.printStackTrace();
                }

                EventDispatcher.onEvent2(new JobCompleteEvent(assigner.getCurrentJob()));
            }

            assigner.stopAssigning();
        }
    }

    public static void parseFile(String filePath, Consumer<String[]> consumer) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filePath));
        while(in.hasNextLine()) consumer.accept(in.nextLine().trim().split(","));
    }

    public static void print(Object o){

        System.out.println(o);
    }
}
