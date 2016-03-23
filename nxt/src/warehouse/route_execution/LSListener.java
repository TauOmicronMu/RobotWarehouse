package warehouse.route_execution;

	import lejos.nxt.LightSensor;

	public class LSListener extends Thread {

		private LineFollow robotUnit;
		private LightSensor ls;
		private int direction;
		private int backgroundValue;
		public static boolean stop=true;

		public LSListener(LineFollow robotUnit, LightSensor ls, int direction) {
			this.robotUnit = robotUnit;
			this.ls = ls;
			this.direction = direction;
		}

		public void setBackgroundValue(int backgroundValue) {
			this.backgroundValue = backgroundValue;
		}

		@Override
		public void run() {
			int lightValue;
			while(stop) {
				lightValue = ls.getLightValue();
				if (lightValue < backgroundValue - 6) {
					if (direction == LineFollow.LEFT) {
						robotUnit.setGoLeft(true);
					} else {
						robotUnit.setGoRight(true);
					}
				} else {
					if (direction == LineFollow.LEFT) {
						robotUnit.setGoLeft(false);
					} else {
						robotUnit.setGoRight(false);
					}
				}
			}
		}
	}

