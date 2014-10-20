package callbacks;

/**
 * Generic callback for a start action.
 * @author Tyler Benfield
 *
 */
public interface OnStart {

	/**
	 * Called when the caller is started.
	 * @throws Exception
	 */
	public void onStart(int degreeSeparation, int timeStep, int displayRate) throws Exception;
	
}
