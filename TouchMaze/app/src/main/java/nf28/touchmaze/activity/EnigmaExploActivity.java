package nf28.touchmaze.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import nf28.touchmaze.R;
import nf28.touchmaze.login.DialogHandler;
import nf28.touchmaze.util.enigmaActivity.enigma.EnigmaManager;
import nf28.touchmaze.util.enigmaActivity.enigma.ExplorerEnigma;
import nf28.touchmaze.util.enigmaActivity.enigma.GuideEnigma;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class EnigmaExploActivity extends AppCompatActivity {

    private ExplorerEnigma enigma;
    private HashMap<ExplorerEnigma, GuideEnigma> enigmasMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enigma_explo);

        enigmasMap = EnigmaManager.getInstance().createNewEnigma();

        for (HashMap.Entry<ExplorerEnigma, GuideEnigma> entry : enigmasMap.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

    }
}
