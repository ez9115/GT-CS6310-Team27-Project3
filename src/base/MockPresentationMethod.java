package base;

public class MockPresentationMethod implements PresentationMethod {

	@Override
	public void present(SimulationResult result) throws InterruptedException {
		Thread.sleep(500);
		System.out.println(result.getTemperature(0, 0));
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
