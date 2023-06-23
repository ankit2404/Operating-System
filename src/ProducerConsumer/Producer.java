package ProducerConsumer;

import java.util.Queue;

public class Producer implements Runnable{
    private Queue<Object> queue;
    private int max;
    private String name;
    public Producer(Queue<Object> qu, int max, String name){
        this.queue = qu;
        this.max = max;
        this.name = name;
    }
    public void run(){
        while (true){
            if(queue.size() < max){
                System.out.println("Adding an element " + this.name + " Queue Size is " + queue.size());

                queue.add(new Object());
            }
        }
    }
}
