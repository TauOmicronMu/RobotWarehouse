package warehouse.main;

import warehouse.event.BeginAssigningEvent;
import warehouse.job.AssignedJob;
import warehouse.job.JobInput;
import warehouse.management_interface.Main;
import warehouse.networking.WarehouseServer;
import warehouse.util.Robot;
import warehouse.util.Subscriber;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sxt567 on 23/03/16.
 */
public class WarehouseMain {

    private static WarehouseServer server;

    public static void main(String[] args) throws IOException {
        // Start server
        server = new WarehouseServer();
        // Start job input
        JobInput.launch();
    }

    @Subscriber
    public static void onBeginAssigning(BeginAssigningEvent event) {
        List<String> robotNames = new LinkedList<>();
        for(Robot robot : server.robots) if(robot != null) robotNames.add(robot.robotName);
        Main.setParameters(event.jobs, new LinkedList<AssignedJob>(), robotNames);
        Main.startGUI();
    }

}
