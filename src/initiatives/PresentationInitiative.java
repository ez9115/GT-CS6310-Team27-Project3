package initiatives;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import base.PausableStoppable;
import base.PresentationMethod;
import base.SimulationResult;
import base.Utils;
import callbacks.OnStart;
import callbacks.OnStop;

public class PresentationInitiative extends PausableStoppable {

	private final static Logger LOGGER = Logger
			.getLogger(SimulationInitiative.class.getName());

	/**
	 * OnStart callback to execute IN PLACE OF the standard start call.
	 */
	private OnStart mOnStart;

	/**
	 * Whether the OnStart callback is enabled or disabled. Useful when the
	 * OnStart callback contains a call to the start() method and you do not
	 * want an infinite loop.
	 */
	private boolean mOnStartEnabled = true;

	/**
	 * OnStop callback to execute when the thread is stopped.
	 */
	private OnStop mOnStop;

	/**
	 * The presentation method implementation to execute when the queue contains
	 * a simulation result.
	 */
	private PresentationMethod mPresentationMethod;

	/**
	 * The shared data queue to add simulation result data to.
	 */
	private BlockingQueue<SimulationResult> mQueue;

	/**
	 * Creates a new PresentationInitiative with the specified shared data queue
	 * and presentation method implementation.
	 * 
	 * @param queue
	 *            The shared data queue. Data to present will be pulled from
	 *            this queue.
	 * @param presentationMethod
	 *            The presentation method implementation to execute when data is
	 *            ready to present.
	 */
	public PresentationInitiative(BlockingQueue<SimulationResult> queue,
			PresentationMethod presentationMethod) {
		mPresentationMethod = presentationMethod;
		mQueue = queue;
		LOGGER.info("Presentation initialized");
	}

	/**
	 * Disables the OnStart listener.
	 */
	public void disableOnStartListener() {
		mOnStartEnabled = false;
	}

	/**
	 * Enables the OnStart listener.
	 */
	public void enableOnStartListener() {
		mOnStartEnabled = true;
	}

	/**
	 * Presents a SimulationResult.
	 * 
	 * @param simulationResult
	 *            The simulation data to present.
	 * @throws InterruptedException
	 *             Thrown if the current thread is interrupted while waiting for
	 *             simulation data to enter the queue.
	 */
	public void present(SimulationResult simulationResult)
			throws InterruptedException {
		mPresentationMethod.present(simulationResult);
	}

	/**
	 * Sets the OnStart listener. Note: This replaces the typical start()
	 * behavior.
	 * 
	 * @param onStart
	 */
	public void setOnStartListener(OnStart onStart) {
		mOnStart = onStart;
	}

	/**
	 * Sets the OnStop listener.
	 * 
	 * @param onStop
	 */
	public void setOnStopListener(OnStop onStop) {
		mOnStop = onStop;
	}

	/**
	 * Begins the presentation process on a thread.
	 */
	@Override
	public void start(int degreeSeparation, int timeStep, int displayRate)
			throws Exception {
		LOGGER.info("Starting presentation");
		if (mOnStart != null && mOnStartEnabled) {
			mDegreeSeparation = degreeSeparation;
			mTimeStep = timeStep;
			mOnStart.onStart(degreeSeparation, timeStep, displayRate);
		} else {
			super.start(degreeSeparation, timeStep, displayRate);
		}
	}

	/**
	 * Pauses the presentation thread if it is executing.
	 */
	@Override
	public void pause() throws Exception {
		LOGGER.info("Pausing presentation");
		super.pause();
		mPresentationMethod.pause();
	}

	/**
	 * Resumes the presentation thread if it is paused.
	 */
	@Override
	public void resume() throws Exception {
		LOGGER.info("Resuming presentation");
		super.resume();
		mPresentationMethod.resume();
	}

	/**
	 * Stops the presentation thread if it is executing.
	 */
	@Override
	public void stop() throws Exception {
		LOGGER.info("Stopping presentation");
		super.stop();
	}

	/**
	 * Returns the task to run inside of the thread. Used by the super class.
	 */
	@Override
	protected Runnable getRunnableAction() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					final float sunPositionChangeBetweenDisplay = Math
							.abs(Utils.convertTimeToDegrees(mDisplayRate));
					float degreesPassed = 0;
					float previousSunPosition = 0;
					while (!mRunningThread.isInterrupted()) {
						checkPaused();
						SimulationResult result = mQueue.take();

						// Check if enough degrees have passed for our display
						// threshold
						degreesPassed += result.getSunPosition() == previousSunPosition ? 360 : Math.abs(result.getSunPosition() - previousSunPosition);
						if (degreesPassed >= sunPositionChangeBetweenDisplay) {
							degreesPassed = degreesPassed
									- sunPositionChangeBetweenDisplay;
							present(result);
						} else {
							LOGGER.info("SimulationResult skipped");
						}
						previousSunPosition = result.getSunPosition();
						LOGGER.info("Buffer size: " + mQueue.size());
					}
				} catch (InterruptedException e) {
					LOGGER.info("Presentation stopped");
				} finally {
					mRunningThread = null;
					if (mOnStop != null) {
						mOnStop.onStop();
					}
				}
			}

		};
	}

}
