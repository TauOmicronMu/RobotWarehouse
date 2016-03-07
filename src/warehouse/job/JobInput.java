package warehouse.job;

import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import warehouse.util.ItemPickup;

public class JobInput {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException 
    {
        
        try (BufferedReader reader = new BufferedReader(new FileReader("jobs.csv"))){
        	Scanner scanner = new Scanner(new File("filename.csv"));
        	String id;
        	while(scanner.hasNextLine()) {
        	    String line = scanner.nextLine();
                scanner.useDelimiter(",");
                List<ItemPickup> itemPickupList = new LinkedList<ItemPickup>();
       			if (scanner.hasNext()) {
       				id = scanner.next();
       			}
        		while (scanner.hasNext()) 
                {
        			itemPickupList.add(new ItemPickup(scanner.next(), null, Integer.parseInt(scanner.next())));
                }
                scanner.close();
        	}
        }
    }
}
