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

public class CancellationMachineTester {

    public CancellationMachineTester(double percentage, String[]... fileNames) throws IOException{

        assert((percentage >= 0) && (percentage <= 1.0));

        for(String[] fileNameArray : fileNames){

            String s = "";

            for(String string : fileNameArray){

                s += " '" + string + "' ";
            }

            System.out.println("\nFor set: " + s + "\nProbability machine is correct = " + testMachine(fileNameArray, percentage));
        }
    }

    public double testMachine(String[] fileNameArray, double percentage) throws IOException{

        assert(fileNameArray.length == 5);

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
            for(int i = 1; i < values.length; i += 2) {
                ItemPickup p = (ItemPickup) itemPickups.get(values[i]).clone();
                p.itemCount = Integer.parseInt(values[i+1]);
                jobPickups.add(p);
            }
            jobs.put(values[0], new Job(null, jobPickups, values[0]));
        });

        // Parse cancellations file (I'm not sure of the actual file name)
        parseFile(fileNameArray[3], values -> jobs.get(values[0]).cancelledInTrainingSet = values[1].equals("0") ? false : true);

        List<Location> dropLocations = new ArrayList<>();
        parseFile(fileNameArray[4], values -> {
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

            System.out.println("p = " + generatedProbability + " cancelled: " + job.cancelledInTrainingSet);

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
        System.out.println("Percentage correct: " + percentageCorrect);
        System.out.println("Number correct:     " + numberJobsCancelled*percentageCorrect);
        return (percentageCorrect/numberJobsCancelled)*100;
    }

    public static void parseFile(String filePath, Consumer<String[]> consumer) throws FileNotFoundException {
		Scanner in = new Scanner(new File(filePath));
		while(in.hasNextLine()) consumer.accept(in.nextLine().trim().split(","));
	}

}
