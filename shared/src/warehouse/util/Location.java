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

    @Override
    public String toString() {
        return toPacketString();
    }

    public String toPacketString() {
        return x + "," + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (x != location.x) return false;
        return y == location.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
