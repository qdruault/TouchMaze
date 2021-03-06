package nf28.touchmaze.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

import java.util.UUID;

import nf28.touchmaze.R;
import nf28.touchmaze.layout.EnigmaSurfaceLayout;
import nf28.touchmaze.layout.MapLayout;
import nf28.touchmaze.layout.ShapeLayout;
import nf28.touchmaze.maze.maze.Direction2D;
import nf28.touchmaze.maze.maze.Maze2D;
import nf28.touchmaze.maze.position.Position2D;
import nf28.touchmaze.util.PinsDisplayer;
import nf28.touchmaze.util.TutoAlertDialogFragment;
import nf28.touchmaze.util.enigmaActivity.tacticon.ByteAdaptable;
import nf28.touchmaze.util.touch.DialogTouchEvent;
import nf28.touchmaze.util.touch.TactileDialogViewHolder;

import static android.R.color.black;
import static nf28.touchmaze.R.color.orange;
import static nf28.touchmaze.activity.ConnectionActivity.TESTMODE;
import static nf28.touchmaze.activity.GameMazeActivity.rectifyTouches;
import static nf28.touchmaze.activity.GameMazeActivity.regularBoolToByte;

public class GameMapActivity extends ChatActivity  implements TactileDialogViewHolder {

    private boolean partnerConnected;
    private InvitationResultHandler invitationResultHandler;
    private Maze2D maze;

    private MapLayout mapLayout;

    private boolean enigmeAck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_map);

        // On affiche le nom du joueur.
        TextView guestname = (TextView) findViewById(R.id.textGuestName);
        String partner = partnerJID.substring(0, partnerJID.indexOf("@"));
        guestname.setText("Vous guidez " + partner);

        // On envoie l'invitation.
        invite();

        try {
            maze = new Maze2D();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mapLayout = (MapLayout) findViewById(R.id.mapLayout);
        mapLayout.constructMazeV(maze);

        mapLayout.setDialogViewHolder(this);

        Log.d("test", "test");

        showDialog();

    }

    public void showDialog() {
        String message = "Vous êtes le guide !\n" +
                "Coopérez avec votre partenaire pour résoudre les énigmes et le guider vers la sortie.|n" +
                "La carte à l'écran est tactile et permet de sentir les murs du labyrinthe.\n" +
                "Le carré vert représente votre coéquipier, les carrés rouges les énigmes et le carré bleu la sortie.\n" +
                "La sortie s'ouvre uniquement lorsque les trois énigmes sont résolues.\n" +
                "Attention, certains murs ne sont détectables que par l'un des deux joueurs, communiquez et avancez en équipe !";

        DialogFragment newFragment = TutoAlertDialogFragment.newInstance(
                message);
        newFragment.show(getFragmentManager(), "dialog");
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

    public Maze2D getMaze() {
        return maze;
    }

    public void setMaze(Maze2D maze) {
        this.maze = maze;
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

    /**
     * Réception d'un message du partenaire.
     * @param chat
     * @param message
     */
    @Override
    public void processMessage(Chat chat, Message message) {
        // On récupère le contenu du message.
        String messageBody = message.getBody();
        if (END_DIALOG_MESSAGE.equals(messageBody)) {
            // User déconnecté.
            finish();
        } else if (messageBody.equals("READY")) {
            // Le partenaire est prêt, on lui envoie les murs de sa position de départ.
            sendWallsMessage();
        } else if (messageBody.equals("ENIGMEACK")) {
            enigmeAck=true;
        } else if (messageBody.equals("right")|| messageBody.equals("up")|| messageBody.equals("down")|| messageBody.equals("left")){
            // On essaye de bouger l'explorateur.
            Direction2D direction;
            switch (messageBody) {
                case "right":
                    direction = new Direction2D(maze, "RIGHT");
                    break;
                case "up":
                    direction = new Direction2D(maze, "FRONT");
                    break;
                case "down":
                    direction = new Direction2D(maze, "REAR");
                    break;
                default:
                    direction = new Direction2D(maze, "LEFT");
                    break;
            }

            Log.d("POSITION", String.valueOf(maze.getExplorerPosition().x) + " " + String.valueOf(maze.getExplorerPosition().y));

            Position2D oldPos = new Position2D(maze.getExplorerPosition().x, maze.getExplorerPosition().y);
            maze.moveTo(direction);

            Log.d("POSITION", String.valueOf(maze.getExplorerPosition().x) + " " + String.valueOf(maze.getExplorerPosition().y));

            // On se prend un mur.
            if (oldPos.is(maze.getExplorerPosition())) {
                // On vibre
                try {
                    chatOut.sendMessage("BOOM");
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }

            // Enigmes.
            Position2D enigmaToRemove = null;
            for (Position2D positionEnigma : maze.getEnigmas()) {
                if (positionEnigma.is(maze.getExplorerPosition())) {
                    // ON prévient l'explo de l'enigme.
                        MessageThread mt = new MessageThread();
                        mt.start();

                        // On lance l'enigme.
                        Intent intent = new Intent(GameMapActivity.this, EnigmaGuideActivity.class);
                        intent.putExtra("PARTNER", partnerJID);
                        startActivityForResult(intent, 10);
                        // On stocke sa position pour la retirer après.

                        //enigmaToRemove = new Position2D(positionEnigma.x, positionEnigma.y);
                        enigmaToRemove = positionEnigma;
                }
            }

            // On la retire de la liste si résolue.
            if (enigmaToRemove != null) {

                Log.d("POSITION", "List des enigmes: " + String.valueOf(maze.getEnigmas().size()));
                Log.d("POSITION", "Enigme à enlever : " + String.valueOf(enigmaToRemove.x) + " " + String.valueOf(enigmaToRemove.y));
                maze.getEnigmas().remove(enigmaToRemove);
                mapLayout.updateEnigmasV(maze.getEnigmas());
                Log.d("POSITION", "List des enigmes: " + String.valueOf(maze.getEnigmas().size()));

            } else if (maze.getExplorerPosition().is(maze.getExit()) && maze.getEnigmas().isEmpty()) {
                // Sortie + toutes les enigmes résolues.
                try {
                    chatOut.sendMessage("WIN");
                    // Partie terminée = écran de victoire !
                    Intent intent = new Intent(GameMapActivity.this, VictoryActivity.class);
                    startActivity(intent);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }

            mapLayout.updateExplorerV(maze.getExplorerPosition());

            sendWallsMessage();
        }
    }
    @Override
    public void onDialogTouch(DialogTouchEvent event) {

        Toast.makeText(GameMapActivity.this, "Mur touché", Toast.LENGTH_SHORT).show();

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

        try {
            // Pause de 100 ms.
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Picots à afficher et lever.
        leftTouches = new boolean[]{false, false, false, false, false, false, false, false};
        rightTouches = new boolean[]{false, false, false, false, false, false, false, false};

        // Affichage.
        picots = PinsDisplayer.setAndDisplay(leftTouches, rightTouches);

        data = new byte[4];
        data[0] = 0x1b;
        data[1] = 0x01;
        data[2] = regularBoolToByte(rectifyTouches(leftTouches));
        data[3] = regularBoolToByte(rectifyTouches(rightTouches));

        sendData = new Intent();

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

    /**
     * Envoie la position des murs.
     */
    private void sendWallsMessage() {
        // Normal.
        String wallsMessage = "{";
        wallsMessage += "\"x\" : " + maze.getExplorerPosition().x +",";
        wallsMessage += "\"y\" : " + maze.getExplorerPosition().y +",";
        wallsMessage += "\"top\" : " + new Direction2D(maze, "FRONT").apply().isTouchableByExplorer()+",";
        wallsMessage += "\"bottom\" : " + new Direction2D(maze, "REAR").apply().isTouchableByExplorer()+",";
        wallsMessage += "\"right\" : " + new Direction2D(maze, "RIGHT").apply().isTouchableByExplorer()+",";
        wallsMessage += "\"left\" : " + new Direction2D(maze, "LEFT").apply().isTouchableByExplorer()+"}";


        try {
            // On lui renvoie les murs autour de lui.
            chatOut.sendMessage(wallsMessage);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    // Récupération du resultat de la seconde activité.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Vérification de l'intent grace a son identifiant
        if (requestCode == 10) {
            Toast.makeText(GameMapActivity.this, "Stèle complétée !!", Toast.LENGTH_SHORT).show();
        }
    }

    // Thread d'allumage du tacticon.
    private class MessageThread extends Thread {
        @Override
        public void run() {
            while (!enigmeAck) {
                try {
                    Log.d("threasd", "dans le thread");
                    Log.d("threasd", String.valueOf(enigmeAck));
                    chatOut.sendMessage("ENIGME");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
            Log.d("threasd", String.valueOf(enigmeAck));
            enigmeAck = false;
        }
    }

    /**
     * Fermeture de l'activité.
     */
    /*@Override
    protected void onDestroy() {
        // On ferme tous les canaux.
        if (chatOut != null) {
            try {
                // On envoie un message de fin au coéquipier.
                chatOut.sendMessage(END_DIALOG_MESSAGE);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
        if (chatOut != null) {
            chatOut.close();
        }
        if (chatIn != null) {
            chatIn.close();
        }
        if (chatManager != null) {
            chatManager.removeChatListener(this);
        }

        super.onDestroy();
    }*/
}
