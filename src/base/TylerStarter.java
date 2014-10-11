package base;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TylerStarter {
	
	static final int MAX_BUFFER_SIZE = 10;
	static final PresentationMethod PRESENTATION_METHOD = new MockPresentationMethod();
	static final SimulationMethod SIMULATION_METHOD = new MockSimulationMethod();
	static final boolean ASYNC_SIMULATION = true;
	static final boolean ASYNC_PRESENTATION = true;
	
	public static void main(String args[]) {
		
		PausableStoppable process = masterController();
//		PausableStoppable process = presentationInitiative();
//		PausableStoppable process = simulationInitiative();

		try {
			Thread.sleep(5000);
			
			process.pause();
			
			Thread.sleep(5000);
			
			process.resume();
			
			Thread.sleep(5000);
			
			process.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static PausableStoppable masterController() {
		MasterControllerInitiative controller = new MasterControllerInitiative(MAX_BUFFER_SIZE, ASYNC_SIMULATION, ASYNC_PRESENTATION, SIMULATION_METHOD, PRESENTATION_METHOD);
		try {
			controller.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return controller;
	}
	
	private static PausableStoppable presentationInitiative() {
		
		BlockingQueue<SimulationResult> queue = new ArrayBlockingQueue<>(MAX_BUFFER_SIZE);
		PresentationInitiative presentation = new PresentationInitiative(queue, PRESENTATION_METHOD);
		final SimulationInitiative simulation = new SimulationInitiative(queue, SIMULATION_METHOD);
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
			Utils.startSimulationProcess(ASYNC_PRESENTATION, ASYNC_SIMULATION, queue, presentation, simulation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return presentation; 
		
	}
	
	private static PausableStoppable simulationInitiative() {

		BlockingQueue<SimulationResult> queue = new ArrayBlockingQueue<>(MAX_BUFFER_SIZE);
		final PresentationInitiative presentation = new PresentationInitiative(queue, PRESENTATION_METHOD);
		SimulationInitiative simulation = new SimulationInitiative(queue, SIMULATION_METHOD);
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
			Utils.startSimulationProcess(ASYNC_PRESENTATION, ASYNC_SIMULATION, queue, presentation, simulation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return simulation;
		
	}

}
