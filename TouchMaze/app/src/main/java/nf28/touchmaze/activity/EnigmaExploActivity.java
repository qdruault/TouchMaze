package nf28.touchmaze.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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
import java.util.HashMap;

import nf28.touchmaze.R;
import nf28.touchmaze.layout.EnigmaSurfaceLayout;
import nf28.touchmaze.util.enigmaActivity.enigma.EnigmaManager;
import nf28.touchmaze.util.enigmaActivity.enigma.ExplorerEnigma;
import nf28.touchmaze.util.enigmaActivity.enigma.GuideEnigma;
import nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon;
import nf28.touchmaze.util.enigmaActivity.touch.EnigmaTactileViewHolder;
import nf28.touchmaze.util.enigmaActivity.touch.EnigmaTouchEvent;

import static android.R.color.background_dark;
import static android.R.color.black;
import static android.R.color.holo_blue_bright;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class EnigmaExploActivity extends AppCompatActivity implements EnigmaTactileViewHolder {

  //on touch qui marche si active
  //qui appelle le on touche de l'act avec le num dans l'event
  //qui lance le tacticon de la case correspondante pendant x sec

  //on double touch qui marche si mobile
  // double touch devrait mettre un flag a true dans l'activité
  //en gros il faut tjs que le surface layout renvoie un event à l'actvité
  //lors d'un event ontouche, si le flag est false, alros touch normal
  // si le flag est true, alors on fait déplacer le tacticon
  // si le tacticon est mobile, sinon rien et on laisse le flag
  // si on touche le meme tacticon ca annule le flag
  //du coup il faut le stocker quelque part.
  // le flag est dans l'activité pas dans le surface layout

  //et a chaque changement, verifier
  // si la verif est a true
  //on envoie la fin à l'autre gars
  // et on repasse à lactivité lab


  // cote guide, il faut seulement avoir le on touche classique
  // et etre pret a recevoir la fin d'enigme
  // si fin, on repasse à l'activité lab

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

        initSurfaceLayout(ex_tab_0);
        initSurfaceLayout(ex_tab_1);
        initSurfaceLayout(ex_tab_2);
        initSurfaceLayout(ex_tab_3);
        initSurfaceLayout(ex_tab_4);
        initSurfaceLayout(ex_tab_5);

        initSurfaceLayout(comp_tab_0);
        initSurfaceLayout(comp_tab_1);
        initSurfaceLayout(comp_tab_2);

        //envoie de l'enigme en JSON
        new Gson().toJson(gEnigma);
        // ENVOI

        //new Gson().fromJson(jsonStr, MyClass.class);

        for (final EnigmaSurfaceLayout sf : surfaceLayouts) {

            sf.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    touchTap++;
                    Log.d("Touch", "Debut" + String.valueOf(touchTap));
                    Log.d("Touch", "Debut" + String.valueOf(sf.getNum()));

                    if (sf.getNum() < 10)
                        touchedTacticon = enigma.getExplorerTab()[sf.getNum()];
                    else
                        touchedTacticon = enigma.getExplorerComplementaryTab()[sf.getNum()-10];

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
                                if (!touchedTacticon.getStatus().equals(Tacticon.Status.REPLECEABLE)) {
                                    // LANCER LE TACTICON
                                    Log.d("Touch", "Lance le tacticon");
                                }
                                else {
                                    Log.d("Touch", "Tacticon off");
                                }
                            }

                            // Déplacement d'un tacticon
                            else if (touchTap == 1 && moveAction) {
                                Toast.makeText(EnigmaExploActivity.this, "single touch movement", Toast.LENGTH_SHORT).show();
                                Log.d("Touch", "ST");

                                // Si le tacticon est remplacable ou added
                                if (touchedTacticon.getStatus().equals(Tacticon.Status.REPLECEABLE) || touchedTacticon.getStatus().equals(Tacticon.Status.ADDED)) {
                                    // Déplacement du tacticon
                                    enigma.proposeTacticon(selectedTacticon, sf.getNum());

                                    refreshColors(sf);

                                    // Changement de la couleur
                                    for (final EnigmaSurfaceLayout colorsf : surfaceLayouts) {
                                        if (colorsf.getNum()==selectedAreaIndex) {
                                            refreshColors(colorsf);
                                        }
                                    }

                                    moveAction = false;

                                    if (enigma.isCompleted()) {
                                        //ENVOIE LA FIN DE L'ACTIVITE à L'AUTRE
                                    }

                                    Log.d("Touch", "Déplacement");
                                }
                                else {
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
                                }
                                else {
                                    Log.d("Touch", "Taction non complementaire");
                                    Animation shakeAnim = AnimationUtils.loadAnimation(EnigmaExploActivity.this, R.anim.shake);
                                    main_layout.startAnimation(shakeAnim);
                                }
                            }

                            else if (touchTap == 2 && moveAction) {
                                Log.d("Touch", "Tacticon selectionné : double tap impossible");
                                Animation shakeAnim = AnimationUtils.loadAnimation(EnigmaExploActivity.this, R.anim.shake);
                                main_layout.startAnimation(shakeAnim);
                            }

                            touchTap = 0;
                            Log.d("Touch", "Fin" + String.valueOf(touchTap));
                        }
                    }, 500);
                    return false;
                }
            });

        }
    }

    public void refreshColors(EnigmaSurfaceLayout p_esl){
        Tacticon tacticon;

        if (p_esl.getNum() < 10)
            tacticon = enigma.getExplorerTab()[p_esl.getNum()];
        else
            tacticon = enigma.getExplorerComplementaryTab()[p_esl.getNum()-10];

        if (tacticon.getStatus().equals(Tacticon.Status.FIXED))
            p_esl.setBackgroundColor(getResources().getColor(R.color.lightblue));
        else if (tacticon.getStatus().equals(Tacticon.Status.REPLECEABLE))
            p_esl.setBackgroundColor(getResources().getColor(R.color.lightblue));
        else if (tacticon.getStatus().equals(Tacticon.Status.COMPLEMENTARY))
            p_esl.setBackgroundColor(getResources().getColor(R.color.orange));
        else if (tacticon.getStatus().equals(Tacticon.Status.ADDED))
            p_esl.setBackgroundColor(getResources().getColor(R.color.darkorange));
    }

    public void initSurfaceLayout(EnigmaSurfaceLayout p_surfaceLayout){

        if(p_surfaceLayout.getNum()<10) {

            for (int i = 0; i < enigma.getExplorerTab().length; ++i) {
                if (p_surfaceLayout.getNum() == i) {
                    if (enigma.getExplorerTab()[i].isOn()) {
                        p_surfaceLayout.setActiveTacticon(true);
                    }
                    else
                       p_surfaceLayout.setBackgroundColor(getResources().getColor(black));

                    if (enigma.getExplorerTab()[i].isReplaceable())
                        p_surfaceLayout.setMobileTacticon(true);
                }
            }
        }else {

            for (int i = 0; i < enigma.getExplorerComplementaryTab().length; ++i) {
                if (10 - p_surfaceLayout.getNum() == i) {
                    if (enigma.getExplorerTab()[i].isOn())
                        p_surfaceLayout.setActiveTacticon(true);
                    if (enigma.getExplorerTab()[i].isReplaceable())
                        p_surfaceLayout.setMobileTacticon(true);
                }
            }
        }

    }



    @Override
    public void onDialogTouch(EnigmaTouchEvent event) {
        Tacticon tacticonToRun;
        Tacticon tacticonToMove;
        int numberToRun;
        int numberToMoveSource;
        int numberToMoveDestination;

        // Lancement d'un tacticon
        if (event.getAction().equals(EnigmaTouchEvent.Action.RunT)) {

            // Index du tacticon à déclencher
            numberToRun = event.getEnigmaSurfaceLayoutNumber();
            // Tacticon du tableau principal
            if (numberToRun < 10)
                tacticonToRun = enigma.getExplorerTab()[numberToRun];
            // Tacticon du tableau secondaire
            else
                tacticonToRun = enigma.getExplorerComplementaryTab()[10 - numberToRun];

            // LANCER LE TACTICON
        }

        // Déplacement d'un tacticon
        else if (event.getAction().equals(EnigmaTouchEvent.Action.MoveT)) {
            // Index du tacticon à déplacer
            numberToMoveSource = event.getEnigmaSurfaceLayoutToMove();
            // Index de la destination
            numberToMoveDestination = event.getEnigmaSurfaceLayoutNumber();

            // Récupération du tacticon à déplacer
            if (numberToMoveSource < 10)
                tacticonToMove = enigma.getExplorerTab()[numberToMoveSource];
            else
                tacticonToMove = enigma.getExplorerComplementaryTab()[10 - numberToMoveSource];

            // Déplacement du tacticon
            enigma.proposeTacticon(tacticonToMove, numberToMoveDestination);
            // CHANGEMENT DE LA COULEUR

            if(enigma.isCompleted()) {
                //ENVOIE LA FIN DE L'ACTIVITE à L'AUTRE
            }

        }
    }
}
