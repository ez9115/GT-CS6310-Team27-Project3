package base;

import simulation.GridData;
import GUI.widget.earth.TemperatureGrid;

/**
 * Contains result data from a single simulation execution.
 * 
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
	 * Sun position
	 */
	private float mSunPosition;

	/**
	 * Constructs a new SimulationResult with the specified result data.
	 * 
	 * @param result
	 *            Data resulting from simulation execution.
	 */
	public SimulationResult(GridData[][] result, float sunPosition) {
		mResult = result;
		mSunPosition = sunPosition;
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
	 * Retrieves the height to the specified grid cell.
	 */
	@Override
	public float getCellHeight(int x, int y) {
		// TODO Auto-generated method stub
		return mCounter;
	}

	/**
	 * Retrieves the position of the sun that produced this result data.
	 * 
	 * @return The position of the sun in degrees
	 */
	public float getSunPosition() {
		// Note that -180 is near Russia, 180 is near Alaska, 0 is center
		return mSunPosition;
	}

	/**
	 * Retrieves the temperature spread of the result data.
	 * 
	 * @return
	 */
	public MinMaxTemp getMinMaxTemperature() {
		MinMaxTemp ret = new MinMaxTemp();
		ret.Min = Double.MAX_VALUE;
		ret.Max = Double.MIN_VALUE;
		for (int i = 0; i < mResult.length; i++) {
			for (int j = 0; j < mResult[i].length; j++) {
				double cell = mResult[i][j].getTemp();
				if (cell < ret.Min) {
					ret.Min = cell;
				}
				if (cell > ret.Max) {
					ret.Max = cell;
				}
			}
		}
		return ret;
	}

	public class MinMaxTemp {
		public double Min;
		public double Max;
	}

}
