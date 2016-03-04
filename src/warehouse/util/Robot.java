package warehouse.util;

public class Robot {

    public String robotName;
    public Location position;
    public double rotation;

    public Robot(String robotName, Location position) {
        this.robotName = robotName;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Robot{" +
                "robotName='" + robotName + '\'' +
                ", position=" + position +
                ", rotation=" + rotation +
                '}';
    }
}