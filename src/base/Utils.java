package base;

import java.util.concurrent.BlockingQueue;

public abstract class Utils {
	
	public static void startSimulationProcess(boolean asyncPresentation, boolean asyncSimulation, BlockingQueue<SimulationResult> queue, PresentationInitiative presentation, SimulationInitiative simulation) throws Exception {
		if (asyncSimulation && asyncPresentation) {
			simulation.start();
			presentation.start();
		} else if (asyncSimulation) {
			simulation.start();
			runPresentationSynchronous(queue, presentation);
		} else if (asyncPresentation) {
			presentation.start();
			runSimulationSynchronous(queue, simulation);
		} else {
			runPresentationAndSimulationSynchronous(presentation, simulation);
		}
	}
	
	private static void runPresentationSynchronous(BlockingQueue<SimulationResult> queue, PresentationInitiative presentation) throws InterruptedException {
		while(true) {
			presentation.present(queue.take());
		}
	}
	
	private static void runSimulationSynchronous(BlockingQueue<SimulationResult> queue, SimulationInitiative simulation) throws InterruptedException {
		while(true) {
			queue.put(simulation.simulate());
		}
	}
	
	private static void runPresentationAndSimulationSynchronous(PresentationInitiative presentation, SimulationInitiative simulation) throws InterruptedException {
		while(true) {
			SimulationResult result = simulation.simulate();
			presentation.present(result);
		}
	}
	
}
