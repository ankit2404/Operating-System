package HelloWorldPrinter;

public class PrintNumber implements  Runnable{
    int number;
    PrintNumber(int no){
        this.number = no;
    }
    public void run(){
        System.out.println("printing from PrintNumber " + number + "  " + Thread.currentThread().getName() );
    }
}
