package base;

import java.util.concurrent.BlockingQueue;

public abstract class Utils {
	
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
