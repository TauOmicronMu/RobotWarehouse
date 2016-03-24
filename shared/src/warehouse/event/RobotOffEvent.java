package warehouse.event;

/**
 * Event called when a robot has been turned off
 * @author txs
 *
 */
<<<<<<< HEAD

=======
>>>>>>> f9880e34f9ec67cde3372b76601c80975d4624f6
import warehouse.util.Robot;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class RobotOffEvent extends Event {
    public RobotOffEvent(Robot robot) {
        super(robot);
    }
<<<<<<< HEAD
=======

    public String toPacketString() {
        String s = "";
        s += "RobotOff";
        s += ",";
        s += robot.toPacketString();
        return s;
    }
>>>>>>> f9880e34f9ec67cde3372b76601c80975d4624f6
}
