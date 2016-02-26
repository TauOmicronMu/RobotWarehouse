package warehouse.job;

import warehouse.ItemPickup;
import warehouse.Location;
import warehouse.Robot;
import warehouse.Route;

import java.util.LinkedList;

public class AssignedJob extends Job {

    public Route route;
    public Robot robot;

    public AssignedJob(Location dropLocation, LinkedList<ItemPickup> pickups, Route route, Robot robot) {
        super(dropLocation, pickups);
        this.route = route;
        this.robot = robot;
    }

    public AssignedJob(Job job, Route route, Robot robot) {
        this(job.dropLocation, job.pickups, route, robot);
    }

}
