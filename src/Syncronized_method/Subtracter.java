package Syncronized_method;

import java.util.concurrent.locks.Lock;

public class Subtracter implements Runnable{
    private Count count;
    private Lock lock;
    public Subtracter(Count count, Lock lock){
        this.count = count;
        this.lock = lock;
    }
    public void run(){
        System.out.println("Subtracter Started");
        for(int i = 0  ; i < 10000; i++){

                count.incrementValue(-1);
            }

        System.out.println("Subtracter Completed");
    }
}
