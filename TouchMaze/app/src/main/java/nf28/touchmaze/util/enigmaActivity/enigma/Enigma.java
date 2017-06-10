package nf28.touchmaze.util.enigmaActivity.enigma;

import nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon;

/**
 * Classe mère Enigma, contient le tableau de tacticon initial.
 *
 * Created by Baptiste on 08/06/2017.
 */

public class Enigma {

    protected Tacticon[] chosenGuideTab;


    public Enigma(){
        chosenGuideTab = new Tacticon[6];
    }


    public Tacticon[] getchosenGuideTab() {
        return chosenGuideTab;
    }

    public void setchosenGuideTab(Tacticon[] chosenGuideTab) {
        this.chosenGuideTab = chosenGuideTab;
    }

}
