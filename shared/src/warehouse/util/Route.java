package warehouse.util;

import warehouse.action.Action;

import java.util.LinkedList;

public class Route {

    public LinkedList<Action> actions;
    public Location start, end;
    public int totalDistance;
    public Direction finalFacing;

    public Route(LinkedList<Action> actions, Location start, Location end, Direction finalFacing) {
        this.actions = actions;
        this.start = start;
        this.end = end;
        this.finalFacing = finalFacing;
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
