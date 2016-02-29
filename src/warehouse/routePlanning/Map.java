package warehouse.routePlanning;

import java.util.HashMap;

import rp.robotics.mapping.GridMap;
import rp.robotics.testing.TestMaps;
import warehouse.Location;

public class Map {
	private HashMap<Location, Boolean> available;
	private Location [][] map;
	public Map(){
		GridMap providedMap = TestMaps.warehouseMap();
		available = new HashMap<Location, Boolean>();
		map = new Location [providedMap.getYSize() + 2][providedMap.getXSize() + 2];
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map[y].length; x++){
				map[y][x] = new Location(x, y);
				if(providedMap.isObstructed(x -1, y -1)){
					System.out.print("X");
					available.put(map[y][x], false);
				}else{
					System.out.print("-");
					available.put(map[y][x], true);
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
