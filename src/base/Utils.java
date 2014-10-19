package base;

import initiatives.PresentationInitiative;
import initiatives.SimulationInitiative;

import java.util.concurrent.BlockingQueue;

import callbacks.OnStart;

/**
 * Utils provides various helper methods. 
 * @author Tyler Benfield
 *
 */
public abstract class Utils {
	
	/**
	 * Helper function for beginning the simulation process.
	 * Main responsibility is to determine how to begin based on the specified parameters.
	 * @param asyncPresentation Whether the presentation should be threaded.
	 * @param asyncSimulation Whether the simulation should be threaded.
	 * @param queue The queue to use as a shared data resource between the simulation and presentation.
	 * @param presentation The presentation method to use.
	 * @param simulation The simulation method to use.
	 * @return An OnStart callback that can be used to start the simulation/presentation process.
	 * @throws Exception
	 */
	public static OnStart startSimulationProcess(boolean asyncPresentation, boolean asyncSimulation, final BlockingQueue<SimulationResult> queue, final PresentationInitiative presentation, final SimulationInitiative simulation) throws Exception {
		if (asyncSimulation && asyncPresentation) {
			return new OnStart() {
				
				@Override
				public void onStart() throws Exception {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					simulation.start();
					presentation.start();
					presentation.enableOnStartListener();
					simulation.enableOnStartListener();
				}
				
			};
		} else if (asyncSimulation) {
			return new OnStart() {
				
				@Override
				public void onStart() throws Exception {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					simulation.start();
					runPresentationSynchronous(queue, presentation);
					presentation.enableOnStartListener();
					simulation.enableOnStartListener();
				}
				
			};
		} else if (asyncPresentation) {
			return new OnStart() {
				
				@Override
				public void onStart() throws Exception {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					presentation.start();
					runSimulationSynchronous(queue, simulation);
					presentation.enableOnStartListener();
					simulation.enableOnStartListener();
				}
				
			};
		} else {
			return new OnStart() {
				
				@Override
				public void onStart() throws InterruptedException {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					runPresentationAndSimulationSynchronous(presentation, simulation);
					presentation.enableOnStartListener();
					simulation.enableOnStartListener();
				}
				
			};
		}
	}
	
	/**
	 * Begins executing the presentation on the current thread.
	 * @param queue The queue to use as a shared data resource between the simulation and presentation.
	 * @param presentation The presentation method to use.
	 * @throws InterruptedException
	 */
	private static void runPresentationSynchronous(BlockingQueue<SimulationResult> queue, PresentationInitiative presentation) throws InterruptedException {
		while(true) {
			presentation.present(queue.take());
		}
	}
	
	/**
	 * Begins executing the simulation on the current thread.
	 * @param queue The queue to use as a shared data resource between the simulation and presentation.
	 * @param simulation The simulation method to use.
	 * @throws InterruptedException
	 */
	private static void runSimulationSynchronous(BlockingQueue<SimulationResult> queue, SimulationInitiative simulation) throws InterruptedException {
		SimulationResult previousResult = null;
		while(true) {
			previousResult = simulation.simulate(previousResult, 15, 15);
			queue.put(previousResult);
		}
	}
	
	/**
	 * Begins alternating execution of the simulation and presentation functions on the current thread.
	 * @param presentation The presentation method to use.
	 * @param simulation The simulation method to use.
	 * @throws InterruptedException
	 */
	private static void runPresentationAndSimulationSynchronous(PresentationInitiative presentation, SimulationInitiative simulation) throws InterruptedException {
		SimulationResult previousResult = null;
		while(true) {
			previousResult = simulation.simulate(previousResult, 15, 15);
			presentation.present(previousResult);
		}
	}
	
}
