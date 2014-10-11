package base;

import java.util.ArrayList;

public class Experiments {
	
	/**
	 * Using an ArrayList buffer so that it can grow and shrink
	 */
	public static volatile ArrayList<Integer> buffer = new ArrayList<Integer>();
	public static int maxBufferSize = 10;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		controllerBothAsync();
		//controllerAsyncMix();
		//controllerBothSync();
	}
	
	/**
	 * Multi-threaded simulation and GUI
	 * Adjusting timeouts throughout process and maxBufferSize will affect which how fast the buffer can fill
	 */
	private static void controllerBothAsync() {
		
		AsyncController controller = new AsyncController();
		
		controller.start();
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		controller.stop();
		
	}
	
	/**
	 * Single-threaded, should always be one simulation per GUI update 
	 */
	private static void controllerBothSync() {
		SyncController controller = new SyncController();
		controller.start();
	}
	
	/**
	 * Threaded simulation, GUI stays on main thread
	 * Adjusting timeouts throughout process and maxBufferSize will affect which how fast the buffer can fill
	 */
	private static void controllerAsyncMix() {
		AsyncMixController controller = new AsyncMixController();
		controller.start();
	}
	
	
	public static class AsyncController implements RequestUpdate, ProgressUpdate {
		
		private Simulation mSim;
		private Presentation mPres;
		private Thread mSimThread;
		private Thread mPresThread;
		private boolean mRunning;
		
		public AsyncController() {
			mSim = new Simulation(this);
			mPres = new Presentation(this);
			mRunning = false;
		}

		@Override
		public void onProgressUpdate() {
			mPres.updateAvailable();
		}

		@Override
		public void onRequestUpdate() {
			mSim.updateRequested();
		}
		
		public void start() {
			if (!mRunning) {
				mSimThread = mSim.runAsync();
				mPresThread = mPres.runAsync();
				mRunning = true;
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mSim.updateRequested();
			}
		}
		
		public void stop() {
			if (mRunning) {
				mPresThread.interrupt();
				mSimThread.interrupt();
				mRunning = false;
			}
		}
		
	}
	
	/**
	 * Simulation runs asynchronous, synchronous changes are pushed to the GUI.
	 * @author Tyler
	 *
	 */
	public static class AsyncMixController implements ProgressUpdate {
		
		private Simulation mSim;
		private Presentation mPres;
		private boolean mRunning;
		
		public AsyncMixController() {
			mSim = new Simulation(this);
			mPres = new Presentation();
			mRunning = false;
		}

		@Override
		public void onProgressUpdate() {
			synchronized(this) {
				this.notify();
			}
		}
		
		public void start() {
			if (!mRunning) {
				mSim.runAsync();
				mRunning = true;
				
				while(!Thread.interrupted()) {
					try {
						synchronized(this) {
							Thread.sleep(3000);
							if (buffer.size() == 0) {
								this.wait();
							}
						}
						mPres.update();
						mSim.updateRequested();
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}
		
	}
	
	public static class SyncController {
		
		private Simulation mSim;
		private Presentation mPres;
		
		public SyncController() {
			mSim = new Simulation();
			mPres = new Presentation();
		}
		
		public void start() {
			while(true) {
				try {
					mSim.simulate();
					mPres.update();
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
		
	}
	
	
	public static class Simulation implements RequestUpdate {
		
		private ProgressUpdate mUpdateTarget;
		
		private int mCounter;
		
		public Simulation() {
			this(null);
		}
		
		public Simulation(ProgressUpdate updateTarget) {
			mUpdateTarget = updateTarget;
			mCounter = 0;
		}
		
		public void simulate() throws InterruptedException {
			mCounter++;
			synchronized(buffer) {
				buffer.add(mCounter);
			}
			
			Thread.sleep(100);
			
			if (mUpdateTarget != null) {
				mUpdateTarget.onProgressUpdate();
			}
		}
		
		public Thread runAsync() {
			final Simulation parent = this;
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(!Thread.interrupted()) {
						try {
							synchronized(parent) {
								if (buffer.size() >= maxBufferSize) {
									parent.wait();
								}
							}
							simulate();
						} catch (InterruptedException e) {
							return;
						}
					}
				}
				
			});
			t.start();
			return t;
		}
		
		public void onRequestUpdate() {
			try {
				simulate();
			} catch (InterruptedException e) {
				return;
			}
		}
		
		public void updateRequested() {
			synchronized(this) {
				this.notify();
			}
		}
	}
	
	public static class Presentation implements ProgressUpdate {
		
		private RequestUpdate mRequestTarget;
		
		public Presentation() {
			this(null);
		}
		
		public Presentation(RequestUpdate requestTarget) {
			mRequestTarget = requestTarget;
		}
		
		public void update() throws InterruptedException {
			synchronized(buffer) {
				if (buffer.size() > 0) {
					System.out.println("Counter: " + buffer.get(0) + " Buffer: " + buffer.size());
					buffer.remove(0);
				}
			}
			
			Thread.sleep(500);
			
			if (mRequestTarget != null) {
				mRequestTarget.onRequestUpdate();
			}
		}
		
		public Thread runAsync() {
			final Presentation parent = this;
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(!Thread.interrupted()) {
						try {
							synchronized(parent) {
								Thread.sleep(3000);
								if (buffer.size() == 0) {
									parent.wait();
								}
							}
							update();
						} catch (InterruptedException e) {
							return;
						}
					}
				}
				
			});
			t.start();
			return t;
		}
		
		public void onProgressUpdate() {
			try {
				update();
			} catch (InterruptedException e) {
				return;
			}
		}
		
		public void updateAvailable() {
			synchronized(this) {
				this.notify();
			}
		}
	}
	
	public static interface ProgressUpdate {
		void onProgressUpdate();
	}
	
	public static interface RequestUpdate {
		void onRequestUpdate();
	}

}
