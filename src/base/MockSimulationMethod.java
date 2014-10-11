package base;

public class MockSimulationMethod implements SimulationMethod {
	
	private int mCounter = 0;

	@Override
	public SimulationResult simulate() throws InterruptedException {
		Thread.sleep(100);
		mCounter++;
		return new SimulationResult(mCounter);
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
