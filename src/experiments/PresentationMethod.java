package experiments;

public interface PresentationMethod {
	
	public void present(SimulationResult result) throws InterruptedException;
	
	public void pause();
	
	public void resume();
	

}
