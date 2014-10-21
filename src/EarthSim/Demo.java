package EarthSim;

import initiatives.SimulationInitiative;

import java.awt.EventQueue;
import java.util.logging.Logger;

import GUI.GUIApp;
import base.InitiativeType;

public class Demo {

	private final static Logger LOGGER = Logger
			.getLogger(SimulationInitiative.class.getName());

	private static int buffersize = 1;
	private static boolean presentationThread = true;
	private static boolean simulationThread = true;
	private static boolean presentationInitiative = false;
	private static boolean simulationInitiative = false;
	private static boolean masterInitiative = true;

	/**
	 * Application entry point
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {

		parseArguments(args);

		final GUIApp presentation = new GUIApp(buffersize, presentationThread,
				simulationThread, getInitiativeType());
		try {
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
			if (args[loop] == "-b") {
				// buffersize
				buffersize = Integer.parseInt(args[loop + 1]);
				loop = loop + 1;
			} else if (args[loop] == "-p") {
				// Presentation runs in a thread
				presentationThread = true;
			} else if (args[loop] == "-s") {
				// Simulation runs in a thread
				simulationThread = true;
			} else if (args[loop] == "-t") {
				// Simulation has initiative
				simulationInitiative = true;
				masterInitiative = false;
			} else if (args[loop] == "-r") {
				// Presentation has initiative
				presentationInitiative = true;
				masterInitiative = false;
			} else {
				// unknown flag
				System.out.println("Invalid parameter specified");
			}
		}

		LOGGER.info("Arguments; Buffer size: " + buffersize);
		LOGGER.info("Arguments; Presentation thread: " + presentationThread);
		LOGGER.info("Arguments; Simulation thread: " + simulationThread);
		LOGGER.info("Arguments; Simulation initiative: " + simulationInitiative);
		LOGGER.info("Arguments; Presentation initiative: "
				+ presentationInitiative);
		LOGGER.info("Arguments; Master initiative: " + masterInitiative);

	}

}
