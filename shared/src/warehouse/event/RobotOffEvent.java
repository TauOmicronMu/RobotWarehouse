package warehouse.event;

/**
 * Event called when a robot has been turned off
 * @author txs
 *
 */

import warehouse.util.Robot;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class RobotOffEvent extends Event {
    public RobotOffEvent(Robot robot) {
        super(robot);
    }

    public String toPacketString() {
        String s = "";
        s += "RobotOff";
        s += ",";
        s += robot.toPacketString();
        return s;
    }
}
