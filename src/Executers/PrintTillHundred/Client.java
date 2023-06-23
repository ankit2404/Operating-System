package Executers.PrintTillHundred;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static void main(String[] args) {
        System.out.println("hello " + Thread.currentThread().getName());
        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0 ; i < 100; i++){
            if(i == 5 || i == 11 || i == 30){
                System.out.println("Debug");
            }
            PrintNumber numberPrinter = new PrintNumber(i);
            executorService.execute(numberPrinter);
        }
        executorService.shutdown();
        System.out.println("After Thread Creation " + Thread.currentThread().getName());
    }
}
