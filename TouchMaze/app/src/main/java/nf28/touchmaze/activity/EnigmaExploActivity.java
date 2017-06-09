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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import nf28.touchmaze.R;
import nf28.touchmaze.layout.EnigmaSurfaceLayout;
import nf28.touchmaze.util.enigmaActivity.enigma.EnigmaManager;
import nf28.touchmaze.util.enigmaActivity.enigma.ExplorerEnigma;
import nf28.touchmaze.util.enigmaActivity.enigma.GuideEnigma;
import nf28.touchmaze.util.enigmaActivity.tacticon.Circle;
import nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon;

import static android.R.color.black;
import static nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon.Status.REPLECEABLE;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class EnigmaExploActivity extends AppCompatActivity{

    Date startTime = new Date();
    Date endTime = new Date();
    Intent sendData = new Intent();

    boolean threadEnCours = false;

    public enum Tacticons {
        SPIRALE,
        CIRCLE,
        WAVE,
        SPLIT,
        SNOW;
    }

    private boolean moveAction = false;

    private ExplorerEnigma enigma;
    private GuideEnigma gEnigma;
    private HashMap<ExplorerEnigma, GuideEnigma> enigmasMap;

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

    private Tacticon touchedTacticon;
    private Tacticon selectedTacticon;
    private int runningAreaIndex = -1;
    private int selectedAreaIndex;

    private ArrayList<EnigmaSurfaceLayout> surfaceLayouts;


    int touchTap = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enigma_explo);

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

        ex_tab_0.setNum(0);
        ex_tab_1.setNum(1);
        ex_tab_2.setNum(2);
        ex_tab_3.setNum(3);
        ex_tab_4.setNum(4);
        ex_tab_5.setNum(5);

        comp_tab_0.setNum(10);
        comp_tab_1.setNum(11);
        comp_tab_2.setNum(12);

        enigmasMap = EnigmaManager.getInstance().createNewEnigma();

        for (HashMap.Entry<ExplorerEnigma, GuideEnigma> entry : enigmasMap.entrySet()) {
            enigma = entry.getKey();
            gEnigma = entry.getValue();
        }

        debuginitSurfaceLayout(ex_tab_0);
        debuginitSurfaceLayout(ex_tab_1);
        debuginitSurfaceLayout(ex_tab_2);
        debuginitSurfaceLayout(ex_tab_3);
        debuginitSurfaceLayout(ex_tab_4);
        debuginitSurfaceLayout(ex_tab_5);

        //envoie de l'enigme en JSON
        new Gson().toJson(gEnigma);
        // ENVOI

        //new Gson().fromJson(jsonStr, MyClass.class);

        for (final EnigmaSurfaceLayout sf : surfaceLayouts) {

            sf.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    Log.d("Touch", String.valueOf(threadEnCours));
                    if (!threadEnCours) {

                        if (runningAreaIndex!=-1) {
                            // Changement de la couleur
                            for (final EnigmaSurfaceLayout colorsf : surfaceLayouts) {
                                Log.d("Touch", "dans le for");
                                if (colorsf.getNum() == runningAreaIndex) {
                                    Log.d("Touch", "dans le if");
                                    refreshColors(colorsf);
                                    Log.d("Touch", "apes refresh color");
                                }
                            }
                            runningAreaIndex = -1;
                        }

                        Log.d("Touch", "fin du thread");

                        touchTap++;
                        Log.d("Touch", "Debut" + String.valueOf(touchTap));
                        Log.d("Touch", "Debut" + String.valueOf(sf.getNum()));

                        if (sf.getNum() < 10)
                            touchedTacticon = enigma.getExplorerTab()[sf.getNum()];
                        else
                            touchedTacticon = enigma.getExplorerComplementaryTab()[sf.getNum() - 10];

                        Log.d("Touch", String.valueOf(touchedTacticon.getStatus()));

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                // Lecture d'un tacticon
                                if (touchTap == 1 && !moveAction) {
                                    Toast.makeText(EnigmaExploActivity.this, "single touch lecture", Toast.LENGTH_SHORT).show();
                                    Log.d("Touch", "ST");

                                    // Si le tacticon est actif
                                    if (!touchedTacticon.getStatus().equals(REPLECEABLE)) {
                                        // LANCER LE TACTICON
                                        Log.d("Touch", "Lance le tacticon");

                                        CircleThread circleThread = new CircleThread();
                                        circleThread.start();
                                        startTime.setTime(System.currentTimeMillis());
                                        endTime.setTime(System.currentTimeMillis() + 5000);
                                        threadEnCours = true;
                                        runningAreaIndex = sf.getNum();
                                        sf.setBackgroundColor(getResources().getColor(R.color.green));

                                    } else {
                                        Log.d("Touch", "Tacticon off");
                                    }
                                }

                                // Déplacement d'un tacticon
                                else if (touchTap == 1 && moveAction) {
                                    Toast.makeText(EnigmaExploActivity.this, "single touch movement", Toast.LENGTH_SHORT).show();
                                    Log.d("Touch", "ST");

                                    // Si le tacticon est remplacable ou added
                                    if (touchedTacticon.getStatus().equals(REPLECEABLE) || touchedTacticon.getStatus().equals(Tacticon.Status.ADDED)) {
                                        // Déplacement du tacticon
                                        enigma.proposeTacticon(selectedTacticon, sf.getNum());

                                        refreshColors(sf);

                                        // Changement de la couleur
                                        for (final EnigmaSurfaceLayout colorsf : surfaceLayouts) {
                                            if (colorsf.getNum() == selectedAreaIndex) {
                                                refreshColors(colorsf);
                                            }
                                        }

                                        moveAction = false;

                                        if (enigma.isCompleted()) {
                                            //ENVOIE LA FIN DE L'ACTIVITE à L'AUTRE
                                        }

                                        Log.d("Touch", "Déplacement");
                                    } else {
                                        Log.d("Touch", "Tacticon non remplacable");
                                        Animation shakeAnim = AnimationUtils.loadAnimation(EnigmaExploActivity.this, R.anim.shake);
                                        main_layout.startAnimation(shakeAnim);
                                    }

                                }

                                // Activation d'un tacticon à déplacer
                                else if (touchTap == 2 && !moveAction) {
                                    Toast.makeText(EnigmaExploActivity.this, "double touch", Toast.LENGTH_SHORT).show();
                                    Log.d("Touch", "DT");

                                    // Si le tacticon est complementaire
                                    if (touchedTacticon.getStatus().equals(Tacticon.Status.COMPLEMENTARY)) {
                                        selectedTacticon = touchedTacticon;
                                        selectedAreaIndex = sf.getNum();

                                        sf.setBackgroundColor(getResources().getColor(R.color.pink));
                                        moveAction = true;

                                        Log.d("Touch", "Selection du taction");
                                    } else {
                                        Log.d("Touch", "Taction non complementaire");
                                        Animation shakeAnim = AnimationUtils.loadAnimation(EnigmaExploActivity.this, R.anim.shake);
                                        main_layout.startAnimation(shakeAnim);
                                    }
                                } else if (touchTap == 2 && moveAction) {
                                    Log.d("Touch", "Tacticon selectionné : double tap impossible");
                                    Animation shakeAnim = AnimationUtils.loadAnimation(EnigmaExploActivity.this, R.anim.shake);
                                    main_layout.startAnimation(shakeAnim);
                                }

                                touchTap = 0;
                                Log.d("Touch", "Fin" + String.valueOf(touchTap));
                            }
                        }, 500);

                    }
                    else
                        Log.d("Touch", "Thread en cours");
                    return false;
                }
            });
        }
    }

    public void refreshColors(EnigmaSurfaceLayout p_esl){

        Log.d("Touch", "au debut du refresh color");

        Tacticon tacticon;

        Log.d("Touch", "1");

        if (p_esl.getNum() < 10)
            tacticon = enigma.getExplorerTab()[p_esl.getNum()];
        else
            tacticon = enigma.getExplorerComplementaryTab()[p_esl.getNum()-10];

        Log.d("Touch", "2");

        if (tacticon.getStatus().equals(Tacticon.Status.COMPLEMENTARY))
            p_esl.setBackgroundColor(getResources().getColor(R.color.orange));
        else if (tacticon.getStatus().equals(Tacticon.Status.ADDED))
            p_esl.setBackgroundColor(getResources().getColor(R.color.darkorange));
        else if (tacticon.getStatus().equals(Tacticon.Status.FIXED))
            p_esl.setBackgroundColor(getResources().getColor(R.color.lightblue));

        Log.d("Touch", "colors");
    }

    public void debuginitSurfaceLayout(EnigmaSurfaceLayout p_surfaceLayout){
        for (int i = 0; i < enigma.getExplorerTab().length; ++i) {
            if (p_surfaceLayout.getNum() == i) {
                if (enigma.getExplorerTab()[i].getStatus().equals(Tacticon.Status.REPLECEABLE))
                   p_surfaceLayout.setBackgroundColor(getResources().getColor(black));
            }
        }
    }

    // Thread d'allumage du CIRCLE.
    private class CircleThread extends Thread{
        @Override
        public void run() {
            while (startTime.compareTo(endTime)<0) {
                // Tant que le flag est levé, on lance la séquence.
                traitementData(Tacticons.CIRCLE);
                startTime.setTime(System.currentTimeMillis());
                Log.d("Touch", "dans le thread");
            }
            threadEnCours = false;
            Log.d("Touch", "apres false");

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

            Log.d("Touch", "test");

        }
    }

    // Méthode d'envoi des données à l'appli Bluetooth.
    public void traitementData(Tacticons t) {
        // Création du tableau de bytes à envoyer.
        byte[] data;
        data = new byte[4];
        // On le remplit.
        switch (t) {
            case CIRCLE:
                data = Circle.SetToByte();
                break;
        }

        sendData.putExtra("BStream", data);
        sendData.setAction("com.example.labocred.bluetooth.StreamBluetooth");
        // On l'envoie à l'appli bluetooth.
        sendBroadcast(sendData);
    }

}
