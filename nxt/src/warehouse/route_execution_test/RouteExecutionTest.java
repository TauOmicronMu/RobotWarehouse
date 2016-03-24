package warehouse.route_execution_test;

/**
 * Class that tests the behaviour of the robot with simple actions
 * @author Gabriel Iuriciuc
 *
 */
public class RouteExecutionTest {
	
	public static void main(String[] args) {
		LineFollowTest lineFollower = new LineFollowTest();
		// Constructed an imaginary route to test
			lineFollower.moveAction(4);
			lineFollower.turnAction(90);
			lineFollower.moveAction(5);
			lineFollower.turnAction(-90);
			lineFollower.moveAction(1);
			lineFollower.turnAction(-90);
			lineFollower.moveAction(2);
			lineFollower.turnAction(-90);
	}
}

