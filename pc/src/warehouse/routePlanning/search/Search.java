package warehouse.routePlanning.search;

import java.util.HashMap;

import warehouse.routePlanning.util.Map;
import warehouse.util.Location;

public abstract class Search {
	protected Location[][] map;
	protected HashMap<Location, Boolean> available;

	public Search(Map m) {
		map = m.getMap();
		available = m.getAvailable();
	}

	/**
	 * Returns whether a given location is inside the map or not
	 * 
	 * @param y
	 *            the y coordinate
	 * @param x
	 *            the x coordinate
	 * @return boolean representing whether the location is in the map or not
	 */
	protected boolean inMap(int y, int x) {
		return (y >= 0 && y < map.length && x >= 0 && x < map[y].length);
	}

}
