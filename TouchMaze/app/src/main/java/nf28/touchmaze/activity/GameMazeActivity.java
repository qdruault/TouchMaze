package nf28.touchmaze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.json.JSONObject;

import nf28.touchmaze.R;
import nf28.touchmaze.layout.WallLayout;
import nf28.touchmaze.util.touch.DialogTouchEvent;
import nf28.touchmaze.util.touch.TactileDialogViewHolder;

import static nf28.touchmaze.activity.ConnectionActivity.TESTMODE;
import nf28.touchmaze.util.PinsDisplayer;

public class GameMazeActivity extends ChatActivity implements TactileDialogViewHolder {

    private boolean partnerConnected;

    WallLayout tactileAreaTop;
    WallLayout tactileAreaBottom;
    WallLayout tactileAreaLeft;
    WallLayout tactileAreaRight;

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

        tactileAreaTop = (WallLayout) findViewById(R.id.tactile_area_top);
        tactileAreaBottom = (WallLayout) findViewById(R.id.tactile_area_bottom);
        tactileAreaLeft = (WallLayout) findViewById(R.id.tactile_area_left);
        tactileAreaRight = (WallLayout) findViewById(R.id.tactile_area_right);

        tactileAreaTop.setDialogViewHolder(this);
        tactileAreaBottom.setDialogViewHolder(this);
        tactileAreaLeft.setDialogViewHolder(this);
        tactileAreaRight.setDialogViewHolder(this);

        // On dit qu'on est pret.
        try {
            chatOut.sendMessage("READY");
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
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
        if (message.getFrom().equals(partnerJID + "/Smack")) {
            final String messageBody = message.getBody();

            if (END_DIALOG_MESSAGE.equals(messageBody)) {
                // User déconnecté.
            } else if (messageBody.equals("ENIGME")){
                Intent intent = new Intent(GameMazeActivity.this, EnigmaExploActivity.class);
                intent.putExtra("PARTNER", partnerJID);
                startActivityForResult(intent, 10);

            } else if (messageBody.equals("BOOM")){
                // On se prend un mur = animation.
                Animation shakeAnim = AnimationUtils.loadAnimation(GameMazeActivity.this, R.anim.shake);
                findViewById(R.id.main_layout_explo).startAnimation(shakeAnim);

            } else {
                try {
                    // On récupère le JSON envoyé.
                    JSONObject json = new JSONObject(messageBody);
                    // On récupère les propriétés des murs et on les affecte.
                    tactileAreaRight.setActiveWall(json.getBoolean("right"));
                    tactileAreaLeft.setActiveWall(json.getBoolean("left"));
                    tactileAreaTop.setActiveWall(json.getBoolean("top"));
                    tactileAreaBottom.setActiveWall(json.getBoolean("bottom"));
                    // Nos coordonnées.
                    int x = json.getInt("x");
                    int y = json.getInt("y");
                    TextView hostname = (TextView) findViewById(R.id.textHostName);
                    String partner = partnerJID.substring(0, partnerJID.indexOf("@"));
                    hostname.setText(String.format("Guidé par %s. x : %s y : %s", partner, x, y));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDialogTouch(DialogTouchEvent event) {

        // Picots à afficher et lever.
        boolean[] leftTouches = new boolean[]{true, true, true, true, true, true, true, true};
        boolean[] rightTouches = new boolean[]{true, true, true, true, true, true, true, true};

        // Affichage.
        String picots = PinsDisplayer.setAndDisplay(leftTouches, rightTouches);

        byte[] data = new byte[4];
        data[0] = 0x1b;
        data[1] = 0x01;
        data[2] = regularBoolToByte(rectifyTouches(leftTouches));
        data[3] = regularBoolToByte(rectifyTouches(rightTouches));

        Intent sendData = new Intent();

        if (!TESTMODE) {
            sendData.putExtra("BStream", data);
            sendData.setAction("com.example.labocred.bluetooth.StreamBluetooth");
        } else {
            sendData.putExtra("Picots", picots);
            sendData.setAction("com.example.labocred.bluetooth.Test");
        }

        // Envoie de l'intent pour le module tactos.
        sendBroadcast(sendData);

    }

    @Override
    public OPTIONS GetOptions() {
        return null;
    }

    /**
     * Convertir le tableau de bool en byte.
     * @param p_tab : le tableau de bool à convertir
     * @return
     */
    static byte regularBoolToByte(boolean[] p_tab) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            // Remplace les false par 0 et les true par 1.
            output.append(p_tab[i] ? '1' : '0');
        }

        return (byte) Integer.parseInt(output.toString(), 2);
    }

    /**
     * Remet le tableau dans l'ordre.
     * Don't ask me why. I don't know
     */
    static boolean[] rectifyTouches(boolean[] t) {
        return new boolean[]{t[1], t[0], t[3], t[5], t[7], t[2], t[4], t[6]};
    }

    // Récupération du resultat de la seconde activité.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Vérification de l'intent grace a son identifiant
        if (requestCode == 10) {
            Toast.makeText(GameMazeActivity.this, "Enigme réussie !!", Toast.LENGTH_SHORT).show();String guideMessage = "STOP";
            try {
                chatOut.sendMessage("STOP");
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }
}
