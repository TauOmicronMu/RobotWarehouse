package warehouse.localisation;



import lejos.robotics.RangeReadings;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.localisation.SensorModel;
import rp.robotics.navigation.Heading;

/**
 * 
 * @author jokLiu
 *
 */
public class SensorModelModified implements SensorModel {

	private RangeReadings readings;
	private Distances dist;
	
	private double constant = 2;

	public SensorModelModified(Distances dist) {
		this.dist = dist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rp.robotics.localisation.SensorModel#updateAfterSensing(rp.robotics.
	 * localisation.GridPositionDistribution, rp.robotics.mapping.Heading,
	 * lejos.robotics.RangeReadings)
	 */
	@Override
	public GridPositionDistribution updateAfterSensing(GridPositionDistribution _from, Heading _heading,
			RangeReadings _readings) {
		readings = _readings;

		GridPositionDistribution to = new GridPositionDistribution(_from);
		
		// Move the probability in the correct direction for the action
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

	private void headPlusY(GridPositionDistribution _from, GridPositionDistribution to) {
		float read = readings.get(0).getRange();
		if (readings.get(0).getRange() <= 2.5f) {

			for (int y = 0; y < to.getGridHeight(); y++) {

				for (int x = 0; x < to.getGridWidth() ; x++) {
					if ((!to.isObstructed(x, y))) {
						if (read > dist.getPlusY(x, y)) {
							if(read > dist.getPlusY(x, y)*constant)
							to.setProbability(x, y, 0f);
						}
						else 
						{
							if(read < dist.getPlusY(x, y)/constant)
							{
								to.setProbability(x, y, 0f);
							}
						}
					}
				}

			}
		}
	}
	
	private void headPlusX(GridPositionDistribution _from, GridPositionDistribution to) {
		float read = readings.get(0).getRange();
		if (readings.get(0).getRange() <= 2.5f) {

			for (int y = 0; y < to.getGridHeight(); y++) {

				for (int x = 0; x < to.getGridWidth(); x++) {
					if ((!to.isObstructed(x, y))) {
						if (read > dist.getPlusX(x, y)) {
							if(read > dist.getPlusX(x, y)*constant)
							to.setProbability(x, y, 0f);
						}
						else 
						{
							if(read < dist.getPlusX(x, y)/constant)
							{
								to.setProbability(x, y, 0f);
							}
						}
					}
				}

			}
		}
	}

	private void headMinusX(GridPositionDistribution _from, GridPositionDistribution to) {
		float read = readings.get(0).getRange();
		if (readings.get(0).getRange() <= 2.5f) {

			for (int y = 0; y < to.getGridHeight(); y++) {

				for (int x = 0; x < to.getGridWidth() ; x++) {
					if ((!to.isObstructed(x, y))) {
						if (read > dist.getMinusX(x, y)) {
							if(read > dist.getMinusX(x, y)*constant)
							to.setProbability(x, y, 0f);
						}
						else 
						{
							if(read < dist.getMinusX(x, y)/constant)
							{
								to.setProbability(x, y, 0f);
							}
						}
					}
				}

			}
	}
	}
	
	private void headMinusY(GridPositionDistribution _from, GridPositionDistribution to) {
		float read = readings.get(0).getRange();
		if (readings.get(0).getRange() <= 2.5f) {

			for (int y = 0; y < to.getGridHeight(); y++) {

				for (int x = 0; x < to.getGridWidth() ; x++) {
					if ((!to.isObstructed(x, y))) {
						if (read > dist.getMinusY(x, y)) {
							if(read > dist.getMinusY(x, y)*constant)
							to.setProbability(x, y, 0f);
						}
						else 
						{
							if(read < dist.getMinusY(x, y)/constant)
							{
								to.setProbability(x, y, 0f);
							}
						}
					}
				}

			}
		}
	}
}
