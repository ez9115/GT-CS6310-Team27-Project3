package experiments;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class Simulation extends PausableStoppable {
	
	private final static Logger LOGGER = Logger.getLogger(Simulation.class.getName()); 

	private OnStop mOnStop;
	private BlockingQueue<SimulationResult> mQueue;
	private SimulationMethod mSimulationMethod;

	public Simulation(BlockingQueue<SimulationResult> queue, SimulationMethod simulationMethod) {
		mQueue = queue;
		mSimulationMethod = simulationMethod;
		LOGGER.info("Simulation initialized");
	}
	
	public void setOnStopListener(OnStop onStop) {
		mOnStop = onStop;
	}
	
	public SimulationResult simulate() throws InterruptedException {
		return mSimulationMethod.simulate();
	}
	
	@Override
	public void start() throws Exception {
		LOGGER.info("Starting simulation");
		super.start();
	}
	
	@Override
	public void pause() throws Exception {
		LOGGER.info("Pausing simulation");
		super.pause();
		mSimulationMethod.pause();
	}
	
	@Override
	public void resume() throws Exception {
		LOGGER.info("Resuming simulation");
		super.resume();
		mSimulationMethod.resume();
	}

	@Override
	public void stop() throws Exception {
		LOGGER.info("Stopping simulation");
		super.stop();
	}
	
	@Override
	protected Runnable getRunnableAction() {
		return new Runnable() {
			
			@Override
			public void run() {
				try {
					while(!runningThread.isInterrupted()) {
						checkPaused();
						mQueue.put(simulate());
					}
				} catch (InterruptedException e) {
					LOGGER.info("Simulation stopped");
				} finally {
					runningThread = null;
					if (mOnStop != null) {
						mOnStop.onStop();
					}
				}
			}
			
		};
	}
	
}
