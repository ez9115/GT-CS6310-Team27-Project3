package base;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class PresentationInitiative extends PausableStoppable {
	
	private final static Logger LOGGER = Logger.getLogger(SimulationInitiative.class.getName()); 

	private OnStop mOnStop;
	private PresentationMethod mPresentationMethod;
	private BlockingQueue<SimulationResult> mQueue;
	
	public PresentationInitiative(BlockingQueue<SimulationResult> queue, PresentationMethod presentationMethod) {
		mPresentationMethod = presentationMethod;
		mQueue = queue;
		LOGGER.info("Presentation initialized");
	}
	
	public void present(SimulationResult simulationResult) throws InterruptedException {
		mPresentationMethod.present(simulationResult);
	}
	
	public void setOnStopListener(OnStop onStop) {
		mOnStop = onStop;
	}
	
	@Override
	public void start() throws Exception {
		LOGGER.info("Starting presentation");
		super.start();
	}
	
	@Override
	public void pause() throws Exception {
		LOGGER.info("Pausing presentation");
		super.pause();
		mPresentationMethod.pause();
	}
	
	@Override
	public void resume() throws Exception {
		LOGGER.info("Resuming presentation");
		super.resume();
		mPresentationMethod.resume();
	}
	
	@Override
	public void stop() throws Exception {
		LOGGER.info("Stopping presentation");
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
						present(mQueue.take());
						LOGGER.info("Buffer size: " + mQueue.size());
					}
				} catch (InterruptedException e) {
					LOGGER.info("Presentation stopped");
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
