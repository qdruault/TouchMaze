package maze;

/**
 * Created by Thibault on 06/06/2017.
 */
public abstract class Communicator {

    protected void sendMessage(String s) {
        Thread t = new Thread() {
            public void run() {
                /*
                Ici, le code pour envoyer le message sur le reseau
                 */
            }
        };
        t.start();
    }

    public abstract void receiveMessage(String s);
}
