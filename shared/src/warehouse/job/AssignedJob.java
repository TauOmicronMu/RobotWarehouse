package warehouse.job;

import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Robot;
import warehouse.util.Route;

import java.util.List;

public class AssignedJob extends Job {

    public Route route;
    public Robot robot;

    public AssignedJob(Location dropLocation, List<ItemPickup> pickups, Route route, Robot robot) {
        super(dropLocation, pickups);
        this.route = route;
        this.robot = robot;
    }

    public AssignedJob(Job job, Route route, Robot robot) {
        this(job.dropLocation, job.pickups, route, robot);
    }

    @Override
    public boolean isAssigned() {
        return true;
    }

    @Override
    public String toString() {
        return "AssignedJob{" +
                "route=" + route +
                ", robot=" + robot +
                '}';
    }
}
