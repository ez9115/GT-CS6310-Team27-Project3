package base;

/**
 * PresentationMethod is an interface to define a module that would present
 * simulation information to the user. The implementation is only responsible
 * for presenting a single SimulationResult. Threading, synchronization, and
 * other factors are handled in caller.
 * 
 * @author Tyler Benfield
 *
 */
public interface PresentationMethod {

	/**
	 * Method of presenting simulation data to the user.
	 * 
	 * @param result
	 *            The data result of a simulation run.
	 * @throws InterruptedException
	 *             Thrown if the thread executing the presentation is
	 *             interrupted.
	 */
	public void present(SimulationResult result) throws InterruptedException;

	/**
	 * Optional method of pausing mid-presentation. Pausing is handled already
	 * in calling method, but this provides a more granular pause if
	 * implemented.
	 */
	public void pause();

	/**
	 * Optional method of resuming a paused presentation. Should be implemented
	 * if pause() is implemented.
	 */
	public void resume();

}
