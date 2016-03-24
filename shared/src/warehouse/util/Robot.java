package warehouse.util;

public class Robot {

    public String robotName;
    public Location position;
    public Direction rotation;
    public int id;

    public Robot(String robotName, Location position, Direction rotation, int id) {
        this.robotName = robotName;
        this.position = position;
        this.rotation =  rotation;
        this.id = id;
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
