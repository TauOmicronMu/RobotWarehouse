package warehouse.job;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import warehouse.util.*;

public class JobInput {
	
	static {
		EventDispatcher.subscribe2(JobInput.class);
	}
	
	public static void main(String[] args) throws IOException {
		// Parse locations
		Scanner scanner = new Scanner(new File("locations.csv"));
		HashMap<String, Location> itemLocations = new HashMap<>();
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] values = line.split(",");
			itemLocations.put(values[0], new Location(Integer.parseInt(values[1]), Integer.parseInt(values[2])));
		}

		// Parse items file
		HashMap<String, ItemPickup> itemPickups = new HashMap<>();
		scanner = new Scanner(new File("items.csv"));
		while(scanner.hasNextLine()) {
			String[] values = scanner.nextLine().split(",");
			itemPickups.put(values[0], new ItemPickup(values[0], itemLocations.get(values[0]), 0));
		}

		// Parse jobs file
		scanner = new Scanner(new File("jobs.csv"));
		HashMap<String, Job> jobs = new HashMap<>();
		while(scanner.hasNextLine()) {
			String[] values = scanner.nextLine().split(",");
			List<ItemPickup> jobPickups = new LinkedList<>();
			for(int i = 1; i < values.length; i += 2) {
				ItemPickup p = (ItemPickup) itemPickups.get(values[i]).clone();
				p.itemCount = Integer.parseInt(values[i+1]);
				jobPickups.add(p);
			}
			jobs.put(values[0], new Job(null, jobPickups));
		}

		// Parse cancellations file (I'm not sure of the actual file name)
		scanner = new Scanner("cancellations.csv");
		while(scanner.hasNextLine()) {
			String[] values = scanner.nextLine().split(",");
			jobs.get(values[0]).cancelledInTrainingSet = values[1].equals("0") ? false : true;
		}

		// Convert the job map to a list
		List<Job> jobList = jobs.values().stream().collect(Collectors.toList());
		EventDispatcher.onEvent2(new BeginAssigningEvent(jobList));
	}
}
