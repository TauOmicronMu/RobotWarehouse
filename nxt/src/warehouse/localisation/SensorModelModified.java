package warehouse.localisation;

import lejos.robotics.RangeReadings;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.localisation.SensorModel;
import rp.robotics.navigation.Heading;


/**
 * Sensor model class.
 *
 * @author jokLiu
 */
public class SensorModelModified implements SensorModel {


	// range readings and distances to all the objects in the map
	private RangeReadings readings;
	
	/** The distances to all the object in the map. */
	private Distances dist;

	/** The constant. */
	private double constant = 2;

	/**
	 * Instantiates a new sensor model modified.
	 *
	 * @param dist the dist
	 */
	public SensorModelModified(Distances dist) {
		this.dist = dist;
	}

	/* (non-Javadoc)
	 * @see rp.robotics.localisation.SensorModel#updateAfterSensing(rp.robotics.localisation.GridPositionDistribution, rp.robotics.navigation.Heading, lejos.robotics.RangeReadings)
	 */
	/*
	 * update the probability distribution after using distance sensors
	 */
	@Override
	public GridPositionDistribution updateAfterSensing(GridPositionDistribution _from, Heading _heading,
			RangeReadings _readings) {
		readings = _readings;

		GridPositionDistribution to = new GridPositionDistribution(_from);

		// Change the probabilities according to the direction which the robot is facing
		//and depending on the distance reading
		if (_heading == Heading.PLUS_X) {
			headPlusX(_from, to);
		} else if (_heading == Heading.PLUS_Y) {
			headPlusY(_from, to);

		} else if (_heading == Heading.MINUS_X) {
			headMinusX(_from, to);
		} else if (_heading == Heading.MINUS_Y) {
			headMinusY(_from, to);
		}

		return to;

	}

	/**
	 * Change probabilities after the readings (Direction Plus Y)
	 *
	 * @param _from the _from
	 * @param to the to
	 */
	private void headPlusY(GridPositionDistribution _from, GridPositionDistribution to) {
		
		//get the reading from the readings object
		float read = readings.get(0).getRange();
		
		//update probabilities only if the distance is in the sensor range
		if (readings.get(0).getRange() <= 2.5f) {

			for (int y = 0; y < to.getGridHeight(); y++) {

				for (int x = 0; x < to.getGridWidth(); x++) {
					
					//update probabilities only if the point is not obstructed by the object
					if ((!to.isObstructed(x, y))) {
						
						//if the reading is bigger than the distance in the map
						if (read > dist.getPlusY(x, y)) {
							
							//if the reading is bigger at the particular rate
							//set the probability of that point to 0
							if (read > dist.getPlusY(x, y) * constant)
								to.setProbability(x, y, 0f);
						}
						//if the reading is smaller than the distance in the map
						else {
							//if the reading is smaller at the particular rate
							//set the probability of that point to 0
							if (read < dist.getPlusY(x, y) / constant) {
								to.setProbability(x, y, 0f);
							}
						}
					}
				}

			}
		}
	}

	/**
	 * Change probabilities after the readings (Direction Plus X)
	 *
	 * @param _from the _from
	 * @param to the to
	 */
	private void headPlusX(GridPositionDistribution _from, GridPositionDistribution to) {
		//get the reading from the readings object
		float read = readings.get(0).getRange();
		
		//update probabilities only if the distance is in the sensor range
		if (readings.get(0).getRange() <= 2.5f) {

			for (int y = 0; y < to.getGridHeight(); y++) {

				for (int x = 0; x < to.getGridWidth(); x++) {
					
					//update probabilities only if the point is not obstructed by the object
					if ((!to.isObstructed(x, y))) {
						
						//if the reading is bigger than the distance in the map
						if (read > dist.getPlusX(x, y)) {
							
							//if the reading is bigger at the particular rate
							//set the probability of that point to 0
							if (read > dist.getPlusX(x, y) * constant)
								to.setProbability(x, y, 0f);
						} 
						//if the reading is smaller than the distance in the map
						else {
							
							//if the reading is smaller at the particular rate
							//set the probability of that point to 0
							if (read < dist.getPlusX(x, y) / constant) {
								to.setProbability(x, y, 0f);
							}
						}
					}
				}

			}
		}
	}

	/**
	 * Change probabilities after the readings (Direction Minus X)
	 *
	 * @param _from the _from
	 * @param to the to
	 */
	private void headMinusX(GridPositionDistribution _from, GridPositionDistribution to) {
		//get the reading from the readings object
		float read = readings.get(0).getRange();
		
		//update probabilities only if the distance is in the sensor range
		if (readings.get(0).getRange() <= 2.5f) {

			for (int y = 0; y < to.getGridHeight(); y++) {

				for (int x = 0; x < to.getGridWidth(); x++) {
					
					//update probabilities only if the point is not obstructed by the object
					if ((!to.isObstructed(x, y))) {
						
						//if the reading is bigger than the distance in the map
						if (read > dist.getMinusX(x, y)) {
							
							//if the reading is bigger at the particular rate
							//set the probability of that point to 0
							if (read > dist.getMinusX(x, y) * constant)
								to.setProbability(x, y, 0f);
						} 
						//if the reading is smaller than the distance in the map
						else {
							
							//if the reading is smaller at the particular rate
							//set the probability of that point to 0
							if (read < dist.getMinusX(x, y) / constant) {
								to.setProbability(x, y, 0f);
							}
						}
					}
				}

			}
		}
	}

	/**
	 * Change probabilities after the readings (Direction Minus Y)
	 *
	 * @param _from the _from
	 * @param to the to
	 */
	private void headMinusY(GridPositionDistribution _from, GridPositionDistribution to) {
		//get the reading from the readings object
		float read = readings.get(0).getRange();
		
		//update probabilities only if the distance is in the sensor range
		if (readings.get(0).getRange() <= 2.5f) {

			for (int y = 0; y < to.getGridHeight(); y++) {

				for (int x = 0; x < to.getGridWidth(); x++) {
					
					//update probabilities only if the point is not obstructed by the object
					if ((!to.isObstructed(x, y))) {
						//if the reading is bigger than the distance in the map
						if (read > dist.getMinusY(x, y)) {
							
							//if the reading is bigger at the particular rate
							//set the probability of that point to 0
							if (read > dist.getMinusY(x, y) * constant)
								to.setProbability(x, y, 0f);
						} 
						//if the reading is smaller than the distance in the map
						else {
							
							//if the reading is smaller at the particular rate
							//set the probability of that point to 0
							if (read < dist.getMinusY(x, y) / constant) {
								to.setProbability(x, y, 0f);
							}
						}
					}
				}

			}
		}
	}
}
