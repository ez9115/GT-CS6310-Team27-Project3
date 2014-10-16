package base;

import GUI.widget.earth.TemperatureGrid;

public class SimulationResult implements TemperatureGrid {
	
	private int mCounter;
	private GridData[][] mResult;
	
	public SimulationResult(GridData[][] result) {
		mResult = result;
	}
	
	public SimulationResult(int counter) {
		mCounter = counter;
	}

	@Override
	public double getTemperature(int x, int y) {
		return mResult[x][y].getTemp();
	}

	@Override
	public float getCellHeight(int x, int y) {
		// TODO Auto-generated method stub
		return mCounter;
	}

}
