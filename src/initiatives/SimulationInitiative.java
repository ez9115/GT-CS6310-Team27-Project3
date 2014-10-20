package initiatives;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import callbacks.OnStart;
import callbacks.OnStop;
import base.ObjectFactory;
import base.PausableStoppable;
import base.SimulationMethod;
import base.SimulationResult;
import base.Utils;

/**
 * SimulationInitiative serves as both the initiative when the simulation has initiative or the simulation controller
 * when another module has initiative.
 * @author Tyler Benfield
 *
 */
public class SimulationInitiative extends PausableStoppable {
	
	private final static Logger LOGGER = Logger.getLogger(SimulationInitiative.class.getName());
	
	private final static int STABILIZATION_DELTA = 1;

	/**
	 * OnStart callback to execute IN PLACE OF the standard start call.
	 */
	private OnStart mOnStart;
	
	/**
	 * Whether the OnStart callback is enabled or disabled.
	 * Useful when the OnStart callback contains a call to the start() method and you do not want an infinite loop.
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
	 * The simulation method implementation to execute on each simulation attempt.
	 */
	private SimulationMethod mSimulationMethod;
	
	/**
	 * Creates a new SimulationInitiative with the specified shared data queue and simulation method implementation. 
	 * @param queue The shared data queue. Result data will be added to this queue.
	 * @param simulationMethod The simulation method implementation to execute when the simulation runs.
	 */
	public SimulationInitiative(BlockingQueue<SimulationResult> queue, SimulationMethod simulationMethod) {
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
	 * Sets the OnStart listener.
	 * Note: This replaces the typical start() behavior.
	 * @param onStart
	 */
	public void setOnStartListener(OnStart onStart) {
		mOnStart = onStart;
	}
	
	/**
	 * Sets the OnStop listener.
	 * @param onStop
	 */
	public void setOnStopListener(OnStop onStop) {
		mOnStop = onStop;
	}
	
	/**
	 * Executes a simulation and returns the result.
	 * @return The resulting simulation data. 
	 * @throws InterruptedException Thrown if the current thread is interrupted while waiting for the queue to be available.
	 */
	public SimulationResult simulate(SimulationResult previousResults, int degreeSeparation, float sunPosition) throws InterruptedException {
		return mSimulationMethod.simulate(previousResults, degreeSeparation, sunPosition);
	}

	/**
	 * Begins the simulation process on a thread.
	 */
	@Override
	public void start(int degreeSeparation, int timeStep) throws Exception {
		LOGGER.info("Starting simulation");
		if (mOnStart != null && mOnStartEnabled) {
			mDegreeSeparation = degreeSeparation;
			mTimeStep = timeStep;
			mOnStart.onStart(degreeSeparation, timeStep);
		} else {
			super.start(degreeSeparation, timeStep);
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
					float sunPosition = 0;
					SimulationResult.MinMaxTemp minMaxTemp = null;
					int numberOfIterationsToStabilization = 0;
					boolean stabilizationAchieved = false;
					SimulationResult previousResult = ObjectFactory.getInitialGrid(mDegreeSeparation);
					while(!mRunningThread.isInterrupted()) {
						checkPaused();
						SimulationResult newResult = simulate(previousResult, mDegreeSeparation, sunPosition);
						
						// Determine if we have stabilized
						if (!stabilizationAchieved) {
							numberOfIterationsToStabilization++;
							SimulationResult.MinMaxTemp newMinMaxTemp = newResult.getMinMaxTemperature();
							if (minMaxTemp != null && !stabilizationAchieved) {
								if (Math.abs(newMinMaxTemp.Max - minMaxTemp.Max) <= STABILIZATION_DELTA && Math.abs(newMinMaxTemp.Min - minMaxTemp.Min) <= STABILIZATION_DELTA) {
									System.out.println("Stabilization achieved");
									stabilizationAchieved = true;
								}
							}
							minMaxTemp = newMinMaxTemp;
						}

						if (stabilizationAchieved) {
							System.out.println("Stabilization achieved at " + numberOfIterationsToStabilization + " iterations");
						}

						mQueue.put(newResult);
						previousResult = newResult;
						sunPosition = Utils.incrementSunPosition(sunPosition, mTimeStep);
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
	
}
