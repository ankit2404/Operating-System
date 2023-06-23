package Executers.PrintTillHundred;

public class PrintNumber implements  Runnable{
    int number;
    public PrintNumber(int no){
        this.number = no;
    }
    public void run(){
        System.out.println("printing from PrintNumber " + number + "  " + Thread.currentThread().getName() );
    }
}
