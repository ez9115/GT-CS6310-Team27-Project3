package base;

import simulation.GridData;
import GUI.widget.earth.TemperatureGrid;

/**
 * Contains result data from a single simulation execution.
 * @author Tyler Benfield
 *
 */
public class SimulationResult implements TemperatureGrid {
	
	/**
	 * TODO: Remove this, it is for testing purposes
	 */
	private int mCounter;
	
	/**
	 * Result data
	 */
	private GridData[][] mResult;
	
	/**
	 * Constructs a new SimulationResult with the specified result data.
	 * @param result Data resulting from simulation execution.
	 */
	public SimulationResult(GridData[][] result) {
		mResult = result;
	}
	
	/**
	 * TODO: Remove this, it is for testing purposes
	 */
	public SimulationResult(int counter) {
		mCounter = counter;
	}
	
	/**
	 * Retrieves the temperature of the specified grid cell.
	 */
	@Override
	public double getTemperature(int x, int y) {
		if (mResult != null) {
			return mResult[x][y].getTemp();
		} else {
			return mCounter;
		}
	}

	/**
	 * Retrieves the heigh to the specified grid cell.
	 */
	@Override
	public float getCellHeight(int x, int y) {
		// TODO Auto-generated method stub
		return mCounter;
	}

}
