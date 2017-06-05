package nf28.touchmaze.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Stanza;

import java.util.UUID;

import nf28.touchmaze.R;
import nf28.touchmaze.login.DialogHandler;

public class GameMapActivity extends AppCompatActivity {

    private AbstractXMPPConnection conn;
    private String partnerJID;
    private DialogHandler dialogHandler;
    private boolean partnerConnected;
    private InvitationResultHandler invitationResultHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_map);

        // On récupère la connexion.
        dialogHandler = (DialogHandler)getApplicationContext();
        conn = dialogHandler.getConn();

        // On récupère le nom du partenaire.
        Intent i = getIntent();
        partnerJID = i.getStringExtra("PARTNER");
        TextView guestname = (TextView) findViewById(R.id.textGuestName);
        String partner = partnerJID.substring(0, partnerJID.indexOf("@"));
        guestname.setText("Vous guidez " + partner);

        // On envoie l'invitation.
        invite();
        }

    /**
     * Envoie l'invitation au guest.
     */
    private void invite() {
        // Création du message.
        IQ invitation = new IQ("invitation", "www.intertact.net/invitation") {
            @Override
            protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
                xml.setEmptyElement();
                return xml;
            }
        };
        // Destinataire.
        invitation.setTo(partnerJID + "/Smack");
        // Id.
        invitation.setStanzaId("invitation" + UUID.randomUUID().toString());

        invitationResultHandler = new InvitationResultHandler();
        try {
            // Envoie le message.
            conn.sendIqWithResponseCallback(invitation, invitationResultHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ecoute la réponse à l'invitation.
     */
    private class InvitationResultHandler implements StanzaListener {
        /**
         * Traite un message.
         * @param packet : le message
         * @throws SmackException.NotConnectedException
         */
        @Override
        public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
            IQ iq = (IQ)packet;
            if (iq.getType() == IQ.Type.result && iq.getFrom().equals(partnerJID + "/Smack")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPartnerConnected(true);
                    }
                });
            }
        }
    }

    /**
     * Met à jour l'état du guest.
     * @param partnerConnected
     */
    private void setPartnerConnected(boolean partnerConnected) {
        this.partnerConnected = partnerConnected;
    }
}
