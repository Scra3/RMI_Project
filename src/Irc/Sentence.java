package Irc;

public class Sentence implements java.io.Serializable, ISentence {

    String data;
    
    @Override
    public void write(String text) {
        data = text;
    }

    @Override
    public String read() {
        return data;
    }
}
