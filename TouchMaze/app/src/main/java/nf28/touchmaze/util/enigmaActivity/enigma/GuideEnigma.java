package nf28.touchmaze.util.enigmaActivity.enigma;

import nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon;

/**
 * Classe Enigma pour le guide.
 * Contient les deux tableaux supplémentaires pour le guide.
 *
 * Created by Baptiste on 08/06/2017.
 */

public class GuideEnigma extends Enigma {

    private Tacticon[] secondGuideTab;
    private Tacticon[] thirdGuideTab;

    public GuideEnigma(){
        super();

        secondGuideTab = new Tacticon[6];
        thirdGuideTab = new Tacticon[6];
    }

    public GuideEnigma(GuideEnigma predefinedEnigma){
        super();

        secondGuideTab = new Tacticon[6];
        thirdGuideTab = new Tacticon[6];

        loadTabs(predefinedEnigma);
    }

    /**
     * Copie les tableaux de l'enigme passée en paramètre dans ceux de l'objet.
     * @param p_enigma
     */
    public void loadTabs(GuideEnigma p_enigma) {
        chosenGuideTab = p_enigma.getchosenGuideTab();
        secondGuideTab = p_enigma.getSecondGuideTab();
        thirdGuideTab = p_enigma.getThirdGuideTab();
    }


    public Tacticon[] getSecondGuideTab() {
        return secondGuideTab;
    }

    public void setSecondGuideTab(Tacticon[] secondGuideTab) {
        this.secondGuideTab = secondGuideTab;
    }

    public Tacticon[] getThirdGuideTab() {
        return thirdGuideTab;
    }

    public void setThirdGuideTab(Tacticon[] thirdGuideTab) {
        this.thirdGuideTab = thirdGuideTab;
    }
}
