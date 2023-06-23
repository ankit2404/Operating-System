package HelloWorldPrinter;

public class Client {
    public static void main(String[] args) {
        System.out.println("hello " + Thread.currentThread().getName());
//        HelloWorldPrinter hvp = new HelloWorldPrinter();
//        Thread t1 = new Thread(hvp);
//        t1.start();

        for(int i = 0 ; i < 100; i++){
        PrintNumber pv = new PrintNumber(i);
        Thread t = new Thread(pv);
        t.start();

        }
        System.out.println("After Thread Creation " + Thread.currentThread().getName());
    }
}
