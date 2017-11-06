
import Server.JvnServerImpl;
import java.util.ArrayList;
import jvn.JvnException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author scra
 */
public class SpecCache {

    ISentence sentence;

    public static void main(String argv[]) throws JvnException {
        InvocationProxy.newInstance(new Sentence(), "IRC_0"); // degree 2 
        InvocationProxy.newInstance(new Sentence(), "IRC_1"); // degree 2 
        InvocationProxy.newInstance(new Sentence(), "IRC_2"); // degree 2 
        InvocationProxy.newInstance(new Sentence(), "IRC_3"); // degree 2 
        InvocationProxy.newInstance(new Sentence(), "IRC_4"); // degree 2 
    }

}
