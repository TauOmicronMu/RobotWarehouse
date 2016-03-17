package warehouse.util;

public class Robot {

    public String robotName;
    public Location position;
    public Direction rotation;

    public Robot(String robotName, Location position, Direction rotation) {
        this.robotName = robotName;
        this.position = position;
        this.rotation =  rotation;
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
