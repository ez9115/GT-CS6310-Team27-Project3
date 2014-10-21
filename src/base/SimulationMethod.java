package base;

/**
 * SimulationMethod is an interface to define a module that would execute a
 * single simulation run. The implementation is only responsible for executing a
 * single simulation and generating result data. Threading, synchronization, and
 * other factors are handled in caller.
 * 
 * @author Tyler Benfield
 *
 */
public interface SimulationMethod {

	/**
	 * Algorithm for executing a single simulation run.
	 * 
	 * @param previousResults
	 *            The results from the previous simulation execution that serve
	 *            as a starting point for this execution
	 * @param degreeSeparation
	 *            The degree of separation between grid cells (0 - 180)
	 * @param sunPosition
	 *            The current sun position (-180 - 180)
	 * @return Resulting simulation data
	 * @throws InterruptedException
	 *             Thrown if the thread executing the simulation is interrupted.
	 */
	public SimulationResult simulate(SimulationResult previousResults,
			int degreeSeparation, float sunPosition)
			throws InterruptedException;

	/**
	 * Optional method of pausing mid-simulation. Pausing is handled already in
	 * calling method, but this provides a more granular pause if implemented.
	 */
	public void pause();

	/**
	 * Optional method of resuming a paused simulation. Should be implemented if
	 * pause() is implemented.
	 */
	public void resume();

}
