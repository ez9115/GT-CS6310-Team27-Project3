package EarthSim;

import initiatives.SimulationInitiative;

import java.awt.EventQueue;
import java.util.logging.Logger;

import base.InitiativeType;
import base.ObjectFactory;
import base.PausableStoppable;
import GUI.GUIApp;

public class Demo {

	private final static Logger LOGGER = Logger.getLogger(SimulationInitiative.class.getName());

	private static int buffersize = 1;
	private static boolean presentationThread = true;
	private static boolean simulationThread = true;
	private static boolean presentationInitiative = false;
	private static boolean simulationInitiative = true;
	private static boolean masterInitiative = false;

	/**
	 * Application entry point
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {

		parseArguments(args);

		final GUIApp presentation = new GUIApp();

		PausableStoppable initiative;
		try {
			initiative = ObjectFactory.getInitiative(getInitiativeType(), buffersize, presentationThread, simulationThread, presentation);

			presentation.setInitiative(initiative);

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						presentation.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private static InitiativeType getInitiativeType() {
		if (presentationInitiative) {
			return InitiativeType.Presentation;
		}
		if (simulationInitiative) {
			return InitiativeType.Simulation;
		}
		return InitiativeType.MasterController;
	}

	private static void parseArguments(String[] args) {

		for (int loop = 0; loop < args.length; loop = loop + 1) {
			switch (args[loop]) {
			// buffersize
			case "-b":
				buffersize = Integer.parseInt(args[loop + 1]);
				loop = loop + 1;
				break;
				// Presentation runs in a thread
			case "-p":
				presentationThread = true;
				break;
				// Simulation runs in a thread
			case "-s":
				simulationThread = true;
				break;
				// Simulation has initiative
			case "-t":
				simulationInitiative = true;
				masterInitiative = false;
				break;
				// Presentation has initiative
			case "-r":
				presentationInitiative = true;
				masterInitiative = false;
				break;
				// unknown flag
			default:
				System.out.println("Invalid parameter specified");
			}
		}

		LOGGER.info("Arguments; Buffer size: " + buffersize);
		LOGGER.info("Arguments; Presentation thread: " + presentationThread);
		LOGGER.info("Arguments; Simulation thred: " + simulationThread);
		LOGGER.info("Arguments; Simulation initiative: " + simulationInitiative);
		LOGGER.info("Arguments; Presentation initiative: " + presentationInitiative);
		LOGGER.info("Arguments; Master initiative: " + masterInitiative);

	}

}
