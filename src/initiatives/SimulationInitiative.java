package initiatives;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import base.ObjectFactory;
import base.PausableStoppable;
import base.SimulationMethod;
import base.SimulationResult;
import base.Utils;
import callbacks.OnStart;
import callbacks.OnStop;

/**
 * SimulationInitiative serves as both the initiative when the simulation has
 * initiative or the simulation controller when another module has initiative.
 *
 * @author Tyler Benfield
 *
 */
public class SimulationInitiative extends PausableStoppable {

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
	 * The shared data queue to add simulation result data to.
	 */
	private BlockingQueue<SimulationResult> mQueue;

	/**
	 * The simulation method implementation to execute on each simulation
	 * attempt.
	 */
	private SimulationMethod mSimulationMethod;

	/**
	 * Creates a new SimulationInitiative with the specified shared data queue
	 * and simulation method implementation.
	 *
	 * @param queue
	 *            The shared data queue. Result data will be added to this
	 *            queue.
	 * @param simulationMethod
	 *            The simulation method implementation to execute when the
	 *            simulation runs.
	 */
	public SimulationInitiative(BlockingQueue<SimulationResult> queue,
			SimulationMethod simulationMethod) {
		mQueue = queue;
		mSimulationMethod = simulationMethod;
		LOGGER.info("Simulation initialized");
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
	 * Executes a simulation and returns the result.
	 *
	 * @return The resulting simulation data.
	 * @throws InterruptedException
	 *             Thrown if the current thread is interrupted while waiting for
	 *             the queue to be available.
	 */
	public SimulationResult simulate(SimulationResult previousResults,
			int degreeSeparation, float sunPosition)
			throws InterruptedException {
		return mSimulationMethod.simulate(previousResults, degreeSeparation,
				sunPosition);
	}

	/**
	 * Begins the simulation process on a thread.
	 */
	@Override
	public void start(int degreeSeparation, int timeStep, int displayRate)
			throws Exception {
		LOGGER.info("Starting simulation");
		if (mOnStart != null && mOnStartEnabled) {
			mDegreeSeparation = degreeSeparation;
			mTimeStep = timeStep;
			mOnStart.onStart(degreeSeparation, timeStep, displayRate);
		} else {
			super.start(degreeSeparation, timeStep, displayRate);
		}
	}

	/**
	 * Pauses the simulation thread if it is executing.
	 */
	@Override
	public void pause() throws Exception {
		LOGGER.info("Pausing simulation");
		super.pause();
		mSimulationMethod.pause();
	}

	/**
	 * Resumes the simulation thread if it is paused.
	 */
	@Override
	public void resume() throws Exception {
		LOGGER.info("Resuming simulation");
		super.resume();
		mSimulationMethod.resume();
	}

	/**
	 * Stops the simulation thread if it is executing.
	 */
	@Override
	public void stop() throws Exception {
		LOGGER.info("Stopping simulation");
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
					float oldSunPosition = 0;
					StabilizationData stabilizationData = new StabilizationData();
					
					SimulationResult previousResult = ObjectFactory.getInitialGrid(mDegreeSeparation);
					
					while (!mRunningThread.isInterrupted()) {
						checkPaused();

						SimulationResult newResult = simulate(previousResult, mDegreeSeparation, oldSunPosition);
						oldSunPosition = Utils.incrementSunPosition(oldSunPosition, mTimeStep);

						// Determine if we have stabilized
						stabilizationData.checkStabilization(newResult);

						if (stabilizationData.stabilizationAchieved) {
							System.out.println(stabilizationData.toString());
						}

						mQueue.put(newResult);
						previousResult = newResult;
					}
				} catch (InterruptedException e) {
					LOGGER.info("Simulation stopped");
				} finally {
					mRunningThread = null;
					if (mOnStop != null) {
						mOnStop.onStop();
					}
				}
			}

		};
	}

	/**
	 * Class to hold information on simulation stabilization.
	 * @author Tyler Benfield
	 *
	 */
	public static class StabilizationData {
		public SimulationResult.MinMaxTemp minMaxTemp = null;
		public int numberOfIterationsToStabilization = 0;
		public boolean stabilizationAchieved = false;
		public long memoryAtStabilization = -1;
		public long startTime;
		public long timeToStabilization = -1;
		
		/**
		 * Initializes a new StabilizationData object and records the startTime.
		 */
		public StabilizationData() {
			startTime = System.currentTimeMillis();
		}
		
		/**
		 * Checks if the simulation has stabilized by comparing new results to the previous.
		 * Also updates the previous results.
		 * @param newResult Newly generated results
		 * @return True if stabilization has been achieved, false otherwise
		 */
		public boolean checkStabilization(SimulationResult newResult) {
			if (!stabilizationAchieved) {
				numberOfIterationsToStabilization++;
				SimulationResult.MinMaxTemp newMinMaxTemp = newResult.getMinMaxTemperature();
				if (minMaxTemp != null) {
					if (Utils.hasStabilized(minMaxTemp, newMinMaxTemp)) {
						stabilizationAchieved();
					}
				}
				minMaxTemp = newMinMaxTemp;
			}
			return stabilizationAchieved;
		}
		
		/**
		 * Marks this simulation as stabilized and records analytics.
		 */
		public void stabilizationAchieved() {
			stabilizationAchieved = true;
			memoryAtStabilization = Runtime.getRuntime().totalMemory();
			timeToStabilization = System.currentTimeMillis() - startTime;
		}
		
		@Override
		public String toString() {
			return String.format("Stabilization achieved at (%d iterations | %d ms) memory: %d bytes", numberOfIterationsToStabilization, timeToStabilization, memoryAtStabilization);
		}
	}
}
