package gridinit;

public class Demo {

	public static void main(String[] args) {

		int buffersize = 1; 
		boolean presentationThread = false;
		boolean simulationThread = false;
		boolean presentationInitiative = false;
		boolean simulationInitiative = false;
		boolean masterInitiative = true;


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

		System.out.println("buffersize: " + buffersize + " pres thread : " + presentationThread + " sim thread: " + simulationThread);
		System.out.println("sim init " + simulationInitiative + " pres init : " + presentationInitiative + " mast init: " + masterInitiative);
		System.out.println("");

		int gs = 90;
		int ts = 1;		
		Grid earth = new Grid(gs);
		//	earth.diffuse();
	}
}