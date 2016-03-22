package warehouse.event;

import warehouse.util.Robot;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class RobotLostEvent extends Event {
    public RobotLostEvent(Robot robot) {
        super(robot);
    }
}
