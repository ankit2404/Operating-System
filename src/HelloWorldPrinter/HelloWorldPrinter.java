package HelloWorldPrinter;

public class HelloWorldPrinter implements Runnable{
    public static void doSomething() {
        System.out.println("Doing Something " + Thread.currentThread().getName());

    }
    public void  run(){
        System.out.println("Hello from print " + Thread.currentThread().getName());
        doSomething();
    }
}
