package ProducerConsumerPatternExperiment;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
	 
    private BlockingQueue<String> queue;
     
    public Producer(BlockingQueue<String> q){
        this.queue=q;
    }
    @Override
    public void run() {
        //produce messages
        for(int i=1; i<=50; i++){
            String msg = ""+i;
            try {
           //     Thread.sleep(i);
                queue.put(msg);
                System.out.println("Produced "+msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("size" + queue.size());
        }
        //adding exit message
        String msg = "exit";
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("size" + queue.size());
    }
 
}