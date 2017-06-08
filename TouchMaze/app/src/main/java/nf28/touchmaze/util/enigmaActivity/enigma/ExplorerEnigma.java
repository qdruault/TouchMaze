package nf28.touchmaze.util.enigmaActivity.enigma;

import nf28.touchmaze.util.enigmaActivity.tacticon.Circle;
import nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon;

/**
 * Classe Enigma pour l'explorateur.
 * Contient le tableau de l'explorateur et son tableau complémentaire.
 *
 * Created by Baptiste on 08/06/2017.
 */

public class ExplorerEnigma extends Enigma {
    private Tacticon[] explorerTab;

    private Tacticon[] explorerComplementaryTab;

    // Indicateur de la résolution de l'énigme.
    private boolean isOver;

    public ExplorerEnigma(){
        super();

        explorerComplementaryTab = new Tacticon[3];
        explorerTab = new Tacticon[6];
    }

    public ExplorerEnigma(ExplorerEnigma predefinedEnigma){
        super();

        explorerComplementaryTab = new Tacticon[3];
        explorerTab = new Tacticon[6];

        loadTabs(predefinedEnigma);
    }

    /**
     * Copie les tableaux de l'enigme passée en paramètre dans ceux de l'objet.
     * @param p_enigma
     */
    public void loadTabs(ExplorerEnigma p_enigma) {
        chosenGuideTab = p_enigma.getchosenGuideTab();

        explorerTab = p_enigma.getExplorerTab();

        generateComplementaryTab();
    }

    /**
     * Génère le tableau complémentaire à partir du tableau explorateur.
     */
    public void generateComplementaryTab() {
        int i = 0;
        // Pour chacun des tacticons "Off" du tableau explorateur
        for (Tacticon tacticon : explorerTab) {
            if (!tacticon.isOn()) {
                // On crée un tacticon de meme type dans le tableau complémentaire
                Class<? extends Tacticon> c = tacticon.getClass();
                switch (c.getName()){
                    case "tacticon.Circle":
                        explorerComplementaryTab[i] = new Circle();
                        explorerComplementaryTab[i].setReplaceable(true);
                        break;
                    default:
                        break;
                }
                i++;
            }
        }
    }

    /**
     * Permet de modifier le tacticon d'une case du tableau explorateur
     * @param p_tacticon
     * @param p_exploTabIndex
     */
    public void proposeTacticon(Tacticon p_tacticon, int p_exploTabIndex){
        explorerTab[p_exploTabIndex]=p_tacticon;
    }

    /**
     * Vérifie si le tableau explorateur modifié correspond au tableau initial.
     * @return
     */
    public boolean isCompleted(){
        for (int i = 0; i < chosenGuideTab.length; i++) {
            // Si le tacticon est "Off" ou si le tacticon ne correspond pas à celui attendu
            if (!explorerTab[i].isOn() || !chosenGuideTab[i].getClass().equals(explorerTab[i].getClass())){
                return false;
            }
        }
        return true;
    }


    public Tacticon[] getExplorerTab() {
        return explorerTab;
    }


    public void setExplorerTab(Tacticon[] explorerTab) {
        this.explorerTab = explorerTab;
    }


    public Tacticon[] getExplorerComplementaryTab() {
        return explorerComplementaryTab;
    }


    public void setExplorerComplementaryTab(Tacticon[] explorerComplementaryTab) {
        this.explorerComplementaryTab = explorerComplementaryTab;
    }


    public boolean isOver() {
        return isOver;
    }


    public void setOver(boolean isOver) {
        this.isOver = isOver;
    }

}
