package warehouse;

import java.util.LinkedList;

/**
 * Created by samtebbs on 22/02/2016.
 */
public class Route {

    private LinkedList<Action> actions;
    private Location start, end;

    public Route(LinkedList<Action> actions, Location start, Location end) {
        this.actions = actions;
        this.start = start;
        this.end = end;
    }
}
