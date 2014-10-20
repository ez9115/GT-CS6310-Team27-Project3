package base;

import initiatives.MasterControllerInitiative;
import initiatives.PresentationInitiative;
import initiatives.SimulationInitiative;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import simulation.GridData;
import simulation.SimulationMethodImpl;
import callbacks.OnStart;
import callbacks.OnStop;

/**
 * ObjectFactory provides methods of creating implementations of interface objects.
 * 
 * This helps decouple the various pieces of the architecture by having a single location
 * responsible for managing implementations.
 * @author Tyler Benfield
 *
 */
public abstract class ObjectFactory {
	
	/**
	 * Generates a 
	 * @param degreeSeparation
	 * @return
	 */
	public static SimulationResult getInitialGrid(int degreeSeparation) {
		int Cols = 360 / degreeSeparation;
		int Rows = 360 / degreeSeparation;
		
		GridData[][] data = new GridData[Cols][];
		for (int i = 0; i < data.length; i++) {
			data[i] = new GridData[Rows];
			for (int j = 0; j < data[i].length; j++) {
				GridData d = new GridData();
				d.setTemp(288);
				data[i][j] = d;
			}
		}
		
		return new SimulationResult(data, 0);
	}

	public static SimulationMethod getSimulationMethod() {
		//return new MockSimulationMethod();
		return new SimulationMethodImpl();
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
		
		BlockingQueue<SimulationResult> queue = new ArrayBlockingQueue<SimulationResult>(bufferSize);
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

		BlockingQueue<SimulationResult> queue = new ArrayBlockingQueue<SimulationResult>(bufferSize);
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
