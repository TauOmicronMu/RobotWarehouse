package warehouse.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import warehouse.util.ItemPickup;

public class JobInput {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		// Parse locations
        	Scanner scanner = new Scanner(new File("locations.csv"));
        	HashMap<String, Location> itemLocations = new HashMap<>();
        	while(scanner.hasNextLine()) {
        		String line = scanner.nextLine();
       			String[] values = line.split(",");
       			// Add the item location to the hash map using the values array
       			// values[0] = item name, values[1] = x coordinate, values[2] = y coordinate
        	}
        	
        	// Parse items file and intitialise each item pickup with the locations from itemLocations
        	// ...
        	
        	// Parse jobs file and get ach pickup from the hash map created when parsing the items file
        	// ...
        }
    }
}
