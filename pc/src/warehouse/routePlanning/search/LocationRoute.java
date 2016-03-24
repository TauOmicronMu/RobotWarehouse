package warehouse.routePlanning.search;

import java.util.LinkedList;

import warehouse.util.Location;

public class LocationRoute {
	private  LinkedList<Location> route;
	private int distance;
	
	public LocationRoute(LinkedList<Location> route, int distance) {
		this.route = route;
		this.distance = distance;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public LinkedList<Location> getRoute() {
		return route;
	}
	
	@Override
	public String toString() {
		String toReturn = "Route: \n";
		for(Location l: route){
			toReturn += "(" + l.x + "," +l.y +")\n";
		}
		return toReturn;
	}
	
}
