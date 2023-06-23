package Adder_Subtracter;

import java.sql.SQLOutput;
import java.util.concurrent.locks.Lock;

public class Subtracter implements Runnable{
    private Count count;
    private Lock lock;
    public Subtracter(Count count,Lock lock){
        this.count = count;
        this.lock = lock;
    }
    public void run(){
        System.out.println("Subtracter Started");
        for(int i = 0  ; i < 10000; i++){
//            lock.lock();
//            this.count.num--;
//            lock.unlock();
            this.count.num.addAndGet(-1);
        }
        System.out.println("Subtracter Completed");
    }
}
