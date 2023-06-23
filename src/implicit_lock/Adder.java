package implicit_lock;

import java.util.concurrent.locks.Lock;

public class Adder implements Runnable{
    private Lock lock;
    private Count count;
    public Adder(Count count, Lock lock){
        this.count = count;
        this.lock = lock;
    }
    public void run(){
        System.out.println("Adder Started");
        for(int i = 0  ; i < 10000; i++){
            synchronized (count) {
                this.count.num++;
            }
        }
        System.out.println("Adder Completed");
    }
}
