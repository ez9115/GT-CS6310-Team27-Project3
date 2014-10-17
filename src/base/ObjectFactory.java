package base;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class ObjectFactory {

	public static SimulationMethod getSimulationMethod() {
		return new MockSimulationMethod();
	}
	
	public static PausableStoppable getInitiative(InitiativeType initiativeType, int bufferSize, boolean asyncSimulation, boolean asyncPresentation, PresentationMethod presentationMethod) throws Exception {
		switch (initiativeType) {
		case MasterController:
			return masterController(presentationMethod, bufferSize, asyncSimulation, asyncPresentation);
		case Presentation:
			return presentationInitiative(presentationMethod, bufferSize, asyncSimulation, asyncPresentation);
		case Simulation:
			return simulationInitiative(presentationMethod, bufferSize, asyncSimulation, asyncPresentation);
		default:
			throw new Exception("Initiative type not supported");
		}
	}
	
	private static PausableStoppable masterController(PresentationMethod presentation, int bufferSize, boolean asyncSimulation, boolean asyncPresentation) {
		return new MasterControllerInitiative(bufferSize, asyncSimulation, asyncPresentation, getSimulationMethod(), presentation);
	}
	
	private static PausableStoppable presentationInitiative(PresentationMethod presentationMethod, int bufferSize, boolean asyncSimulation, boolean asyncPresentation) {
		
		BlockingQueue<SimulationResult> queue = new ArrayBlockingQueue<>(bufferSize);
		PresentationInitiative presentation = new PresentationInitiative(queue, presentationMethod);
		final SimulationInitiative simulation = new SimulationInitiative(queue, getSimulationMethod());
		presentation.setOnStopListener(new OnStop() {
			
			@Override
			public void onStop() {
				try {
					simulation.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		try {
			OnStart starter = Utils.startSimulationProcess(asyncPresentation, asyncSimulation, queue, presentation, simulation);
			presentation.setOnStartListener(starter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return presentation;
		
	}
	
	private static PausableStoppable simulationInitiative(PresentationMethod presentationMethod, int bufferSize, boolean asyncSimulation, boolean asyncPresentation) {

		BlockingQueue<SimulationResult> queue = new ArrayBlockingQueue<>(bufferSize);
		final PresentationInitiative presentation = new PresentationInitiative(queue, presentationMethod);
		SimulationInitiative simulation = new SimulationInitiative(queue, getSimulationMethod());
		simulation.setOnStopListener(new OnStop() {
			
			@Override
			public void onStop() {
				try {
					presentation.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});

		try {
			OnStart starter = Utils.startSimulationProcess(asyncPresentation, asyncSimulation, queue, presentation, simulation);
			simulation.setOnStartListener(starter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return simulation;
		
	}
	
}
