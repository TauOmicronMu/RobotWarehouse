package warehouse.event;

import warehouse.util.Robot;

import java.io.Serializable;

public abstract class Event implements Serializable {

    public final Robot robot;

    public Event(Robot robot) {
        this.robot = robot;
    }
}
