package warehouse.event;

import warehouse.action.*;
import warehouse.job.AssignedJob;
import warehouse.job.Job;
import warehouse.util.*;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class Event implements Serializable {

    public final Robot robot;

    public Event(Robot robot) {
        this.robot = robot;
    }

    public static void main(String[] args) {
        String str = "false,2,aa,1,2,3,ab,2,3,4,101";
        Route route = parseRoute(split(str, ','), 0).t;
        System.out.println(route);
    }

    public static Event fromPacketString(String str) {
        String[] values = split(str, ',');
        assert values.length > 0;
        String type = values[0];
        Event event = null;
        Pair<Robot, Integer> robotPair = parseRobot(values, 1);
        if (type.equals("ActionComplete")) {
            Action action = parseAction(values, robotPair.e).t;
            event = new ActionCompleteEvent(robotPair.t, action);
        } else if (type.equals("BeginAssigning"))
            event = new BeginAssigningEvent(parseJobList(values, 1).t, new LinkedList<Location>());
        else if (type.equals("DropOffReached"))
            event = new DropOffReachedEvent("", (AssignedJob) parseJob(values, 1).t);
        else if (type.equals("JobAssignment")) event = new JobAssignedEvent((AssignedJob) parseJob(values, 1).t);
        else if (type.equals("JobCancellation")) event = new JobCancellationEvent((AssignedJob) parseJob(values, 1).t);
        else if (type.equals("PickupComplete"))
            event = new PickupCompleteEvent(robotPair.t, parsePickup(values, robotPair.e).t);
        else if (type.equals("PickupReached"))
            event = new PickupReachedEvent(parsePickup(values, robotPair.e).t, robotPair.t);
        else if (type.equals("RobotLost")) event = new RobotLostEvent(robotPair.t);
        else if (type.equals("RobotOffEvent")) event = new RobotLostEvent(robotPair.t);
        else if (type.equals("WrongPlace")) event = new WrongPlaceEvent(robotPair.t);
        return event;
    }

    private static Pair<ItemPickup, Integer> parsePickup(String[] values, Integer i) {
        String itemName = values[i];
        Pair<Location, Integer> loc = parseLocation(values, i + 1);
        return new Pair<>(new ItemPickup(itemName, loc.t, Integer.parseInt(values[loc.e])), loc.e + 1);
    }

    private static Pair<List<Job>, Integer> parseJobList(String[] values, int i) {
        int size = Integer.parseInt(values[i]);
        List<Job> jobs = new LinkedList<>();
        i++;
        for (int j = 0; j < size; j++) {
            Pair<Job, Integer> pair = parseJob(values, i);
            i = pair.e;
            jobs.add(pair.t);
        }
        return new Pair<>(jobs, i);
    }

    private static Pair<Job, Integer> parseJob(String[] values, int i) {
        boolean isAssigned = Boolean.valueOf(values[i]);
        Pair<List<ItemPickup>, Integer> pickups = parsePickupList(values, i + 1);
        int id = Integer.parseInt(values[pickups.e]);
        if (isAssigned) {
            Pair<Robot, Integer> robot = parseRobot(values, pickups.e + 1);
            Route route = parseRoute(values, robot.e).t;
        }
        return null;
    }

    private static Pair<Route, Integer> parseRoute(String[] values, Integer i) {
        int size = Integer.parseInt(values[i]);
        Pair<List<Action>, Integer> actions = parseActionList(values, i + 1);
        Pair<Location, Integer> loc = parseLocation(values, actions.e), loc2 = parseLocation(values, loc.e);
        Direction direction = Direction.valueOf(values[loc2.e]);
        return new Pair<>(new Route(actions.t, loc.t, loc2.t, direction), loc2.e + 1);
    }

    private static Pair<List<Action>, Integer> parseActionList(String[] values, int i) {
        int size = Integer.parseInt(values[i]);
        i++;
        List<Action> actions = new LinkedList<>();
        for (int j = 0; j < size; j++) {
            Pair<Action, Integer> action = parseAction(values, i);
            i = action.e;
            actions.add(action.t);
        }
        return new Pair<>(actions, i);
    }

    private static Pair<Action, Integer> parseAction(String[] values, int i) {
        String actionType = values[i];
        Action action = null;
        i++;
        if (actionType.equals("Dropoff")) {
            action = new DropoffAction();
            i++;
        } else if (actionType.equals("Idle")) {
            action = new IdleAction(Integer.parseInt(values[i++]));
        } else if (actionType.equals("Move")) {
            action = new MoveAction(Integer.parseInt(values[i]), parseLocation(values, i + 1).t);
            i += 2;
        } else if (actionType.equals("Pickup")) {
            action = new PickupAction();
            i++;
        } else if (actionType.equals("Turn")) {
            action = new TurnAction(Double.parseDouble(values[i++]));
        }
        return new Pair<>(action, i);
    }

    private static Pair<List<ItemPickup>, Integer> parsePickupList(String[] values, int i) {
        int size = Integer.parseInt(values[i]);
        List<ItemPickup> pickups = new LinkedList<>();
        i++;
        for (int j = 0; j < size; j++) {
            Pair<ItemPickup, Integer> pair = parsePickup(values, i);
            i = pair.e;
            pickups.add(pair.t);
        }
        return new Pair<>(pickups, i);
    }

    private static Pair<Location, Integer> parseLocation(String[] values, int start) {
        return new Pair<>(new Location(Integer.parseInt(values[start]), Integer.parseInt(values[start + 1])), start + 2);
    }

    private static Pair<Robot, Integer> parseRobot(String[] values, int start) {
        Pair<Location, Integer> loc = parseLocation(values, start + 1);
        return new Pair<>(new Robot(values[start], loc.t, Direction.valueOf(values[loc.e]), Integer.parseInt(values[loc.e + 1])), loc.e + 2);
    }

    public static String[] split(String str, char delim) {
        List<String> list = new LinkedList<>();
        String progress = "";
        for (char ch : str.toCharArray()) {
            if (ch == delim) {
                if (!progress.isEmpty()) list.add(progress);
                progress = "";
            } else progress += ch;
        }
        if (!progress.isEmpty()) list.add(progress);
        String[] array = new String[list.size()];
        for (int i = 0; i < array.length; i++) array[i] = list.get(i);
        return array;
    }

}
