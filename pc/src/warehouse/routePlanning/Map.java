package warehouse.routePlanning;

import java.util.HashMap;

import rp.robotics.mapping.GridMap;
import warehouse.util.Location;

public class Map {
	private HashMap<Location, Boolean> available;
	private Location[][] map;

	public Map(GridMap providedMap) {
		available = new HashMap<Location, Boolean>();
		int sizeY = providedMap.getYSize();
		int sizeX = providedMap.getXSize();
		map = new Location[sizeY][sizeX];
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				Location temp = new Location(x, y);
				map[y][x] = temp;
				if (providedMap.isObstructed(x, y)) {
					System.out.print("X");
					available.put(temp, false);
				} else {
					System.out.print("-");
					available.put(temp, true);
				}
			}
			System.out.println();
		}
	}

	public HashMap<Location, Boolean> getAvailable() {
		return available;
	}

	public Location[][] getMap() {
		return map;
	}
}