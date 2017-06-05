package nf28.touchmaze.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;

import nf28.touchmaze.R;
import nf28.touchmaze.login.DialogHandler;

public class GameMazeActivity extends AppCompatActivity {

    private AbstractXMPPConnection conn;
    private String partnerJID;
    private DialogHandler dialogHandler;
    private boolean partnerConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_maze);

        // On récupère la connexion.
        dialogHandler = (DialogHandler)getApplicationContext();
        conn = dialogHandler.getConn();

        // On récupère le nom du partenaire.
        Intent i = getIntent();
        partnerJID = i.getStringExtra("PARTNER");

        TextView hostname = (TextView) findViewById(R.id.textHostName);
        String partner = partnerJID.substring(0, partnerJID.indexOf("@"));
        hostname.setText("Guidé par " + partner);

        // On accpepte l'invitation.
        dialogHandler.acceptInvitation(partnerJID);
        setPartnerConnected(true);
    }

    /**
     * Met à jour l'état du guest.
     * @param partnerConnected
     */
    private void setPartnerConnected(boolean partnerConnected) {
        this.partnerConnected = partnerConnected;
    }
}
