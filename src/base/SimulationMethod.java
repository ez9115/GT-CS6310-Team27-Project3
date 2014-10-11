package base;

public interface SimulationMethod {

	public SimulationResult simulate() throws InterruptedException;
	
	public void pause();
	
	public void resume();
	
}
