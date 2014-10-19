package mock;

import java.util.Random;

import base.SimulationMethod;
import base.SimulationResult;

/**
 * Mock simulation method for testing purposes. 
 * @author Tyler Benfield
 *
 */
public class MockSimulationMethod implements SimulationMethod {

	@Override
	public SimulationResult simulate(SimulationResult previousResults, int degreeSeparation, float sunPosition) throws InterruptedException {
		Thread.sleep(250);
		return new SimulationResult(randInt(-100,92)); //288k is the starting temp
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

}
