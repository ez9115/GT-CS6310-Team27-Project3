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
				public void onStart(int degreeSeparation, int timeStep) throws Exception {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					simulation.start(degreeSeparation, timeStep);
					presentation.start(degreeSeparation, timeStep);
					presentation.enableOnStartListener();
					simulation.enableOnStartListener();
				}
				
			};
		} else if (asyncSimulation) {
			return new OnStart() {
				
				@Override
				public void onStart(int degreeSeparation, int timeStep) throws Exception {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					simulation.start(degreeSeparation, timeStep);
					runPresentationSynchronous(queue, presentation);
					presentation.enableOnStartListener();
					simulation.enableOnStartListener();
				}
				
			};
		} else if (asyncPresentation) {
			return new OnStart() {
				
				@Override
				public void onStart(int degreeSeparation, int timeStep) throws Exception {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					presentation.start(degreeSeparation, timeStep);
					runSimulationSynchronous(queue, simulation, degreeSeparation, timeStep);
					presentation.enableOnStartListener();
					simulation.enableOnStartListener();
				}
				
			};
		} else {
			return new OnStart() {
				
				@Override
				public void onStart(int degreeSeparation, int timeStep) throws InterruptedException {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					runPresentationAndSimulationSynchronous(presentation, simulation, degreeSeparation, timeStep);
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
	private static void runSimulationSynchronous(BlockingQueue<SimulationResult> queue, SimulationInitiative simulation, int degreeSeparation, int timeStep) throws InterruptedException {
		SimulationResult previousResult = ObjectFactory.getInitialGrid(degreeSeparation);
		float sunPosition = 0;
		while(true) {
			previousResult = simulation.simulate(previousResult, degreeSeparation, sunPosition);
			queue.put(previousResult);
			sunPosition = incrementSunPosition(sunPosition, timeStep);
		}
	}
	
	/**
	 * Begins alternating execution of the simulation and presentation functions on the current thread.
	 * @param presentation The presentation method to use.
	 * @param simulation The simulation method to use.
	 * @throws InterruptedException
	 */
	private static void runPresentationAndSimulationSynchronous(PresentationInitiative presentation, SimulationInitiative simulation, int degreeSeparation, int timeStep) throws InterruptedException {
		SimulationResult previousResult = ObjectFactory.getInitialGrid(degreeSeparation);
		float sunPosition = 0;
		while(true) {
			previousResult = simulation.simulate(previousResult, degreeSeparation, sunPosition);
			presentation.present(previousResult);
			sunPosition = incrementSunPosition(sunPosition, timeStep);
		}
	}
	
	/**
	 * Converts a time step into change in degrees.
	 * @param timeStep
	 * @return
	 */
	public static float convertTimeToDegrees(int timeStep) {
		return (float) (-360.0 * timeStep / 1440.0);
	}
	
	/**
	 * Increments the sun position, rolling over at 180 degrees.
	 * @param sunPosition
	 * @param increment
	 * @return
	 */
	public static float incrementSunPosition(float sunPosition, int timeStep) {
		float increment = convertTimeToDegrees(timeStep);
		sunPosition += increment;
		if (sunPosition > 180) {
			sunPosition -= 360;
		}
		else if (sunPosition < -180) {
			sunPosition += 360;
		}
		return sunPosition;
	}
	
}
