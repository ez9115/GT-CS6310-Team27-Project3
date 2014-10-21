package base;

import initiatives.PresentationInitiative;
import initiatives.SimulationInitiative;
import initiatives.SimulationInitiative.StabilizationData;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import callbacks.OnStart;

/**
 * Utils provides various helper methods.
 *
 * @author Tyler Benfield
 *
 */
public abstract class Utils {

	private final static Logger LOGGER = Logger
			.getLogger(SimulationInitiative.class.getName());

	/**
	 * The difference threshold allowed for stabilization.
	 */
	private final static double STABILIZATION_DELTA = 0.1;

	/**
	 * Helper function for beginning the simulation process. Main responsibility
	 * is to determine how to begin based on the specified parameters.
	 *
	 * @param asyncPresentation
	 *            Whether the presentation should be threaded.
	 * @param asyncSimulation
	 *            Whether the simulation should be threaded.
	 * @param queue
	 *            The queue to use as a shared data resource between the
	 *            simulation and presentation.
	 * @param presentation
	 *            The presentation method to use.
	 * @param simulation
	 *            The simulation method to use.
	 * @return An OnStart callback that can be used to start the
	 *         simulation/presentation process.
	 * @throws Exception
	 */
	public static OnStart startSimulationProcess(boolean asyncPresentation,
			boolean asyncSimulation,
			final BlockingQueue<SimulationResult> queue,
			final PresentationInitiative presentation,
			final SimulationInitiative simulation) throws Exception {
		if (asyncSimulation && asyncPresentation) {
			return new OnStart() {

				@Override
				public void onStart(int degreeSeparation, int timeStep,
						int displayRate) throws Exception {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					simulation.start(degreeSeparation, timeStep, displayRate);
					presentation.start(degreeSeparation, timeStep, displayRate);
					presentation.enableOnStartListener();
					simulation.enableOnStartListener();
				}

			};
		} else if (asyncSimulation) {
			return new OnStart() {

				@Override
				public void onStart(int degreeSeparation, int timeStep,
						int displayRate) throws Exception {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					simulation.start(degreeSeparation, timeStep, displayRate);
					runPresentationSynchronous(queue, presentation, displayRate);
					presentation.enableOnStartListener();
					simulation.enableOnStartListener();
				}

			};
		} else if (asyncPresentation) {
			return new OnStart() {

				@Override
				public void onStart(int degreeSeparation, int timeStep,
						int displayRate) throws Exception {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					presentation.start(degreeSeparation, timeStep, displayRate);
					runSimulationSynchronous(queue, simulation,
							degreeSeparation, timeStep);
					presentation.enableOnStartListener();
					simulation.enableOnStartListener();
				}

			};
		} else {
			return new OnStart() {

				@Override
				public void onStart(int degreeSeparation, int timeStep,
						int displayRate) throws InterruptedException {
					presentation.disableOnStartListener();
					simulation.disableOnStartListener();
					runPresentationAndSimulationSynchronous(presentation,
							simulation, degreeSeparation, timeStep, displayRate);
					presentation.enableOnStartListener();
					simulation.enableOnStartListener();
				}

			};
		}
	}

	/**
	 * Begins executing the presentation on the current thread.
	 *
	 * @param queue
	 *            The queue to use as a shared data resource between the
	 *            simulation and presentation.
	 * @param presentation
	 *            The presentation method to use.
	 * @throws InterruptedException
	 */
	private static void runPresentationSynchronous(
			BlockingQueue<SimulationResult> queue,
			PresentationInitiative presentation, int displayRate)
			throws InterruptedException {
		// TODO: Would be nice to do some sort of timer check here to not have
		// an infinite loop, but the single-threaded context makes it difficult
		final float sunPositionChangeBetweenDisplay = Utils
				.convertTimeToDegrees(displayRate);
		float degreesPassed = 0;
		float previousSunPosition = 0;
		while (true) {
			SimulationResult result = queue.take();

			// Check if enough degrees have passed for our display threshold
			degreesPassed += Math.abs(result.getSunPosition()
					- previousSunPosition);
			if (degreesPassed >= sunPositionChangeBetweenDisplay) {
				degreesPassed = degreesPassed - sunPositionChangeBetweenDisplay;
				previousSunPosition = result.getSunPosition();
				presentation.present(result);
			} else {
				LOGGER.info("SimulationResult skipped");
			}

		}
	}

	/**
	 * Begins executing the simulation on the current thread.
	 *
	 * @param queue
	 *            The queue to use as a shared data resource between the
	 *            simulation and presentation.
	 * @param simulation
	 *            The simulation method to use.
	 * @throws InterruptedException
	 */
	private static void runSimulationSynchronous(
			BlockingQueue<SimulationResult> queue,
			SimulationInitiative simulation, int degreeSeparation, int timeStep)
			throws InterruptedException {
		float sunPosition = 0;
		StabilizationData stabilizationData = new StabilizationData();

		SimulationResult previousResult = ObjectFactory.getInitialGrid(degreeSeparation);

		// TODO: Would be nice to do some sort of timer check here to not have
		// an infinite loop, but the single-threaded context makes it difficult
		while (true) {
			previousResult = simulation.simulate(previousResult, degreeSeparation, sunPosition);
			
			stabilizationData.checkStabilization(previousResult);

			if (stabilizationData.stabilizationAchieved) {
				System.out.println(stabilizationData.toString());
			}
			
			queue.put(previousResult);
			sunPosition = incrementSunPosition(sunPosition, timeStep);
		}
	}

	/**
	 * Begins alternating execution of the simulation and presentation functions
	 * on the current thread.
	 *
	 * @param presentation
	 *            The presentation method to use.
	 * @param simulation
	 *            The simulation method to use.
	 * @throws InterruptedException
	 */
	private static void runPresentationAndSimulationSynchronous(
			PresentationInitiative presentation,
			SimulationInitiative simulation, int degreeSeparation,
			int timeStep, int displayRate) throws InterruptedException {
		SimulationResult previousResult = ObjectFactory
				.getInitialGrid(degreeSeparation);
		final float sunPositionChangeBetweenDisplay = Utils
				.convertTimeToDegrees(displayRate);
		float degreesPassed = 0;
		float previousSunPosition = 0;
		float sunPosition = 0;

		StabilizationData stabilizationData = new StabilizationData();

		// TODO: Would be nice to do some sort of timer check here to not have
		// an infinite loop, but the single-threaded context makes it difficult
		while (true) {
			previousResult = simulation.simulate(previousResult, degreeSeparation, sunPosition);

			// Check if enough degrees have passed for our display threshold
			degreesPassed += Math.abs(previousResult.getSunPosition() - previousSunPosition);
			if (degreesPassed >= sunPositionChangeBetweenDisplay) {
				degreesPassed = degreesPassed - sunPositionChangeBetweenDisplay;
				previousSunPosition = previousResult.getSunPosition();

				// Determine if we have stabilized
				stabilizationData.checkStabilization(previousResult);

				if (stabilizationData.stabilizationAchieved) {
					System.out.println(stabilizationData.toString());
				}
				
				presentation.present(previousResult);
			} else {
				LOGGER.info("SimulationResult skipped");
			}
			sunPosition = incrementSunPosition(sunPosition, timeStep);
		}
	}

	/**
	 * Converts a time step into change in degrees.
	 *
	 * @param timeStep
	 *            Time step in minutes
	 * @return Degrees the earth would rotate in the amount of time from -180 to
	 *         180
	 */
	public static float convertTimeToDegrees(int timeStep) {
		return (float) (-360.0 * timeStep / 1440.0);
	}

	/**
	 * Increments the sun position, rolling over at 180 degrees.
	 *
	 * @param sunPosition
	 * @param increment
	 * @return
	 */
	public static float incrementSunPosition(float sunPosition, int timeStep) {
		float increment = convertTimeToDegrees(timeStep);
		sunPosition += increment;
		if (sunPosition > 180) {
			sunPosition -= 360;
		} else if (sunPosition < -180) {
			sunPosition += 360;
		}
		return sunPosition;
	}

	/**
	 * Converts a degree of rotation to a time step in seconds.
	 *
	 * @param degrees
	 *            The degree of rotation
	 * @return The amount of time in seconds that passes when the earth rotates
	 *         the specified number of degrees
	 */
	public static double convertDegreesToTime(float degrees) {
		float portionOfDayElapsed = Math.abs(degrees) / 360;
		return portionOfDayElapsed * 86400.0;
	}
	
	/**
	 * Determines if a result has stabilized based on the previous results.
	 * @param minMaxTemp The previous results
	 * @param newMinMaxTemp The current results
	 * @return Whether the results have stabilized
	 */
	public static boolean hasStabilized(SimulationResult.MinMaxTemp minMaxTemp, SimulationResult.MinMaxTemp newMinMaxTemp) {
		return Math.abs(newMinMaxTemp.Max - minMaxTemp.Max) <= STABILIZATION_DELTA && Math.abs(newMinMaxTemp.Min - minMaxTemp.Min) <= STABILIZATION_DELTA;
	}

	/**
	 * Converts a number of seconds elapsed to a formatted time string.
	 * @param secondsElapsed
	 * @return
	 */
	public static String convertSecondsToTimeString(float secondsElapsed) {
		int yearsElapsed = (int) Math.floor(secondsElapsed / 31536000.0);
		double remainingSeconds = secondsElapsed % 31536000.0;

		int daysElapsed = (int) Math.floor(remainingSeconds / 86400.0);
		remainingSeconds = (int) (remainingSeconds % 86400.0);

		int hoursElapsed = (int) Math.floor(remainingSeconds / 3600.0);
		remainingSeconds = (int) (remainingSeconds % 3600.0);

		int minutesElapsed = (int) Math.floor(remainingSeconds / 60.0);

		return String.format("%d Yr, %d Day, %d Hr, %d Min", yearsElapsed, daysElapsed, hoursElapsed, minutesElapsed);
	}
	
}
