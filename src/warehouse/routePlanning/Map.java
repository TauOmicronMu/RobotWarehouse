package warehouse.routePlanning;

import java.util.HashMap;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import warehouse.Location;

public class Map {
	private HashMap<Location, Boolean> available;
	private Location [][] map;
	public Map(){
		GridMap providedMap = MapUtils.createRealWarehouse();
		available = new HashMap<Location, Boolean>();
		int sizeY = providedMap.getYSize();
		int sizeX = providedMap.getXSize();
		map = new Location [sizeY][sizeX];
		for(int y = 0; y < sizeY; y++){
			for(int x = 0; x < sizeX; x++){
				Location temp = new Location(x, (sizeY - 1) - y);
				map[(sizeY - 1) - y][x] = temp;
				if(providedMap.isObstructed(x, y)){
					available.put(temp, false);
				}else{
					available.put(temp, true);
				}
			}
		}
	}
	
	public HashMap<Location, Boolean> getAvailable() {
		return available;
	}
	
	public Location[][] getMap() {
		return map;
	}
}
