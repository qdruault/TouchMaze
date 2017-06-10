package nf28.touchmaze.util.enigmaActivity.enigma;

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

    // Tableau contenant les numéros des enigme pré définies
    private ArrayList<Integer> usablePredefinedTabs;

    public static EnigmaManager getInstance(){
        if (instance == null)
            instance = new EnigmaManager();
        return instance;
    }

    private EnigmaManager(){
        usablePredefinedTabs = new ArrayList<Integer>();
        for (int i = 0; i < PredefinedEnigmas.getInstance().NB_PREDEFINED_ENIGMA; ++i) {
            usablePredefinedTabs.add(i);
        }
    }

    public HashMap<ExplorerEnigma, GuideEnigma> createNewEnigma(){

        // Numéro d'enigme déterminé au hasard.
        Random rand = new Random();
        int index = rand.nextInt(usablePredefinedTabs.size());
        int enigmaNb = usablePredefinedTabs.get(index);

        // Création des enigmes à partir de la classe ressource
        ExplorerEnigma predefinedExEnigma = new ExplorerEnigma(PredefinedEnigmas.getInstance().exEnigmas[enigmaNb]);
        GuideEnigma predefinedGuideEnigma = new GuideEnigma(PredefinedEnigmas.getInstance().guideEnigmas[enigmaNb]);

        // Création de la variable de retour
        HashMap<ExplorerEnigma, GuideEnigma> enigma = new HashMap<ExplorerEnigma, GuideEnigma>();

        enigma.put(predefinedExEnigma, predefinedGuideEnigma);

        System.out.println(enigmaNb);
        System.out.println("restants");

        // Suppression de l'énigme déjà utilisée du tableau.
        usablePredefinedTabs.remove(enigmaNb);
        for (Integer integer : usablePredefinedTabs) {
            System.out.println(integer);
        }

        return enigma;
    }
}
