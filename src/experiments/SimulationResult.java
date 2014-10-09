package experiments;

import cs6310.gui.widget.earth.TemperatureGrid;

public class SimulationResult implements TemperatureGrid {
	
	private int mCounter;
	
	public SimulationResult(int counter) {
		mCounter = counter;
	}

	@Override
	public double getTemperature(int x, int y) {
		// TODO Auto-generated method stub
		return mCounter;
	}

	@Override
	public float getCellHeight(int x, int y) {
		// TODO Auto-generated method stub
		return mCounter;
	}

}
