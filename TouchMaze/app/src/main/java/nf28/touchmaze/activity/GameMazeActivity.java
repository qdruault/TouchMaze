package nf28.touchmaze.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;

import nf28.touchmaze.R;

public class GameMazeActivity extends ChatActivity {

    private boolean partnerConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_maze);

        TextView hostname = (TextView) findViewById(R.id.textHostName);
        String partner = partnerJID.substring(0, partnerJID.indexOf("@"));
        hostname.setText(String.format("Guidé par  %s", partner));

        // On accepte l'invitation.
        dialogHandler.acceptInvitation(partnerJID);
        setPartnerConnected(true);

        // Ajout des listeners sur les boutons.
        Button btn_up = (Button)findViewById(R.id.btn_up);
        Button btn_down = (Button)findViewById(R.id.btn_down);
        Button btn_right = (Button)findViewById(R.id.btn_right);
        Button btn_left = (Button)findViewById(R.id.btn_left);
        Button btns[] = {btn_up, btn_down, btn_right, btn_left};
        for (Button btn : btns) {
            btn.setOnClickListener(clickMove(btn));
        }

    }

    /**
     * Met à jour l'état du guest.
     * @param partnerConnected
     */
    private void setPartnerConnected(boolean partnerConnected) {
        this.partnerConnected = partnerConnected;
    }

    /**
     * Génère un listener pour le clic sur les boutons.
     * @param btn
     * @return
     */
    private View.OnClickListener clickMove(final Button btn) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On récupère le contenu du bouton.
                String direction = btn.getText().toString();
                try {
                    // On envoie au guide notre direction.
                    chatOut.sendMessage(direction);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Réception d'un message du partenaire.
     * @param chat
     * @param message
     */
    @Override
    public void processMessage(Chat chat, final Message message) {

    }
}
