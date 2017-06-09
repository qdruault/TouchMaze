package nf28.touchmaze.activity;

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
import java.util.Random;

import nf28.touchmaze.R;
import nf28.touchmaze.layout.EnigmaSurfaceLayout;
import nf28.touchmaze.layout.SurfaceLayout;
import nf28.touchmaze.util.enigmaActivity.enigma.Enigma;
import nf28.touchmaze.util.enigmaActivity.enigma.EnigmaManager;
import nf28.touchmaze.util.enigmaActivity.enigma.ExplorerEnigma;
import nf28.touchmaze.util.enigmaActivity.enigma.GuideEnigma;
import nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon;

import static android.R.color.black;
import static nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon.Status.REPLECEABLE;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class EnigmaGuideActivity extends AppCompatActivity {

    private GuideEnigma enigma;
    private HashMap<ExplorerEnigma, GuideEnigma> enigmasMap;

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

    private Tacticon touchedTacticon;

    private ArrayList<EnigmaSurfaceLayout> tableau_up;
    private ArrayList<EnigmaSurfaceLayout> tableau_middle;
    private ArrayList<EnigmaSurfaceLayout> tableau_bottom;

    private ArrayList<ArrayList<EnigmaSurfaceLayout>> surfaceLayouts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enigma_guide);

        enigmasMap = EnigmaManager.getInstance().createNewEnigma();

        for (HashMap.Entry<ExplorerEnigma, GuideEnigma> entry : enigmasMap.entrySet()) {
            enigma = entry.getValue();
        }

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


        randomizeTabs();

        debuginitSurfaceLayout();

        // 0 -> 5
        // chosenTab
        // 10 -> 15
        // secondTab
        // 100 -> 105
        // thirdTab

        //INTENT du gson ?

        //new Gson().fromJson(jsonStr, MyClass.class);
        //enigma = djespjdepjs

        for (ArrayList<EnigmaSurfaceLayout> tab : surfaceLayouts){
            for (final EnigmaSurfaceLayout sf : tab){
                sf.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.d("Touch", "Num du surfaceLayout" + String.valueOf(sf.getNum()));

                        if (sf.getNum() < 10)
                            touchedTacticon = enigma.getchosenGuideTab()[sf.getNum()];
                        else if (sf.getNum() < 100)
                            touchedTacticon = enigma.getSecondGuideTab()[sf.getNum() - 10];
                        else
                            touchedTacticon = enigma.getThirdGuideTab()[sf.getNum() - 100];

                        Log.d("Touch", String.valueOf(touchedTacticon.getStatus()));

                        Log.d("Touch", "Lancement");

                        // LANCER LE TACTICON

                        return false;
                    }
                });
            }
        }
    }

    public ArrayList<Integer> getPermutation(){
        ArrayList<Integer> position = new ArrayList<Integer>();
        ArrayList<Integer> order = new ArrayList<Integer>();
        position.add(1);
        position.add(2);
        position.add(3);

        Random rand = new Random();
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

    public void randomizeTabs(){

        ArrayList<Integer> order = getPermutation();

        Log.d("Touch", String.valueOf(order.get(0)));
        Log.d("Touch", String.valueOf(order.get(1)));
        Log.d("Touch", String.valueOf(order.get(2)));

        int i = 0;

        if (order.get(0)==1) {
            for (EnigmaSurfaceLayout sf : tableau_up) {
                sf.setNum(i);
                i++;
            }
        }
        else if (order.get(0)==2) {
            i = 10;
            for (EnigmaSurfaceLayout sf : tableau_up) {
                sf.setNum(i);
                i++;
            }
        }
        else if (order.get(0)==3) {
            i = 100;
            for (EnigmaSurfaceLayout sf : tableau_up) {
                sf.setNum(i);
                i++;
            }
        }

        i = 0;

        if (order.get(1)==1) {
            for (EnigmaSurfaceLayout sf : tableau_middle) {
                sf.setNum(i);
                i++;
            }
        }
        else if (order.get(1)==2) {
            i = 10;
            for (EnigmaSurfaceLayout sf : tableau_middle) {
                sf.setNum(i);
                i++;
            }
        }
        else if (order.get(1)==3) {
            i = 100;
            for (EnigmaSurfaceLayout sf : tableau_middle) {
                sf.setNum(i);
                i++;
            }
        }

        i = 0;

        if (order.get(2)==1) {
            for (EnigmaSurfaceLayout sf : tableau_bottom) {
                sf.setNum(i);
                i++;
            }
        }
        else if (order.get(2)==2) {
            i = 10;
            for (EnigmaSurfaceLayout sf : tableau_bottom) {
                sf.setNum(i);
                i++;
            }
        }
        else if (order.get(2)==3) {
            i = 100;
            for (EnigmaSurfaceLayout sf : tableau_bottom) {
                sf.setNum(i);
                i++;
            }
        }
    }

    public void debuginitSurfaceLayout(){
        for (ArrayList<EnigmaSurfaceLayout> tab : surfaceLayouts){
            for (final EnigmaSurfaceLayout sf : tab){
                if (sf.getNum() < 10)
                    sf.setBackgroundColor(getResources().getColor(black));
            }
        }
    }

}
