package Spec;

/**
 * *
 * Irc class : simple implementation of a chat using JAVANAISE Contact:
 *
 * Authors:
 */
import Irc.ISentence;
import Irc.Sentence;
import JvnObject.JvnDynamicProxy;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpecIrc {

    ISentence sentence;

    /**
     * main method create a JVN object nammed IRC for representing the Chat
     * application
     *
     */
    public static void main(String argv[]) {
        new SpecIrc((ISentence) JvnDynamicProxy.intitialyze(
                new Sentence(),
                "IRC"
        ));
    }

    public SpecIrc(ISentence sentence) {
        this.sentence = sentence;

        try {
            startSpec();
        } catch (InterruptedException ex) {
            Logger.getLogger(SpecIrc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startSpec() throws InterruptedException {
        int i = 0;
        sleep(5000);
        while (i <= 10000000) {
            String s = this.sentence.read();
            System.out.println("READ : " + s);

            i++;

            s = i + "_" + "write";
            this.sentence.write(s);
            System.out.println("WRITE");
            System.out.println(s);
        }
    }
}
