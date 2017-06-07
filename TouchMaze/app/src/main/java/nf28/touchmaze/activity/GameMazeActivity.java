package nf28.touchmaze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;

import nf28.touchmaze.R;
import nf28.touchmaze.layout.SurfaceLayout;
import nf28.touchmaze.util.touch.DialogTouchEvent;
import nf28.touchmaze.util.touch.TactileDialogViewHolder;

import static nf28.touchmaze.activity.ConnectionActivity.TESTMODE;
import static nf28.touchmaze.util.PinsDisplayer.setAndDisplay;

public class GameMazeActivity extends ChatActivity implements TactileDialogViewHolder {

    private boolean partnerConnected;

    SurfaceLayout tactileAreaTop;
    SurfaceLayout tactileAreaBottom;
    SurfaceLayout tactileAreaLeft;
    SurfaceLayout tactileAreaRight;

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

        tactileAreaTop = (SurfaceLayout) findViewById(R.id.tactile_area_top);
        tactileAreaBottom = (SurfaceLayout) findViewById(R.id.tactile_area_bottom);
        tactileAreaLeft = (SurfaceLayout) findViewById(R.id.tactile_area_left);
        tactileAreaRight = (SurfaceLayout) findViewById(R.id.tactile_area_right);

        tactileAreaTop.setDialogViewHolder(this);
        tactileAreaBottom.setDialogViewHolder(this);
        tactileAreaLeft.setDialogViewHolder(this);
        tactileAreaRight.setDialogViewHolder(this);

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
        // MAJ DES BOOLEAN MURS DES TACTILES AREAS DE LA VUE
    }

    @Override
    public void onDialogTouch(DialogTouchEvent event) {

        // Picots à afficher et lever.
        boolean[] leftTouches = new boolean[]{true, true, true, true, true, true, true, true};
        boolean[] rightTouches = new boolean[]{true, true, true, true, true, true, true, true};

        // Affichage.
        setAndDisplay(leftTouches, rightTouches);

        byte[] data = new byte[4];
        data[0] = 0x1b;
        data[1] = 0x01;
        data[2] = regularBoolToByte(rectifyTouches(leftTouches));
        data[3] = regularBoolToByte(rectifyTouches(rightTouches));

        Intent sendData = new Intent();

        sendData.putExtra("BStream", data);

        if (!TESTMODE)
            sendData.setAction("com.example.labocred.bluetooth.StreamBluetooth");
        sendData.setAction("com.example.labocred.bluetooth..Test");

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
}
