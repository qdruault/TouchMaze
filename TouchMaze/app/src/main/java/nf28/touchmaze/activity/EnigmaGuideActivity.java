package nf28.touchmaze.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import nf28.touchmaze.R;
import nf28.touchmaze.layout.EnigmaSurfaceLayout;
import nf28.touchmaze.util.PinsDisplayer;
import nf28.touchmaze.util.enigmaActivity.enigma.EnigmaManager;
import nf28.touchmaze.util.enigmaActivity.enigma.ExplorerEnigma;
import nf28.touchmaze.util.enigmaActivity.enigma.GuideEnigma;
import nf28.touchmaze.util.enigmaActivity.tacticon.Alternation;
import nf28.touchmaze.util.enigmaActivity.tacticon.ByteAdaptable;
import nf28.touchmaze.util.enigmaActivity.tacticon.Circle;
import nf28.touchmaze.util.enigmaActivity.tacticon.Cube;
import nf28.touchmaze.util.enigmaActivity.tacticon.Pointbypoint;
import nf28.touchmaze.util.enigmaActivity.tacticon.Rotation;
import nf28.touchmaze.util.enigmaActivity.tacticon.Shape;
import nf28.touchmaze.util.enigmaActivity.tacticon.Snow;
import nf28.touchmaze.util.enigmaActivity.tacticon.Split;
import nf28.touchmaze.util.enigmaActivity.tacticon.Stick;
import nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon;
import nf28.touchmaze.util.enigmaActivity.tacticon.Wave;

import static android.R.color.black;
import static nf28.touchmaze.activity.ConnectionActivity.TESTMODE;
import static nf28.touchmaze.activity.GameMazeActivity.rectifyTouches;
import static nf28.touchmaze.activity.GameMazeActivity.regularBoolToByte;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class EnigmaGuideActivity extends ChatActivity {

    String testmessage;

    // Date de demarrage du tacticon
    Date startTime = new Date();
    // Date d'arret du tacticon
    Date endTime = new Date();

    boolean threadIsRunning = false;

    // Intent pour le module tactos
    Intent sendData = new Intent();

    private GuideEnigma enigma;
    private HashMap<ExplorerEnigma, GuideEnigma> enigmasMap;

    // Controls
    private LinearLayout main_layout;

    private EnigmaSurfaceLayout tab_0;
    private EnigmaSurfaceLayout tab_1;
    private EnigmaSurfaceLayout tab_2;
    private EnigmaSurfaceLayout tab_3;
    private EnigmaSurfaceLayout tab_4;
    private EnigmaSurfaceLayout tab_5;

    private EnigmaSurfaceLayout tab_6;
    private EnigmaSurfaceLayout tab_7;
    private EnigmaSurfaceLayout tab_8;
    private EnigmaSurfaceLayout tab_9;
    private EnigmaSurfaceLayout tab_10;
    private EnigmaSurfaceLayout tab_11;

    private EnigmaSurfaceLayout tab_12;
    private EnigmaSurfaceLayout tab_13;
    private EnigmaSurfaceLayout tab_14;
    private EnigmaSurfaceLayout tab_15;
    private EnigmaSurfaceLayout tab_16;
    private EnigmaSurfaceLayout tab_17;

    // Liste des surface layout
    private ArrayList<EnigmaSurfaceLayout> tableau_up;
    private ArrayList<EnigmaSurfaceLayout> tableau_middle;
    private ArrayList<EnigmaSurfaceLayout> tableau_bottom;

    private ArrayList<ArrayList<EnigmaSurfaceLayout>> surfaceLayouts;

    private Tacticon touchedTacticon;
    private int runningAreaIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enigma_guide);

        // Controls
        main_layout = (LinearLayout) findViewById(R.id.main_layout);

        tab_0 = (EnigmaSurfaceLayout) findViewById(R.id.tab_0);
        tab_1 = (EnigmaSurfaceLayout) findViewById(R.id.tab_1);
        tab_2 = (EnigmaSurfaceLayout) findViewById(R.id.tab_2);
        tab_3 = (EnigmaSurfaceLayout) findViewById(R.id.tab_3);
        tab_4 = (EnigmaSurfaceLayout) findViewById(R.id.tab_4);
        tab_5 = (EnigmaSurfaceLayout) findViewById(R.id.tab_5);

        tab_6 = (EnigmaSurfaceLayout) findViewById(R.id.tab_6);
        tab_7 = (EnigmaSurfaceLayout) findViewById(R.id.tab_7);
        tab_8 = (EnigmaSurfaceLayout) findViewById(R.id.tab_8);
        tab_9 = (EnigmaSurfaceLayout) findViewById(R.id.tab_9);
        tab_10 = (EnigmaSurfaceLayout) findViewById(R.id.tab_10);
        tab_11 = (EnigmaSurfaceLayout) findViewById(R.id.tab_11);

        tab_12 = (EnigmaSurfaceLayout) findViewById(R.id.tab_12);
        tab_13 = (EnigmaSurfaceLayout) findViewById(R.id.tab_13);
        tab_14 = (EnigmaSurfaceLayout) findViewById(R.id.tab_14);
        tab_15 = (EnigmaSurfaceLayout) findViewById(R.id.tab_15);
        tab_16 = (EnigmaSurfaceLayout) findViewById(R.id.tab_16);
        tab_17 = (EnigmaSurfaceLayout) findViewById(R.id.tab_17);

        // Ajout des surfaceLayout dans la liste
        tableau_up = new ArrayList<EnigmaSurfaceLayout>();
        tableau_up.add(tab_0);
        tableau_up.add(tab_1);
        tableau_up.add(tab_2);
        tableau_up.add(tab_3);
        tableau_up.add(tab_4);
        tableau_up.add(tab_5);

        tableau_middle = new ArrayList<EnigmaSurfaceLayout>();
        tableau_middle.add(tab_6);
        tableau_middle.add(tab_7);
        tableau_middle.add(tab_8);
        tableau_middle.add(tab_9);
        tableau_middle.add(tab_10);
        tableau_middle.add(tab_11);

        tableau_bottom = new ArrayList<EnigmaSurfaceLayout>();
        tableau_bottom.add(tab_12);
        tableau_bottom.add(tab_13);
        tableau_bottom.add(tab_14);
        tableau_bottom.add(tab_15);
        tableau_bottom.add(tab_16);
        tableau_bottom.add(tab_17);

        surfaceLayouts = new ArrayList<ArrayList<EnigmaSurfaceLayout>>();
        surfaceLayouts.add(tableau_up);
        surfaceLayouts.add(tableau_middle);
        surfaceLayouts.add(tableau_bottom);

        // Initialisation des numéros et randomisation des positions
        randomizeTabs();

        // Debug
        /*enigmasMap = EnigmaManager.getInstance().createNewEnigma();

        for (HashMap.Entry<ExplorerEnigma, GuideEnigma> entry : enigmasMap.entrySet()) {
            enigma = entry.getValue();
        }
        */
        debuginitSurfaceLayout();


        for (ArrayList<EnigmaSurfaceLayout> tab : surfaceLayouts){
            for (final EnigmaSurfaceLayout sf : tab){
                sf.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        // Si aucun thread de tacticon est en cours, on gère l'event
                        Log.d("Touch", String.valueOf(threadIsRunning));
                        if (!threadIsRunning) {

                            Log.d("Touch", "Num du surfaceLayout" + String.valueOf(sf.getNum()));

                            if (testmessage != null)
                                Log.d("Test", testmessage);

                            // Si l'enigme à été set
                            if (enigma != null) {

                                // Premier tableau
                                if (sf.getNum() < 10) {
                                    touchedTacticon = enigma.getchosenGuideTab()[sf.getNum()];
                                    // Second tableau
                                }else if (sf.getNum() < 100) {
                                    touchedTacticon = enigma.getSecondGuideTab()[sf.getNum() - 10];
                                    // Troisieme tableau
                                }else {
                                    touchedTacticon = enigma.getThirdGuideTab()[sf.getNum() - 100];
                                }

                                Log.d("Touch", "Lance le tacticon");

                                EnigmaGuideActivity.TacticonThread tThread = null;

                                Tacticon.Type t = touchedTacticon.getType();

                                switch (t){
                                    case CIRCLE:
                                        tThread = new EnigmaGuideActivity.TacticonThread((ByteAdaptable) new Circle());
                                        break;
                                    case ALTERNATION:
                                        tThread = new EnigmaGuideActivity.TacticonThread((ByteAdaptable) new Alternation());
                                        break;
                                    case CUBE:
                                        tThread = new EnigmaGuideActivity.TacticonThread((ByteAdaptable) new Cube());
                                        break;
                                    case SPLIT:
                                        tThread = new EnigmaGuideActivity.TacticonThread((ByteAdaptable) new Split());
                                        break;
                                    case STICK:
                                        tThread = new EnigmaGuideActivity.TacticonThread((ByteAdaptable) new Stick());
                                        break;
                                    case SNOW:
                                        tThread = new EnigmaGuideActivity.TacticonThread((ByteAdaptable) new Snow());
                                        break;
                                    case WAVE:
                                        tThread = new EnigmaGuideActivity.TacticonThread((ByteAdaptable) new Wave());
                                        break;
                                    case POINT:
                                        tThread = new EnigmaGuideActivity.TacticonThread((ByteAdaptable) new Pointbypoint());
                                        break;
                                    case SHAPE:
                                        tThread = new EnigmaGuideActivity.TacticonThread((ByteAdaptable) new Shape());
                                        break;
                                    case ROTATION:
                                        tThread = new EnigmaGuideActivity.TacticonThread((ByteAdaptable) new Rotation());
                                        break;
                                }

                                // Lancement du tacticon
                                tThread.start();

                                startTime.setTime(System.currentTimeMillis());
                                // Thread en cours sur 5 secondes
                                endTime.setTime(System.currentTimeMillis() + 5000);

                                threadIsRunning = true;
                                runningAreaIndex = sf.getNum();

                                // Feedback visuel
                                sf.setBackgroundColor(getResources().getColor(R.color.green));
                            }
                        }else{
                            Log.d("Touch", "Thread en cours");
                        }

                        return false;
                    }
                });
            }
        }
    }

    /**
     * Donne une permutation possible pour 3 éléments
     * @return
     */
    public ArrayList<Integer> getPermutation(){
        ArrayList<Integer> position = new ArrayList<Integer>();
        ArrayList<Integer> order = new ArrayList<Integer>();
        position.add(1);
        position.add(2);
        position.add(3);

        Random rand = new Random();
        // random entre 0 et position.size
        int index = rand.nextInt(position.size());

        order.add(position.get(index));
        position.remove(index);

        index = rand.nextInt(position.size());

        order.add(position.get(index));
        position.remove(index);

        index = rand.nextInt(position.size());

        order.add(position.get(index));

        return order;
    }

    /**
     * Place les 3 tableaux de manière aléatoire
     */
    public void randomizeTabs(){

        // 0 -> 5
        // chosenTab
        // 10 -> 15
        // secondTab
        // 100 -> 105
        // thirdTab

        ArrayList<Integer> order = getPermutation();

        Log.d("Touch", String.valueOf(order.get(0)));
        Log.d("Touch", String.valueOf(order.get(1)));
        Log.d("Touch", String.valueOf(order.get(2)));

        int i = 0;

        // Si le premier index est 1 alors le tableau du haut contient le premier tableau
        if (order.get(0)==1) {
            for (EnigmaSurfaceLayout sf : tableau_up) {
                sf.setNum(i);
                i++;
            }
        }
        // Si le premier index est 2 alors le tableau du haut contient le deuxieme tableau
        else if (order.get(0)==2) {
            i = 10;
            for (EnigmaSurfaceLayout sf : tableau_up) {
                sf.setNum(i);
                i++;
            }
        }
        // Si le premier index est 3 alors le tableau du haut contient le troisieme tableau
        else if (order.get(0)==3) {
            i = 100;
            for (EnigmaSurfaceLayout sf : tableau_up) {
                sf.setNum(i);
                i++;
            }
        }

        i = 0;

        // Si le deuxieme index est 1 alors le tableau du milieu contient le premier tableau
        if (order.get(1)==1) {
            for (EnigmaSurfaceLayout sf : tableau_middle) {
                sf.setNum(i);
                i++;
            }
        }
        // Si le deuxieme index est 2 alors le tableau du milieu contient le deuxieme tableau
        else if (order.get(1)==2) {
            i = 10;
            for (EnigmaSurfaceLayout sf : tableau_middle) {
                sf.setNum(i);
                i++;
            }
        }
        // Si le deuxieme index est 3 alors le tableau du milieu contient le troisieme tableau
        else if (order.get(1)==3) {
            i = 100;
            for (EnigmaSurfaceLayout sf : tableau_middle) {
                sf.setNum(i);
                i++;
            }
        }

        i = 0;

        // Si le troisieme index est 1 alors le tableau du bas contient le premier tableau
        if (order.get(2)==1) {
            for (EnigmaSurfaceLayout sf : tableau_bottom) {
                sf.setNum(i);
                i++;
            }
        }
        // Si le troisieme index est 2 alors le tableau du bas contient le deuxieme tableau
        else if (order.get(2)==2) {
            i = 10;
            for (EnigmaSurfaceLayout sf : tableau_bottom) {
                sf.setNum(i);
                i++;
            }
        }
        // Si le troisieme index est 3 alors le tableau du bas contient le troisieme tableau
        else if (order.get(2)==3) {
            i = 100;
            for (EnigmaSurfaceLayout sf : tableau_bottom) {
                sf.setNum(i);
                i++;
            }
        }
    }

    /**
     * Met les surface layout du chosenTab en noir pour le debug
     */
    public void debuginitSurfaceLayout(){
        for (ArrayList<EnigmaSurfaceLayout> tab : surfaceLayouts){
            for (final EnigmaSurfaceLayout sf : tab){
                if (sf.getNum() < 10)
                    sf.setBackgroundColor(getResources().getColor(black));
            }
        }
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
            }
            else if (messageBody.equals("STOP")) {
                Intent intent = getIntent();
                finish();
            }
            else {
                enigma = new Gson().fromJson(messageBody, GuideEnigma.class);
                //testmessage = messageBody;
            }
        }
    }

    // Thread d'allumage du tacticon.
    private class TacticonThread extends Thread{
        private ByteAdaptable tacticon;

        public TacticonThread(ByteAdaptable p_tacticon){
            tacticon = p_tacticon;
        }

        @Override
        public void run() {
            while (startTime.compareTo(endTime)<0) {
                // Tant que les 5 sec ne sont pas écoulées, on run le tacticon
                traitementData(tacticon);
                startTime.setTime(System.currentTimeMillis());
                Log.d("Touch", "dans le thread");
            }
            // Fin du thread
            threadIsRunning = false;

            // runOnUiThread pour pouvoir changer la couleur des élément du layout
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Changement de la couleur
                    for (ArrayList<EnigmaSurfaceLayout> tab : surfaceLayouts){
                        for (final EnigmaSurfaceLayout colorsf : tab){
                            if (colorsf.getNum() == runningAreaIndex) {
                                colorsf.setBackgroundColor(getResources().getColor(R.color.lightblue));
                            }
                        }
                    }
                }
            });

            // Picots à afficher et lever.
            boolean[] leftTouches = new boolean[]{false, false, false, false, false, false, false, false};
            boolean[] rightTouches = new boolean[]{false, false, false, false, false, false, false, false};

            // Affichage.
            String picots = PinsDisplayer.setAndDisplay(leftTouches, rightTouches);

            byte[] data = new byte[4];
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
    }

    // Méthode d'envoi des données à l'appli Bluetooth.
    public void traitementData(ByteAdaptable p_tacticon) {
        // Création du tableau de bytes à envoyer.
        byte[] data;

        // On le remplit.
        data = p_tacticon.SetToByte();

        if (!TESTMODE) {

            sendData.putExtra("BStream", data);
            sendData.setAction("com.example.labocred.bluetooth.StreamBluetooth");
            // On l'envoie à l'appli bluetooth.
            sendBroadcast(sendData);
        }
    }

    /**
     * Fermeture de l'activité.
     */
    @Override
    protected void onDestroy() {
        // On ferme tous les canaux.
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
    }

}
