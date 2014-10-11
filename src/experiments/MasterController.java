/**
 * 
 */
package experiments;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author 
 *
 */
public class MasterController extends PausableStoppable {
	
	private BlockingQueue<SimulationResult> mSharedQueue;
	private Simulation mSim;
	private Presentation mPres;
	private boolean mAsyncPresentation;
	private boolean mAsyncSimulation;
	
	public MasterController(int bufferSize, boolean asyncSimulation, boolean asyncPresentation, SimulationMethod simulationMethod, PresentationMethod presentationMethod) {
		mSharedQueue = new ArrayBlockingQueue<>(bufferSize);
		mSim = new Simulation(mSharedQueue, simulationMethod);
		mPres = new Presentation(mSharedQueue, presentationMethod);
		mAsyncPresentation = asyncPresentation;
		mAsyncSimulation = asyncSimulation;
	}

	@Override
	public void start() throws Exception {
		//TODO: Pause, resume, and stop need to be implemented here as well
		Utils.startSimulationProcess(mAsyncPresentation, mAsyncSimulation, mSharedQueue, mPres, mSim);
	}

	@Override
	public void pause() {
		if (mAsyncSimulation) {
			try {
				mSim.pause();
			} catch (Exception e) {
				
			}
		}
		
		if (mAsyncPresentation) {
			try {
				mPres.pause();
			} catch (Exception e) {
				
			}
		}
	}

	@Override
	public void resume() {
		if (mAsyncSimulation) {
			try {
				mSim.resume();
			} catch (Exception e) {
				
			}
		}
		
		if (mAsyncPresentation) {
			try {
				mPres.resume();
			} catch (Exception e) {
				
			}
		}
	}
	
	@Override
	public void stop() {
		if (mAsyncSimulation) {
			try {
				mSim.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (mAsyncPresentation) {
			try {
				mPres.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected Runnable getRunnableAction() {
		return null;
	}
	
}
