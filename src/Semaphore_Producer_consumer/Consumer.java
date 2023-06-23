package Semaphore_Producer_consumer;

import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Consumer implements Runnable {
    private Queue<Object> queue;
    private int max;
    private String name;
    private Semaphore consumerSemaphore;
    private Semaphore producerSemaphore;
    public Consumer(Queue<Object> qu, int max, String name, Semaphore consumerSemaphore, Semaphore producerSemaphore){
        this.queue = qu;
        this.max = max;
        this.name = name;
        this.consumerSemaphore = consumerSemaphore;
        this.producerSemaphore = producerSemaphore;
    }
    public void run(){
        while (true) {
            try {
                consumerSemaphore.acquire();
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Removing an element " + this.name + " Queue Size is " + queue.size());
            queue.remove();
            producerSemaphore.release();
        }
    }
}
