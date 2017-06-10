package nf28.touchmaze.activity;

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

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import nf28.touchmaze.R;
import nf28.touchmaze.layout.EnigmaSurfaceLayout;
import nf28.touchmaze.util.enigmaActivity.enigma.EnigmaManager;
import nf28.touchmaze.util.enigmaActivity.enigma.ExplorerEnigma;
import nf28.touchmaze.util.enigmaActivity.enigma.GuideEnigma;
import nf28.touchmaze.util.enigmaActivity.tacticon.ByteAdaptable;
import nf28.touchmaze.util.enigmaActivity.tacticon.Circle;
import nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon;

import static android.R.color.black;
import static nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon.Status.REPLECEABLE;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class EnigmaExploActivity extends ChatActivity {

    // Date de demarrage du tacticon
    Date startTime = new Date();
    // Date d'arret du tacticon
    Date endTime = new Date();

    boolean threadIsRunning = false;

    // Intent pour le module tactos
    Intent sendData = new Intent();

    // Compteur du nombre de touch effectué
    int touchTap = 0;

    private boolean moveAction = false;

    private ExplorerEnigma enigma;
    private GuideEnigma gEnigma;
    private HashMap<ExplorerEnigma, GuideEnigma> enigmasMap;

    // Controls
    private LinearLayout main_layout;

    private EnigmaSurfaceLayout ex_tab_0;
    private EnigmaSurfaceLayout ex_tab_1;
    private EnigmaSurfaceLayout ex_tab_2;
    private EnigmaSurfaceLayout ex_tab_3;
    private EnigmaSurfaceLayout ex_tab_4;
    private EnigmaSurfaceLayout ex_tab_5;

    private EnigmaSurfaceLayout comp_tab_0;
    private EnigmaSurfaceLayout comp_tab_1;
    private EnigmaSurfaceLayout comp_tab_2;

    // Liste des surfaceLayout
    private ArrayList<EnigmaSurfaceLayout> surfaceLayouts;

    // Gestions des tacticon/surfaceLayout touchés
    private Tacticon touchedTacticon;
    private Tacticon selectedTacticon;
    private int runningAreaIndex;
    private int selectedAreaIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enigma_explo);

        // Controls
        main_layout = (LinearLayout) findViewById(R.id.main_layout);

        ex_tab_0 = (EnigmaSurfaceLayout) findViewById(R.id.ex_tab_0);
        ex_tab_1 = (EnigmaSurfaceLayout) findViewById(R.id.ex_tab_1);
        ex_tab_2 = (EnigmaSurfaceLayout) findViewById(R.id.ex_tab_2);
        ex_tab_3 = (EnigmaSurfaceLayout) findViewById(R.id.ex_tab_3);
        ex_tab_4 = (EnigmaSurfaceLayout) findViewById(R.id.ex_tab_4);
        ex_tab_5 = (EnigmaSurfaceLayout) findViewById(R.id.ex_tab_5);

        comp_tab_0 = (EnigmaSurfaceLayout) findViewById(R.id.comp_tab_0);
        comp_tab_1 = (EnigmaSurfaceLayout) findViewById(R.id.comp_tab_1);
        comp_tab_2 = (EnigmaSurfaceLayout) findViewById(R.id.comp_tab_2);

        // Ajout des surfaceLayout dans la liste
        surfaceLayouts = new ArrayList<EnigmaSurfaceLayout>();
        surfaceLayouts.add(ex_tab_0);
        surfaceLayouts.add(ex_tab_1);
        surfaceLayouts.add(ex_tab_2);
        surfaceLayouts.add(ex_tab_3);
        surfaceLayouts.add(ex_tab_4);
        surfaceLayouts.add(ex_tab_5);
        surfaceLayouts.add(comp_tab_0);
        surfaceLayouts.add(comp_tab_1);
        surfaceLayouts.add(comp_tab_2);

        // Initialisation des numéros
        ex_tab_0.setNum(0);
        ex_tab_1.setNum(1);
        ex_tab_2.setNum(2);
        ex_tab_3.setNum(3);
        ex_tab_4.setNum(4);
        ex_tab_5.setNum(5);

        comp_tab_0.setNum(10);
        comp_tab_1.setNum(11);
        comp_tab_2.setNum(12);

        // Récupération de l'énigme
        enigmasMap = EnigmaManager.getInstance().createNewEnigma();

        for (HashMap.Entry<ExplorerEnigma, GuideEnigma> entry : enigmasMap.entrySet()) {
            enigma = entry.getKey();
            gEnigma = entry.getValue();
        }

        // Envoie de l'enigme au guide en JSON
        String guideMessage = new Gson().toJson(gEnigma);
        try {
            chatOut.sendMessage(guideMessage);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        // Debug init (couleurs)
        debuginitSurfaceLayout(ex_tab_0);
        debuginitSurfaceLayout(ex_tab_1);
        debuginitSurfaceLayout(ex_tab_2);
        debuginitSurfaceLayout(ex_tab_3);
        debuginitSurfaceLayout(ex_tab_4);
        debuginitSurfaceLayout(ex_tab_5);

        // Listener sur chaque surface layout
        // Gestion des tap et des double tap en fonction des actions en cours / à effectuer
        for (final EnigmaSurfaceLayout sf : surfaceLayouts) {

            sf.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    // Si aucun thread de tacticon est en cours, on gère l'event
                    Log.d("Touch", String.valueOf(threadIsRunning));
                    if (!threadIsRunning) {

                        // Mis à jour du nombre de tap
                        touchTap++;
                        Log.d("Touch", "Debut" + String.valueOf(touchTap));
                        Log.d("Touch", "Debut" + String.valueOf(sf.getNum()));

                        // Récupération du tacticon touché
                        if (sf.getNum() < 10) {
                            // Tacticon de l'explorerTab
                            touchedTacticon = enigma.getExplorerTab()[sf.getNum()];
                        } else {
                            // Tacticon du complementaryTab
                            touchedTacticon = enigma.getExplorerComplementaryTab()[sf.getNum() - 10];
                        }

                        Log.d("Touch", String.valueOf(touchedTacticon.getStatus()));

                        // Handler pour permettre le double tap
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                // Lecture d'un tacticon
                                if (touchTap == 1 && !moveAction) {
                                    Log.d("Touch", "ST");

                                    // Si le tacticon est actif
                                    if (!touchedTacticon.getStatus().equals(REPLECEABLE)) {
                                        // Lancement du tacticon
                                        Log.d("Touch", "Lance le tacticon");

                                        // Creation du thread
                                        TacticonThread tThread = new TacticonThread((ByteAdaptable) touchedTacticon);
                                        tThread.start();

                                        startTime.setTime(System.currentTimeMillis());
                                        // Thread en cours sur 5 secondes
                                        endTime.setTime(System.currentTimeMillis() + 5000);

                                        threadIsRunning = true;
                                        runningAreaIndex = sf.getNum();

                                        // Feedback visuel
                                        sf.setBackgroundColor(getResources().getColor(R.color.green));

                                    } else {
                                        Log.d("Touch", "Tacticon off");
                                    }
                                }

                                // Déplacement d'un tacticon
                                else if (touchTap == 1 && moveAction) {
                                    Log.d("Touch", "ST");

                                    // Si le tacticon est remplacable ou added
                                    if (touchedTacticon.getStatus().equals(REPLECEABLE) || touchedTacticon.getStatus().equals(Tacticon.Status.ADDED)) {
                                        // Déplacement du tacticon (le tacticon passe de repleceable à added)
                                        enigma.proposeTacticon(selectedTacticon, sf.getNum());

                                        // Mise à jour des couleurs
                                        refreshColors(sf);

                                        for (final EnigmaSurfaceLayout colorsf : surfaceLayouts) {
                                            if (colorsf.getNum() == selectedAreaIndex) {
                                                refreshColors(colorsf);
                                            }
                                        }

                                        // Sortie de l'etat moveAction
                                        moveAction = false;

                                        // Vérification de la fin de l'enigme
                                        if (enigma.isFull()) {
                                            if (enigma.isCompleted()) {
                                                // Envoie la fin de l'activité
                                                String guideMessage = "STOP";
                                                try {
                                                    chatOut.sendMessage(guideMessage);
                                                } catch (SmackException.NotConnectedException e) {
                                                    e.printStackTrace();
                                                }

                                                Toast.makeText(EnigmaExploActivity.this, "FINI", Toast.LENGTH_SHORT).show();

                                                // setResult(RESULT_OK, new Intent());
                                                finish();
                                            } else {
                                                Toast.makeText(EnigmaExploActivity.this, "Stèle incorrecte", Toast.LENGTH_SHORT).show();

                                                // Animation à l'écran
                                                Animation shakeAnim = AnimationUtils.loadAnimation(EnigmaExploActivity.this, R.anim.shake);
                                                main_layout.startAnimation(shakeAnim);
                                            }

                                        }

                                        Log.d("Touch", "Déplacement");
                                    } else {
                                        Log.d("Touch", "Tacticon non remplacable");

                                        // Animation à l'écran
                                        Animation shakeAnim = AnimationUtils.loadAnimation(EnigmaExploActivity.this, R.anim.shake);
                                        main_layout.startAnimation(shakeAnim);
                                    }

                                }

                                // Activation d'un tacticon à déplacer
                                else if (touchTap == 2 && !moveAction) {
                                    Log.d("Touch", "DT");

                                    // Si le tacticon est complementaire
                                    if (touchedTacticon.getStatus().equals(Tacticon.Status.COMPLEMENTARY)) {

                                        // Stocke le tacticon sélectionné et l'index
                                        selectedTacticon = touchedTacticon;
                                        selectedAreaIndex = sf.getNum();

                                        // Feedback visuel
                                        sf.setBackgroundColor(getResources().getColor(R.color.pink));

                                        // Passage dans l'etat moveAction
                                        moveAction = true;

                                        Log.d("Touch", "Selection du taction");
                                    } else {
                                        Log.d("Touch", "Taction non complementaire");

                                        // Animation à l'écran
                                        Animation shakeAnim = AnimationUtils.loadAnimation(EnigmaExploActivity.this, R.anim.shake);
                                        main_layout.startAnimation(shakeAnim);
                                    }

                                    // Cas d'un double tap impossible
                                } else if (touchTap == 2 && moveAction) {
                                    Log.d("Touch", "Tacticon selectionné : double tap impossible");

                                    // Animation à l'écran
                                    Animation shakeAnim = AnimationUtils.loadAnimation(EnigmaExploActivity.this, R.anim.shake);
                                    main_layout.startAnimation(shakeAnim);
                                }

                                // Remise à 0 du compteur de tap
                                touchTap = 0;
                                Log.d("Touch", "Fin" + String.valueOf(touchTap));

                                // Temps accordé pour réaliser le double tap : 0.5 sec
                            }
                        }, 500);

                    } else {
                        Log.d("Touch", "Thread en cours");
                    }
                    return false;
                }
            });
        }
    }

    /**
     * Met à jour la couleur du surface layout en fonction du status de son tacticon
     *
     * @param p_esl
     */
    public void refreshColors(EnigmaSurfaceLayout p_esl) {
        Tacticon tacticon;

        if (p_esl.getNum() < 10)
            tacticon = enigma.getExplorerTab()[p_esl.getNum()];
        else
            tacticon = enigma.getExplorerComplementaryTab()[p_esl.getNum() - 10];

        if (tacticon.getStatus().equals(Tacticon.Status.COMPLEMENTARY))
            p_esl.setBackgroundColor(getResources().getColor(R.color.orange));
        else if (tacticon.getStatus().equals(Tacticon.Status.ADDED))
            p_esl.setBackgroundColor(getResources().getColor(R.color.darkorange));
        else if (tacticon.getStatus().equals(Tacticon.Status.FIXED))
            p_esl.setBackgroundColor(getResources().getColor(R.color.lightblue));

    }

    /**
     * Met les surface layout off en noir pour le debug
     *
     * @param p_surfaceLayout
     */
    public void debuginitSurfaceLayout(EnigmaSurfaceLayout p_surfaceLayout) {
        for (int i = 0; i < enigma.getExplorerTab().length; ++i) {
            if (p_surfaceLayout.getNum() == i) {
                if (enigma.getExplorerTab()[i].getStatus().equals(Tacticon.Status.REPLECEABLE))
                    p_surfaceLayout.setBackgroundColor(getResources().getColor(black));
            }
        }
    }

    // Méthode d'envoi des données à l'appli Bluetooth.
    public void traitementData(ByteAdaptable p_tacticon) {
        // Création du tableau de bytes à envoyer.
        byte[] data;

        // On le remplit.
        data = p_tacticon.SetToByte();

        sendData.putExtra("BStream", data);
        sendData.setAction("com.example.labocred.bluetooth.StreamBluetooth");
        // On l'envoie à l'appli bluetooth.
        sendBroadcast(sendData);
    }

    // Thread d'allumage du tacticon.
    private class TacticonThread extends Thread {
        private ByteAdaptable tacticon;

        public TacticonThread(ByteAdaptable p_tacticon) {
            tacticon = p_tacticon;
        }

        @Override
        public void run() {
            while (startTime.compareTo(endTime) < 0) {

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
                    for (final EnigmaSurfaceLayout colorsf : surfaceLayouts) {
                        if (colorsf.getNum() == runningAreaIndex) {
                            refreshColors(colorsf);
                        }
                    }
                }
            });
        }
    }

}
