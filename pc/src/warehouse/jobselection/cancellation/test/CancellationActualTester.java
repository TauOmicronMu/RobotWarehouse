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


        System.out.println("Percentage correct: " + testMachine(trainingSet, testSet));

    }

    public double testMachine(String[] trainingSet, String[] testSet) throws IOException{

        assert(trainingSet.length == 5);
        assert(testSet.length == 5);

        //TRAINING SET

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
            dropLocations.add(new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1].trim())));
        });

        // Convert the job map to a list

        List<Job> trainingJobsList = jobs.values().stream().collect(Collectors.toList());

        //TEST SET

        // Parse locations
        HashMap<String, Location> itemLocations2 = new HashMap<>();
        parseFile(testSet[0], values -> itemLocations2.put(values[2], new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1]))));

        // Parse items file
        HashMap<String, ItemPickup> itemPickups2 = new HashMap<>();
        parseFile(testSet[1], values -> itemPickups2.put(values[0], new ItemPickup(values[0], itemLocations2.get(values[0]), 0, Double.parseDouble(values[1]), Double.parseDouble(values[2]))));

        // Parse jobs file
        HashMap<String, Job> jobs2 = new HashMap<>();
        parseFile(testSet[2], values -> {
            List<ItemPickup> jobPickups = new LinkedList<>();
            for(int i = 1; i < values.length; i += 2) {
                ItemPickup p = (ItemPickup) itemPickups2.get(values[i]).clone();
                p.itemCount = Integer.parseInt(values[i+1]);
                jobPickups.add(p);
            }
            jobs2.put(values[0], new Job(null, jobPickups, values[0]));
        });

        parseFile(testSet[3], values -> {

            if(values[values.length - 1].equals("Cancel")) {

                jobs2.get(values[0]).cancelledInTrainingSet = true;
            }
        });

        List<Location> dropLocations2 = new ArrayList<>();
        parseFile(testSet[4], values -> {
            if(values.length < 2) return;
            dropLocations2.add(new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1].trim())));
        });

        // Convert the job map to a list

        List<Job> knownJobsList = jobs2.values().stream().collect(Collectors.toList());

        CancellationMachine testMachine = new NaiveBayes(trainingJobsList);

        System.out.println(testMachine);
        double percentageCancelledCorrect = 0;
        double percentageNotCancelledCorrect = 0;

        int numberCancelled = 0;
        int numberNotCancelled = 0;

        for(Job job : knownJobsList){

            double generatedProbability = testMachine.getProbability(job);

            //System.out.println("p = " + generatedProbability + " cancelled: " + job.cancelledInTrainingSet);

            if(job.cancelledInTrainingSet){

                numberCancelled++;

                if (generatedProbability > 0.5) {

                    percentageCancelledCorrect++;
                }
            } else if(!job.cancelledInTrainingSet){

                numberNotCancelled++;

                if(generatedProbability <= 0.5){

                    percentageNotCancelledCorrect++;
                }
            }
        }

        System.out.println("Number Predicted cancelled:     " + percentageCancelledCorrect);
        System.out.println("Number cancelled:               " + numberCancelled);
        System.out.println("Number Predicted not cancelled: " + percentageNotCancelledCorrect);
        System.out.println("Number not cancelled            " + numberNotCancelled);
        return ((percentageCancelledCorrect + percentageNotCancelledCorrect)/knownJobsList.size())*100;
    }

    public static void parseFile(String filePath, Consumer<String[]> consumer) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filePath));
        while(in.hasNextLine()) consumer.accept(in.nextLine().trim().split(","));
    }

}
