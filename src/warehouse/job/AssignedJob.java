package warehouse.job;

import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Robot;
import warehouse.util.Route;

import java.util.LinkedList;

public class AssignedJob extends Job {

    private Route route;
    private Robot robot;

    public AssignedJob(Location dropLocation, LinkedList<ItemPickup> pickups, Route route, Robot robot) {
        super(dropLocation, pickups);
        this.route = route;
        this.robot = robot;
    }

    public AssignedJob(Job job, Route route, Robot robot) {
        this(job.dropLocation, job.pickups, route, robot);
    }
}
