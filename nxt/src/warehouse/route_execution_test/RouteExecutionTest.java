package warehouse.route_execution_test;

/**
 * Class that tests the behaviour of the robot with simple actions
 * @author Gabriel Iuriciuc
 *
 */
public class RouteExecutionTest {
	
	public static void main(String[] args) {
		LineFollowTest lineFollower = new LineFollowTest();
		int i=4;
		while(i>0){
			lineFollower.moveAction(1);
			lineFollower.turnAction(90);
			i--;
		}
	}
}
