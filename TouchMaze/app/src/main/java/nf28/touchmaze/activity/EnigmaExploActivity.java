package nf28.touchmaze.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.HashMap;

import nf28.touchmaze.R;
import nf28.touchmaze.layout.SurfaceLayout;
import nf28.touchmaze.login.DialogHandler;
import nf28.touchmaze.util.enigmaActivity.enigma.EnigmaManager;
import nf28.touchmaze.util.enigmaActivity.enigma.ExplorerEnigma;
import nf28.touchmaze.util.enigmaActivity.enigma.GuideEnigma;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class EnigmaExploActivity extends AppCompatActivity {

  //faire un nouveau dialogviewhandler specialement pour les enigmes

  // faire un nouveau surfacelayout pour les tacticons / enigmes
  //avec form active
  // form mobile
  // numéro

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
  //il faudrait du coup désactiver les surfacelayout du tableau complémentaire

  //et a chaque changement, verifier
  // si la verif est a true
  //on envoie la fin à l'autre gars
  // et on repasse à lactivité lab


  // cote guide, il faut seulement avoir le on touche classique
  // et etre pret a recevoir la fin d'enigme
  // si fin, on repasse à l'activité lab

    private ExplorerEnigma enigma;
    private GuideEnigma gEnigma;
    private HashMap<ExplorerEnigma, GuideEnigma> enigmasMap;

    private SurfaceLayout ex_tab_0;
    private SurfaceLayout ex_tab_1;
    private SurfaceLayout ex_tab_2;
    private SurfaceLayout ex_tab_3;
    private SurfaceLayout ex_tab_4;
    private SurfaceLayout ex_tab_5;

    private SurfaceLayout comp_tab_0;
    private SurfaceLayout comp_tab_1;
    private SurfaceLayout comp_tab_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enigma_explo);

        ex_tab_0 = (SurfaceLayout)findViewById(R.id.ex_tab_0);
        ex_tab_1 = (SurfaceLayout)findViewById(R.id.ex_tab_1);
        ex_tab_2 = (SurfaceLayout)findViewById(R.id.ex_tab_2);
        ex_tab_3 = (SurfaceLayout)findViewById(R.id.ex_tab_3);
        ex_tab_4 = (SurfaceLayout)findViewById(R.id.ex_tab_4);
        ex_tab_5 = (SurfaceLayout)findViewById(R.id.ex_tab_5);

        comp_tab_0 = (SurfaceLayout)findViewById(R.id.comp_tab_0);
        comp_tab_1 = (SurfaceLayout)findViewById(R.id.comp_tab_1);
        comp_tab_2 = (SurfaceLayout)findViewById(R.id.comp_tab_2);

        enigmasMap = EnigmaManager.getInstance().createNewEnigma();

        for (HashMap.Entry<ExplorerEnigma, GuideEnigma> entry : enigmasMap.entrySet()) {

            enigma = entry.getKey();
            gEnigma = entry.getValue();
        }

        //envoie de l'enigme en JSON
        new Gson().toJson(gEnigma);

        //new Gson().fromJson(jsonStr, MyClass.class);

    }

    public void initSurfaceLayout(SurfaceLayout p_surfaceLayout){
     for (int i = 0; i < enigma.getexplotab; ++i){
       if (p_surfaceLayout.getNum() == i){
         if (enigma.getexplotab.get(i).isactive)
          p_surfaceLayout.activewall = true
        if (enigma.getexplotab.get(i).isremovable)
           p_surfaceLayout.mobiletacticon = true
       }

     }
    }
}
