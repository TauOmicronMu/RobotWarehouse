package warehouse.job;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import warehouse.event.BeginAssigningEvent;
import warehouse.util.*;

public class JobInput {
	
	static {
		EventDispatcher.subscribe2(JobInput.class);
	}
	
	public static void launch() throws IOException {
		// Parse locations
		HashMap<String, Location> itemLocations = new HashMap<>();
        List<Location> dropLocations = new ArrayList<>();
        HashMap<String, ItemPickup> itemPickups = new HashMap<>();
        HashMap<String, Job> jobs = new HashMap<>();

        final int numSets = 5;

        repeat(numSets, i -> {
            try {
                parseFile(i + "/locations.csv", values -> itemLocations.put(values[2], new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1]))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

		// Parse items file
        repeat(numSets, i -> {
            try {
                parseFile(i + "/items.csv", values -> itemPickups.put(values[0], new ItemPickup(values[0], itemLocations.get(values[0]), 0, Double.parseDouble(values[1]), Double.parseDouble(values[2]))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

		// Parse jobs file
		repeat(numSets, i -> {
            try {
                parseFile(i + "/jobs.csv", values -> {
                    List<ItemPickup> jobPickups = new LinkedList<>();
                    for (int j = 1; j < values.length; j += 2) {
                        ItemPickup p = (ItemPickup) itemPickups.get(values[j]).clone();
                        p.itemCount = Integer.parseInt(values[j + 1]);
                        jobPickups.add(p);
                    }
                    jobs.put(values[0], new Job(null, jobPickups, values[0]));
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

		// Parse cancellations file
		repeat(numSets, i -> {
            try {
                parseFile(i + "/cancellations.csv", values -> {
                    jobs.get(values[0]).cancelledInTrainingSet = values[1].equals("0") ? false : true;
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        repeat(numSets, i -> {
            try {
                parseFile(i + "/drops.csv", values -> {
                    if (values.length < 2) return;
                    System.out.println(Arrays.toString(values));
                    dropLocations.add(new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

		// Convert the job map to a list
		List<Job> jobList = jobs.values().stream().collect(Collectors.toList());
		Location lastDropLocation = dropLocations.get(dropLocations.size() - 1);
        	jobList.forEach(job -> job.dropLocation = lastDropLocation);
        	EventDispatcher.onEvent2(new BeginAssigningEvent(jobList, new LinkedList<>()));
	}

    private static void repeat(int times, Consumer<Integer> consumer) {
        for(int i = 0; i < times; i++) consumer.accept(i);
    }

    public static void parseFile(String filePath, Consumer<String[]> consumer) throws FileNotFoundException {
		Scanner in = new Scanner(new File(filePath));
		while(in.hasNextLine()) {
			String line = in.nextLine();
			if(!line.isEmpty()) consumer.accept(line.trim().split(","));
		}
	}

}
