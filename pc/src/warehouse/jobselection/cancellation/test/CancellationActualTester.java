package warehouse.jobselection.cancellation.test;

import warehouse.job.Job;
import warehouse.jobselection.cancellation.CancellationMachine;
import warehouse.jobselection.cancellation.NaiveBayes;
import warehouse.util.ItemPickup;
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
public class CancellationActualTester {

    public CancellationActualTester(String[] trainingSet, String[] testSet) throws IOException {


        System.out.println("Percentage correct: " + testMachine(trainingSet, testSet);

    }

    public double testMachine(String[] trainingSet, String[] testSet) throws IOException{

        assert(trainingSet.length == 5);
        assert(testSet.length == 5);

        // Parse locations
        HashMap<String, Location> itemLocations = new HashMap<>();
        parseFile(trainingSet[0], values -> itemLocations.put(values[2], new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1]))));

        // Parse items file
        HashMap<String, ItemPickup> itemPickups = new HashMap<>();
        parseFile(trainingSet[1], values -> itemPickups.put(values[0], new ItemPickup(values[0], itemLocations.get(values[0]), 0, Double.parseDouble(values[1]), Double.parseDouble(values[2]))));

        // Parse jobs file
        HashMap<String, Job> jobs = new HashMap<>();
        parseFile(trainingSet[2], values -> {
            List<ItemPickup> jobPickups = new LinkedList<>();
            for(int i = 1; i < values.length; i += 2) {
                ItemPickup p = (ItemPickup) itemPickups.get(values[i]).clone();
                p.itemCount = Integer.parseInt(values[i+1]);
                jobPickups.add(p);
            }
            jobs.put(values[0], new Job(null, jobPickups, values[0]));
        });

        // Parse cancellations file (I'm not sure of the actual file name)
        parseFile(trainingSet[3], values -> jobs.get(values[0]).cancelledInTrainingSet = values[1].equals("0") ? false : true);

        List<Location> dropLocations = new ArrayList<>();
        parseFile(trainingSet[4], values -> {
            if(values.length < 2) return;
            dropLocations.add(new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
        });

        // Convert the job map to a list

        List<Job> trainingJobsList = new LinkedList<>();
        List<Job> knownJobsList = new LinkedList<>();
        List<Job> jobList = jobs.values().stream().collect(Collectors.toList());

        for(int i = 0; i < (int)(jobs.size()*percentage); i++){

            trainingJobsList.add(jobList.get(i));
        }

        for(int i = (int)(jobs.size()*percentage); i < jobs.size(); i++){

            knownJobsList.add(jobList.get(i));
        }

        CancellationMachine testMachine = new NaiveBayes(trainingJobsList);

        System.out.println(testMachine);
        double percentageCorrect = 0;
        int numberJobsCancelled = 0;

        for(Job job : knownJobsList){

            double generatedProbability = testMachine.getProbability(job);

            //System.out.println("p = " + generatedProbability + " cancelled: " + job.cancelledInTrainingSet);

            if(job.cancelledInTrainingSet){

                numberJobsCancelled++;

                if (generatedProbability > 0.5) {

                    percentageCorrect++;
                }
            }
        }

        System.out.println("Total Jobs:         " + jobList.size());
        System.out.println("Training with:      " + trainingJobsList.size());
        System.out.println("Checking with:      " + knownJobsList.size());
        System.out.println("Number Cancelled:   " + numberJobsCancelled);
        System.out.println("Number Predicted:   " + percentageCorrect);
        return (percentageCorrect/numberJobsCancelled)*100;
    }

    public static void parseFile(String filePath, Consumer<String[]> consumer) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filePath));
        while(in.hasNextLine()) consumer.accept(in.nextLine().trim().split(","));
    }

}
