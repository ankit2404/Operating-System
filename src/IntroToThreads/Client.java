package IntroToThreads;

public class Client {
    public static void main(String[] args) {
        // Thread.currentThread().setName("initial");
        System.out.println("hello " + Thread.currentThread().getName());
        doSomething();
    }

    public static void doSomething() {
        System.out.println("DO Something " + Thread.currentThread().getName());

    }
}
