package base;

/**
 * PausableStoppable provides some core functionality for classes with
 * concurrent actions that can be started, stopped, paused, and resumed.
 * 
 * @author Tyler Benfield
 *
 */
public abstract class PausableStoppable {

	/**
	 * Stores a reference to the currently executing thread. Once the thread is
	 * stopped, this variable is set back to null.
	 */
	protected Thread mRunningThread;

	/**
	 * Holds the current pause state. If true, the thread should pause at the
	 * next available opportunity.
	 */
	protected Boolean mPaused = false;

	/**
	 * Holds the specified degrees of separation
	 */
	protected int mDegreeSeparation = 15;

	/**
	 * Holds the specified presentation display rate
	 */
	protected int mDisplayRate = 1;

	/**
	 * Holds the specified time step
	 */
	protected int mTimeStep = 1;

	/**
	 * Pauses the underlying thread if it is executing. Throws an exception if
	 * the thread has not been started.
	 * 
	 * @throws Exception
	 *             Thrown if the thread has not been started.
	 */
	public void pause() throws Exception {
		if (mRunningThread == null) {
			throw new Exception("Thread has not been started");
		} else {
			synchronized (mPaused) {
				mPaused = true;
			}
		}
	}

	/**
	 * Resumes the underlying thread if it is paused. Throws an exception if the
	 * thread has not been started.
	 * 
	 * @throws Exception
	 *             Thrown if the thread has not been started.
	 */
	public void resume() throws Exception {
		if (mRunningThread == null) {
			throw new Exception("Thread has not been started");
		} else {
			synchronized (mPaused) {
				Boolean toNotify = mPaused;
				mPaused = false;
				toNotify.notify();
			}
		}
	}

	/**
	 * Starts the the Runnable from getRunnableAction() on a new thread and
	 * begins the pause/resume/stop process.
	 * 
	 * @throws Exception
	 *             Thrown if the thread has already been started.
	 */
	public void start(int degreeSeparation, int timeStep, int displayRate)
			throws Exception {
		if (mRunningThread == null) {
			mDegreeSeparation = degreeSeparation;
			mDisplayRate = displayRate;
			mTimeStep = timeStep;
			mRunningThread = new Thread(getRunnableAction());
			mRunningThread.start();
		} else {
			throw new Exception("Thread already started");
		}
	}

	/**
	 * Stops the currently executing thread. Throws an exception if the thread
	 * has not been started.
	 * 
	 * @throws Exception
	 *             Thrown if the thread has not been started.
	 */
	public void stop() throws Exception {
		if (mRunningThread == null) {
			throw new Exception("Thread has not been started");
		} else {
			mRunningThread.interrupt();
		}
	}

	/**
	 * Checks the pause state of the thread and waits if necessary.
	 * 
	 * @throws InterruptedException
	 *             Thrown if the thread is interrupted while paused.
	 */
	protected synchronized void checkPaused() throws InterruptedException {
		synchronized (mPaused) {
			if (mPaused) {
				mPaused.wait();
			}
		}
	}

	/**
	 * Should be overridden to return a Runnable to execute in the thread when
	 * start() is called.
	 * 
	 * @return Runnable that will be executed on the underlying thread.
	 */
	protected abstract Runnable getRunnableAction();
}
