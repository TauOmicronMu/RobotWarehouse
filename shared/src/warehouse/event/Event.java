package warehouse.event;

import warehouse.util.Robot;

import java.io.Serializable;
import java.util.Optional;

public abstract class Event implements Serializable {

    public final Optional<Robot> robot;

    public Event(Robot robot) {
        this.robot = robot != null ? Optional.of(robot) : Optional.empty();
    }
}
