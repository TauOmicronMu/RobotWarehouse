package warehouse.util;

public class Location {

    public int x, y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int manhattanDistance(Location loc) {
        return Math.abs(loc.x - x) + Math.abs(loc.y - y);
    }
    
}
