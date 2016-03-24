package warehouse.routePlanning.search;

import warehouse.util.Direction;
import warehouse.util.Location;

public class State {
	private Location location;
	private Direction facing;

	public State(Location location, Direction facing) {
		this.location = location;
		this.facing = facing;
	}

	public Location getLocation() {
		return location;
	}

	public Direction getFacing() {
		return facing;
	}
}