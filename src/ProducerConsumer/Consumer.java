package ProducerConsumer;

import java.util.Queue;

public class Consumer implements Runnable {
    private Queue<Object> queue;
    private int max;
    private String name;

    public Consumer(Queue<Object> qu, int max, String name){
        this.queue = qu;
        this.max = max;
        this.name = name;
    }
    public void run(){
        while (true){
            if(queue.size() > 0){
                System.out.println("Removing an element " + this.name + " Queue Size is " + queue.size());
                queue.remove();
            }
        }
    }
}
