
import Server.JvnServerImpl;
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
public class SpecCache2 {

    ISentence sentence;
    
    public static void main(String argv[]) throws JvnException {
        InvocationProxy.newInstance(new Sentence(), "IRC_0"); // degree 3
        InvocationProxy.newInstance(new Sentence(), "IRC_1"); // degree 3 
        InvocationProxy.newInstance(new Sentence(), "IRC_2"); // degree 3 
        InvocationProxy.newInstance(new Sentence(), "IRC_3"); // degree 3

        //The 4 will be destroy
        //The cache of the coord need to remove a sentence to add a new sentence (IRC_5)
        InvocationProxy.newInstance(new Sentence(), "IRC_5");

        JvnServerImpl js = JvnServerImpl.jvnGetServer();
        Object ob = js.jvnLookupObject("IRC_4");
        System.out.println("Should be null: " + ob);

        ob = js.jvnLookupObject("IRC_5");
        System.out.println("Should not be null: " + ob);
    }

}
