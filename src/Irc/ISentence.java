package Irc;


/**
 *
 * @author scra
 */

import JvnObject.MethodAnnotation;

public interface ISentence {
    
    @MethodAnnotation(type = "write")
    public void write(String text);

    @MethodAnnotation(type = "read")
    public String read();
}

