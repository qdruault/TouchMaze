package nf28.touchmaze.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

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

    private EnigmaSurfaceLayout ex_tab_0;
    private EnigmaSurfaceLayout ex_tab_1;
    private EnigmaSurfaceLayout ex_tab_2;
    private EnigmaSurfaceLayout ex_tab_3;
    private EnigmaSurfaceLayout ex_tab_4;
    private EnigmaSurfaceLayout ex_tab_5;

    private EnigmaSurfaceLayout comp_tab_0;
    private EnigmaSurfaceLayout comp_tab_1;
    private EnigmaSurfaceLayout comp_tab_2;


    int touchTap = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enigma_explo);

        ex_tab_0 = (EnigmaSurfaceLayout)findViewById(R.id.ex_tab_0);
        ex_tab_1 = (EnigmaSurfaceLayout)findViewById(R.id.ex_tab_1);
        ex_tab_2 = (EnigmaSurfaceLayout)findViewById(R.id.ex_tab_2);
        ex_tab_3 = (EnigmaSurfaceLayout)findViewById(R.id.ex_tab_3);
        ex_tab_4 = (EnigmaSurfaceLayout)findViewById(R.id.ex_tab_4);
        ex_tab_5 = (EnigmaSurfaceLayout)findViewById(R.id.ex_tab_5);

        comp_tab_0 = (EnigmaSurfaceLayout)findViewById(R.id.comp_tab_0);
        comp_tab_1 = (EnigmaSurfaceLayout)findViewById(R.id.comp_tab_1);
        comp_tab_2 = (EnigmaSurfaceLayout)findViewById(R.id.comp_tab_2);

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


        ex_tab_0.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchTap++;
                Log.d("Touch", "Debut"+String.valueOf(touchTap));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (touchTap==1){
                            Toast.makeText(EnigmaExploActivity.this, "single touch", Toast.LENGTH_SHORT).show();
                            Log.d("Touch", "ST");
                        }else if (touchTap==2 && !moveAction){
                            Toast.makeText(EnigmaExploActivity.this, "double touch", Toast.LENGTH_SHORT).show();
                            Log.d("Touch", "DT");
                            moveAction=true;
                            ex_tab_0.setBackgroundColor(getResources().getColor(R.color.darkorange));
                        }
                        touchTap = 0;
                        Log.d("Touch", "Fin"+String.valueOf(touchTap));
                    }
                }, 500);
                return false;
            }
        });

    }

    public void initSurfaceLayout(EnigmaSurfaceLayout p_surfaceLayout){

        if(p_surfaceLayout.getNum()<10) {

            for (int i = 0; i < enigma.getExplorerTab().length; ++i) {
                if (p_surfaceLayout.getNum() == i) {
                    p_surfaceLayout.setBackgroundColor(getResources().getColor(black));
                    if (enigma.getExplorerTab()[i].isOn()) {
                        p_surfaceLayout.setActiveTacticon(true);
                        p_surfaceLayout.setBackgroundColor(getResources().getColor(holo_blue_bright));
                    }
                    if (enigma.getExplorerTab()[i].isReplaceable())
                        p_surfaceLayout.setMobileTacticon(true);
                }
            }
        }else {

            for (int i = 0; i < enigma.getExplorerComplementaryTab().length; ++i) {
                if (10 - p_surfaceLayout.getNum() == i) {
                    p_surfaceLayout.setBackgroundColor(getResources().getColor(R.color.orange));
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
