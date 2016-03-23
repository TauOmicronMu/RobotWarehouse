package warehouse.management_interface;

import java.util.LinkedList;
import java.util.List;

import warehouse.job.AssignedJob;
import warehouse.job.Job;

/**
 * This class provides an example as to how to launch the management interface from outside
 * @author aml
 *
 */

public class LaunchGUIFromOutside {

	public static void main(String[] args) {
		List<String> robotNames = new LinkedList<String>();
		robotNames.add("Andrei");
		robotNames.add("Sam");
		robotNames.add("Tom");
		
		Main.setParameters(new LinkedList<Job>(), new LinkedList<AssignedJob>(), robotNames);
		Main.startGUI();
		

	}

}
