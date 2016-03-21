package warehouse.localisation;


import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.navigation.Heading;
/**
 * 
 * @author jokLiu
 *
 */
public class ActionModelModified implements ActionModel {

	private Heading _heading;

	@Override
	public GridPositionDistribution updateAfterMove(GridPositionDistribution _from, Heading _heading) {

		this._heading = _heading;
		// Create the new distribution that will result from applying the action
		// model
		GridPositionDistribution to = new GridPositionDistribution(_from);

		// Move the probability in the correct direction for the action
		if (_heading == Heading.PLUS_X) {

			movePlusX(_from, to);
		} else if (_heading == Heading.PLUS_Y) {

			movePlusY(_from, to);

		} else if (_heading == Heading.MINUS_X) {

			moveMinusX(_from, to);

		} else if (_heading == Heading.MINUS_Y) {

			moveMinusY(_from, to);

		}

		return to;
	}

	/**
	 * Move probabilities from _from one cell in the plus x direction into _to
	 * 
	 * @param _from
	 * @param _to
	 */
	private void movePlusX(GridPositionDistribution _from, GridPositionDistribution _to) {

		

			for (int y = 0; y < _to.getGridHeight(); y++) {
				if(!_to.isObstructed(0, y))
				_to.setProbability(0, y, 0f);
			}
		
		// iterate through points updating as appropriate

		for (int y = 0; y < _to.getGridHeight(); y++) {

			for (int x = 0; x < _to.getGridWidth() - 1; x++) {

				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x+1, y)) {

					// position before move
					int fromX = x;
					int fromY = y;
					float fromProb = _from.getProbability(fromX, fromY);

					// position after move

					int toX = x + 1;
					int toY = y;
					
					_to.setProbability(toX, toY, fromProb);

				}
			}
		}
	}
	
	private void moveMinusY(GridPositionDistribution _from, GridPositionDistribution _to) 
	{
	
		
		for (int x = 0; x < _to.getGridWidth(); x++) {
			
			if (!_to.isObstructed(x, _to.getGridHeight()-1)) {
				_to.setProbability(x, _to.getGridHeight()-1, 0f);
			}
		}

		for (int y = 1; y < _to.getGridHeight(); y++) {

			for (int x = 0; x < _to.getGridWidth(); x++) {

				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x, y-1)) {
					// position before move
					int fromX = x;
					int fromY = y;
					float fromProb = _from.getProbability(fromX, fromY);

					// position after move
					int toX = x;
					int toY = y-1;

					_to.setProbability(toX, toY, fromProb);

				}
			}
		}
	}

	private void moveMinusX(GridPositionDistribution _from, GridPositionDistribution _to) 
	{
		for (int y = 0; y < _to.getGridHeight(); y++) {
			if(!_to.isObstructed(_to.getGridWidth()-1, y))
			_to.setProbability(_to.getGridWidth()-1, y, 0f);
		}
		
		for (int y = 0; y < _to.getGridHeight(); y++) {

			for (int x = 1; x < _to.getGridWidth(); x++) {

				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x-1, y)) {
					
					// position before move
					int fromX = x;
					int fromY = y;
					float fromProb = _from.getProbability(fromX, fromY);

					// position after move
					int toX = x-1;
					int toY = y;

					_to.setProbability(toX, toY, fromProb);

				}
			}
		}
	}
	
	private void movePlusY(GridPositionDistribution _from, GridPositionDistribution _to) {

		for (int x = 0; x < _to.getGridWidth(); x++) {
			if (!_to.isObstructed(x, 0)) {
				_to.setProbability(x, 0, 0f);
			}
		}

		for (int y = 0; y < _to.getGridHeight() - 1; y++) {

			for (int x = 0; x < _to.getGridWidth(); x++) {

				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x, y+1)) {
					
					// position before move
					int fromX = x;
					int fromY = y;
					float fromProb = _from.getProbability(fromX, fromY);

					// position after move
					int toX = x;
					int toY = y + 1;

					_to.setProbability(toX, toY, fromProb);

				}
			}
		}
	}
}
