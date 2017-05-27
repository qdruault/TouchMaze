package nf28.touchmaze.login;

import android.os.Handler;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PresenceTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Binova on 10/12/2015.
 */
public class DialogHandler {
    // Infos du serveur.
    public static final String HOST = "servgsp.utc.fr";
    public static final String SERVICE = "servgsp.utc.fr";
    public static final int PORT = 8080;

    private AbstractXMPPConnection connection;
    private DialogInvitationHandler dialogInvitationHandler;
    private Map<String, IQ> invitations = new HashMap<>();

    private static Handler disconnectHandler;

    /**
     * Renvoie la connexion.
     * @param username
     * @param password
     * @return
     * @throws SmackException
     * @throws IOException
     * @throws XMPPException
     */
    public AbstractXMPPConnection realConnection(String username, String password) throws SmackException, IOException, XMPPException {

        // Infos du serveur.
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder()
                .setServiceName(DialogHandler.SERVICE)
                .setHost(DialogHandler.HOST)
                .setPort(DialogHandler.PORT)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)//@todo: no security here!
                .setConnectTimeout(10000);

        // Identifiants pour la connexion.
        if (username != null && password != null) {
            builder.setUsernameAndPassword(username, password);
        }

        // On génère la connexion.
        AbstractXMPPConnection conn = new XMPPTCPConnection(builder.build());
        conn.connect();

        if (username != null && password != null) {
            // On se connecte.
            conn.login();

            conn.registerIQRequestHandler(getDialogInvitationHandler());
            conn.addAsyncStanzaListener(new SubscribeHandler(conn), PresenceTypeFilter.SUBSCRIBE);
        }

        connection = conn;

        return conn;
    }

    /**
     * Se connecte avec username et password.
     * @param username
     * @param password
     * @return
     * @throws SmackException
     * @throws IOException
     * @throws XMPPException
     */
    public AbstractXMPPConnection connect(String username, String password) throws SmackException, IOException, XMPPException
    {
        return realConnection(username, password);
    }

    /**
     * Se connecte sans identifiants.
     * @return
     * @throws SmackException
     * @throws IOException
     * @throws XMPPException
     */
    public AbstractXMPPConnection connect() throws SmackException, IOException, XMPPException
    {
        return realConnection(null, null);
    }

    /**
     * Retourne la connexion.
     * @return
     */
    public AbstractXMPPConnection getConn() {
        return connection;
    }

    /**
     * Retourne le DialogInvitationHandler.
     * @return
     */
    public DialogInvitationHandler getDialogInvitationHandler() {
        if (dialogInvitationHandler == null) {
            dialogInvitationHandler = new DialogInvitationHandler(this);
        }

        return dialogInvitationHandler;
    }

    /**
     * Passe le statut en "non disponible".
     */
    public void disconnect() {
        Presence offlinePresence = new Presence(Presence.Type.unavailable);
        try {
            connection.sendStanza(offlinePresence);
        } catch(SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        connection.disconnect();
    }

    /**
     * Ajout le nom de la personne à notre liste d'invitations.
     * @param invitationIQ
     */
    public void addInvitation(IQ invitationIQ) {
        invitations.put(invitationIQ.getFrom(), invitationIQ);
    }

    /**
     * Cherche un membre dans nos invitations.
     * @param bareJID
     * @return
     */
    public boolean hasInvited(String bareJID) {
        return invitations.containsKey(bareJID + "/Smack");
    }

    /**
     * Accepte une invitation.
     * @param partnerJID
     */
    public void acceptInvitation(String partnerJID) {
        // Récupère l'invitation.
        IQ invitation = invitations.get(partnerJID + "/Smack");
        // Enlève l'invitaiton de la liste.
        invitations.remove(partnerJID + "/Smack");
        // Prépare la réponse.
        IQ response = new IQ("accept_invitation", "www.intertact.net/invitation")

        {
            @Override
            protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
                xml.setEmptyElement();
                return xml;
            }
        };
        response.setTo(partnerJID + "/Smack");
        response.setType(IQ.Type.result);
        response.setStanzaId(invitation.getStanzaId());
        try {
            // Envoie la réponse.
            connection.sendStanza(response);
        } catch (SmackException.NotConnectedException e) { }
    }
}
