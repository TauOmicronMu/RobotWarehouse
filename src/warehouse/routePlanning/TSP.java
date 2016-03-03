package warehouse.routePlanning;

import java.util.LinkedList;

import warehouse.Action;
import warehouse.Location;
import warehouse.Route;
import warehouse.job.Job;

public class TSP {
	private Search s = new Search();
	private LinkedList<Action> moves;

	public Route getShortestRoute(Job j, Location startingPosition) {
		int numberOfNodes = j.pickups.size() + 2;
		double[][] adjacencyMatrix = new double[numberOfNodes][numberOfNodes];
		moves = new LinkedList<Action>();

		return new Route(moves, startingPosition, j.dropLocation);
	}

	/**
	 * Method which calculates a lower bound for a subset of possible routes
	 *
	 * @param adjacencyMatrix
	 *            The adjacency matrix which stores all of the connections
	 *            between pickup locations
	 * @return lowerBound - The lowest possible weight of that a subset of
	 *         possible routes could have
	 */
	private double findLowerBound(double[][] adjacencyMatrix) {
		// finds the lower bound for the rest of the route from the current node
		// by looking through current adjacency matrix.
		double lowerBound = 0;
		for (int currentNode = 0; currentNode < adjacencyMatrix.length; currentNode++) {
			double bestWeight = Double.POSITIVE_INFINITY;
			double secondWeight = Double.POSITIVE_INFINITY;

			// most wanted loop for two lowest weights
			for (int connectedNode = 0; connectedNode < adjacencyMatrix.length; connectedNode++) {
				double foundWeight = adjacencyMatrix[currentNode][connectedNode];
				if (foundWeight < secondWeight) {
					if (foundWeight < bestWeight) {
						secondWeight = bestWeight;
						bestWeight = foundWeight;
					} else {
						secondWeight = foundWeight;
					}
				}
			}
			lowerBound = lowerBound + secondWeight + bestWeight;
		}
		lowerBound = lowerBound / 2;
		return lowerBound;
	}
}