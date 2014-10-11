package EarthSim;

import java.awt.EventQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import base.MasterControllerInitiative;
import base.MockSimulationMethod;
import base.OnStop;
import base.PausableStoppable;
import base.PresentationInitiative;
import base.PresentationMethod;
import base.SimulationInitiative;
import base.SimulationMethod;
import base.SimulationResult;
import base.Utils;
import GUI.GUIApp;

public class Demo {
	
	static final int MAX_BUFFER_SIZE = 10;
	static final SimulationMethod SIMULATION_METHOD = new MockSimulationMethod();
	static final boolean ASYNC_SIMULATION = true;
	static final boolean ASYNC_PRESENTATION = true;
	
	public static void main(String[] args) {
		
		final GUIApp presentation = new GUIApp();
		
		PausableStoppable process = masterController(presentation);
//		PausableStoppable process = presentationInitiative();
//		PausableStoppable process = simulationInitiative();
		
		presentation.setInitiative(process);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					presentation.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	private static PausableStoppable masterController(PresentationMethod presentation) {
		MasterControllerInitiative controller = new MasterControllerInitiative(MAX_BUFFER_SIZE, ASYNC_SIMULATION, ASYNC_PRESENTATION, SIMULATION_METHOD, presentation);

		return controller;
	}
	
//	private static PausableStoppable presentationInitiative() {
//		
//		BlockingQueue<SimulationResult> queue = new ArrayBlockingQueue<>(MAX_BUFFER_SIZE);
//		PresentationInitiative presentation = new PresentationInitiative(queue, PRESENTATION_METHOD);
//		final SimulationInitiative simulation = new SimulationInitiative(queue, SIMULATION_METHOD);
//		presentation.setOnStopListener(new OnStop() {
//			
//			@Override
//			public void onStop() {
//				try {
//					simulation.stop();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			
//		});
//		
//		try {
//			Utils.startSimulationProcess(ASYNC_PRESENTATION, ASYNC_SIMULATION, queue, presentation, simulation);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return presentation; 
//		
//	}
	
//	private static PausableStoppable simulationInitiative() {
//
//		BlockingQueue<SimulationResult> queue = new ArrayBlockingQueue<>(MAX_BUFFER_SIZE);
//		final PresentationInitiative presentation = new PresentationInitiative(queue, PRESENTATION_METHOD);
//		SimulationInitiative simulation = new SimulationInitiative(queue, SIMULATION_METHOD);
//		simulation.setOnStopListener(new OnStop() {
//			
//			@Override
//			public void onStop() {
//				try {
//					presentation.stop();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			
//		});
//
//		try {
//			Utils.startSimulationProcess(ASYNC_PRESENTATION, ASYNC_SIMULATION, queue, presentation, simulation);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return simulation;
//		
//	}

}
