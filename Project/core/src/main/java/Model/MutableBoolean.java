package Model;

public class MutableBoolean{
    private boolean mutableBoolean;
    public MutableBoolean(){
        this.mutableBoolean = false;
    }
    public MutableBoolean(boolean bool){
        this.mutableBoolean = bool;
    }

    public void setBoolean(boolean value){
        this.mutableBoolean = value;
    }
    
    public boolean getBoolean(){
        return this.mutableBoolean;
    }
}