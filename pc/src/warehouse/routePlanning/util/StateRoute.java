package warehouse.routePlanning.util;

import java.util.LinkedList;

public class StateRoute {

	private  LinkedList<State> route;
	private int distance;
	
	public StateRoute(LinkedList<State> route, int distance) {
		this.route = route;
		this.distance = distance;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public LinkedList<State> getRoute() {
		return route;
	}
	
	@Override
	public String toString() {
		String toReturn = "Route: \n";
		for(State s: route){
			toReturn += "(" + s.getLocation().x +"," + s.getLocation().y +"), facing: " + s.getFacing() +"\n";
		}
		return toReturn;
	}
}
