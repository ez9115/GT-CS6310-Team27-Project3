package base;

public abstract class PausableStoppable {
	
	protected Thread runningThread;
	protected Boolean paused = false;
	
	public void pause() throws Exception {
		if (runningThread == null) {
			throw new Exception("Thread has not been started");
		} else {
			synchronized(paused) {
				paused = true;
			}
		}
	}
	
	public void resume() throws Exception {
		if (runningThread == null) {
			throw new Exception("Thread has not been started");
		} else {
			synchronized(paused) {
				Boolean toNotify = paused;
				paused = false;
				toNotify.notify();
			}
		}
	}
	
	public void start() throws Exception {
		if (runningThread == null) {
			runningThread = new Thread(getRunnableAction());
			runningThread.start();
		} else {
			throw new Exception("Thread already started");
		}
	}
	
	public void stop() throws Exception {
		if (runningThread == null) {
			throw new Exception("Thread has not been started");
		} else {
			runningThread.interrupt();
		}
	}
	
	protected synchronized void checkPaused() throws InterruptedException {
		synchronized(paused) {
			if (paused) {
				paused.wait();
			}
		}
	}
	
	protected abstract Runnable getRunnableAction();
}
