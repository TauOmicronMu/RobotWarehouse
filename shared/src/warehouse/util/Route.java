package warehouse.util;

import warehouse.action.Action;

import java.util.List;

public class Route {

    public List<Action> actions;
    public Location start, end;
    public int totalDistance;
    public Direction finalFacing;

    public Route(List<Action> actions, Location start, Location end, Direction finalFacing) {
        this.actions = actions;
        this.start = start;
        this.end = end;
        this.finalFacing = finalFacing;
    }

    @Override
    public String toString() {
        return "Route{" +
                "actions=" + actions +
                ", start=" + start +
                ", end=" + end +
                ", totalDistance=" + totalDistance +
                ", finalFacing=" + finalFacing +
                '}';
    }

    public String toPacketString() {
        String s = "";
        s += actions.size();
        s += ",";
        for(Action a : actions) {
            s += a.toPacketString();
            s += ",";
        }
        s += start.toPacketString();
        s += ",";
        s += end.toPacketString();
        s += ",";
        s += finalFacing.toPacketString();
        return s;
    }
}
