package simulation;

import base.SimulationMethod;
import base.SimulationResult;

public class SimulationMethodImpl implements SimulationMethod {
	public SimulationResult simulate() throws InterruptedException {
		Thread.sleep(250);
//		mCounter++;
		return new SimulationResult(0); //288k is the starting temp
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
