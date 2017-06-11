package nf28.touchmaze.util.enigmaActivity.enigma;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import nf28.touchmaze.util.enigmaActivity.resource.PredefinedEnigmas;

/**
 * Classe permettant la création d'Enigme.
 *
 * Created by Baptiste on 08/06/2017.
 */

public class EnigmaManager {

    public static EnigmaManager instance;

    public static EnigmaManager getInstance(){
        if (instance == null)
            instance = new EnigmaManager();
        return instance;
    }

    private EnigmaManager(){
    }

    public HashMap<ExplorerEnigma, GuideEnigma> createNewEnigma(int enigmaNB){

        // Création des enigmes à partir de la classe ressource
        ExplorerEnigma predefinedExEnigma = new ExplorerEnigma(PredefinedEnigmas.getInstance().exEnigmas[enigmaNB]);
        GuideEnigma predefinedGuideEnigma = new GuideEnigma(PredefinedEnigmas.getInstance().guideEnigmas[enigmaNB]);

        // Création de la variable de retour
        HashMap<ExplorerEnigma, GuideEnigma> enigma = new HashMap<ExplorerEnigma, GuideEnigma>();

        enigma.put(predefinedExEnigma, predefinedGuideEnigma);

        return enigma;
    }
}