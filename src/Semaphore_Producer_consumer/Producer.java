package Semaphore_Producer_consumer;

import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Producer implements Runnable{
    private Queue<Object> queue;
    private int max;
    private String name;
    private Semaphore producerSemaphore;
    private Semaphore consumerSemaphore;
    public Producer(Queue<Object> qu, int max, String name, Semaphore producerSemaphore, Semaphore consumerSemaphore){
        this.queue = qu;
        this.max = max;
        this.name = name;
        this.producerSemaphore = producerSemaphore;
        this.consumerSemaphore = consumerSemaphore;
    }
    public void run(){
        while (true) {
            try {
                producerSemaphore.acquire();
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("adding an element " + this.name + " Queue Size is " + queue.size());
            queue.add(new Object());
            consumerSemaphore.release();
        }
    }
}
