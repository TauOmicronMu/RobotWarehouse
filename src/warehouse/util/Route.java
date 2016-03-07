package warehouse.util;

import warehouse.action.Action;

import java.util.LinkedList;

public class Route {

    public LinkedList<Action> actions;
    public Location start, end;
    public int totalDistance;
    public Direction finalFacing;

    public Route(LinkedList<Action> actions, Location start, Location end) {
        this.actions = actions;
        this.start = start;
        this.end = end;
    }
    
    public void addAction(Action action) {
        actions.add(action);
    }
    
}
