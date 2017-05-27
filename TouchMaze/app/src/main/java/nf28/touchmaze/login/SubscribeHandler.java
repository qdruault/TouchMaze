package nf28.touchmaze.login;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by Quentin on 24/05/2017.
 */

public class SubscribeHandler implements StanzaListener
{
    AbstractXMPPConnection connection;

    public SubscribeHandler(AbstractXMPPConnection connection) {
        this.connection = connection;
    }

    /**
     * Traitement d'un message reçu.
     * @param packet : le message.
     */
    @Override
    public void processPacket(Stanza packet) {
        // On envoie un message subscribe à l'expéditeur.
        Presence subscribeBack = new Presence(Presence.Type.subscribe);
        subscribeBack.setTo(packet.getFrom());
        try {
            connection.sendStanza(subscribeBack);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }
}