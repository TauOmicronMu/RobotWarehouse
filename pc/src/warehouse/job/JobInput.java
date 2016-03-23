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
		parseFile("locations.csv", values -> itemLocations.put(values[2], new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1]))));

		// Parse items file
		HashMap<String, ItemPickup> itemPickups = new HashMap<>();
		parseFile("items.csv", values -> {
			itemPickups.put(values[0], new ItemPickup(values[0], itemLocations.get(values[0]), 0, Double.parseDouble(values[1]), Double.parseDouble(values[2])));
		});

		// Parse jobs file
		HashMap<String, Job> jobs = new HashMap<>();
		parseFile("jobs.csv", values -> {
			List<ItemPickup> jobPickups = new LinkedList<>();
			for(int i = 1; i < values.length; i += 2) {
				ItemPickup p = (ItemPickup) itemPickups.get(values[i]).clone();
				p.itemCount = Integer.parseInt(values[i+1]);
				jobPickups.add(p);
			}
			jobs.put(values[0], new Job(null, jobPickups, values[0]));
		});

		// Parse cancellations file
		parseFile("cancellations.csv", values -> {
			jobs.get(values[0]).cancelledInTrainingSet = values[1].equals("0") ? false : true;
		});

        List<Location> dropLocations = new ArrayList<>();
        parseFile("drops.csv", values -> {
            if(values.length < 2) return;
            System.out.println(Arrays.toString(values));
            dropLocations.add(new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
        });

		// Convert the job map to a list
		List<Job> jobList = jobs.values().stream().collect(Collectors.toList());
		Location lastDropLocation = dropLocations.get(dropLocations.size() - 1);
        	jobList.forEach(job -> job.dropLocation = lastDropLocation);
        	EventDispatcher.onEvent2(new BeginAssigningEvent(jobList, new LinkedList<>()));
	}
	
        public static void parseFile(String filePath, Consumer<String[]> consumer) throws FileNotFoundException {
		Scanner in = new Scanner(new File(filePath));
		while(in.hasNextLine()) consumer.accept(in.nextLine().trim().split(","));
	}

}
