package warehouse.event;

import warehouse.util.Robot;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class RobotOffEvent extends Event {
    public RobotOffEvent(Robot robot) {
        super(robot);
    }
}
