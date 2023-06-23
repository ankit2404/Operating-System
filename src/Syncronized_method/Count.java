package Syncronized_method;

public class Count {
    private int value;
    public synchronized void incrementValue(int offSet){
        this.value += offSet;
    }
    public int getValue(){
        return this.value;
    }
}
