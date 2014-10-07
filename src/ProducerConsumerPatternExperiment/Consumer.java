package ProducerConsumerPatternExperiment;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
	 
private BlockingQueue<String> queue;
     
    public Consumer(BlockingQueue<String> q){
        this.queue=q;
    }
 
    @Override
    public void run() {
        try{
            String msg;
            //consuming messages until exit message is received
            while((msg = queue.take()) !="exit"){
            Thread.sleep(100);
            System.out.println("Consumed "+msg+ "osize" + queue.size());
            }
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}