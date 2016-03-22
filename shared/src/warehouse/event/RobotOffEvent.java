package warehouse.event;

/**
 * Event called when a robot has been turned off
 * @author txs
 *
 */
public class RobotOffEvent
{

import warehouse.util.Robot;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class RobotOffEvent extends Event {
    protected RobotOffEvent(Robot robot) {
        super(robot);
    }
}}
