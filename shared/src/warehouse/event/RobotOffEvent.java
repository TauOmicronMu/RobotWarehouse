package warehouse.event;

/**
 * Event called when a robot has been turned off
 * @author txs
 *
 */
<<<<<<< HEAD

=======
<<<<<<< HEAD

=======
>>>>>>> f9880e34f9ec67cde3372b76601c80975d4624f6
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
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
<<<<<<< HEAD
=======
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf

    public String toPacketString() {
        String s = "";
        s += "RobotOff";
        s += ",";
        s += robot.toPacketString();
        return s;
    }
<<<<<<< HEAD
=======
>>>>>>> f9880e34f9ec67cde3372b76601c80975d4624f6
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
}
