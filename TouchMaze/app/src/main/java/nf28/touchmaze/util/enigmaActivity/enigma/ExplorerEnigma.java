package nf28.touchmaze.util.enigmaActivity.enigma;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import nf28.touchmaze.util.enigmaActivity.tacticon.Alternation;
import nf28.touchmaze.util.enigmaActivity.tacticon.Circle;
import nf28.touchmaze.util.enigmaActivity.tacticon.Cube;
import nf28.touchmaze.util.enigmaActivity.tacticon.Pointbypoint;
import nf28.touchmaze.util.enigmaActivity.tacticon.Rotation;
import nf28.touchmaze.util.enigmaActivity.tacticon.Shape;
import nf28.touchmaze.util.enigmaActivity.tacticon.Snow;
import nf28.touchmaze.util.enigmaActivity.tacticon.Split;
import nf28.touchmaze.util.enigmaActivity.tacticon.Stick;
import nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon;
import nf28.touchmaze.util.enigmaActivity.tacticon.Wave;

/**
 * Classe Enigma pour l'explorateur.
 * Contient le tableau de l'explorateur et son tableau complémentaire.
 *
 * Created by Baptiste on 08/06/2017.
 */

public class ExplorerEnigma extends Enigma {
    private Tacticon[] explorerTab;

    private Tacticon[] explorerComplementaryTab;

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
            if (tacticon.getStatus().equals(Tacticon.Status.REPLECEABLE)) {
                // On crée un tacticon de meme type dans le tableau complémentaire
                Class<? extends Tacticon> c = tacticon.getClass();
                switch (c.getName()) {
                    case "nf28.touchmaze.util.enigmaActivity.tacticon.Circle":
                        explorerComplementaryTab[i] = new Circle();
                        break;
                    case "nf28.touchmaze.util.enigmaActivity.tacticon.Alternation":
                        explorerComplementaryTab[i] = new Alternation();
                        break;
                    case "nf28.touchmaze.util.enigmaActivity.tacticon.Cube":
                        explorerComplementaryTab[i] = new Cube();
                        break;
                    case "nf28.touchmaze.util.enigmaActivity.tacticon.Pointbypoint":
                        explorerComplementaryTab[i] = new Pointbypoint();
                        break;
                    case "nf28.touchmaze.util.enigmaActivity.tacticon.Rotation":
                        explorerComplementaryTab[i] = new Rotation();
                        break;
                    case "nf28.touchmaze.util.enigmaActivity.tacticon.Shape":
                        explorerComplementaryTab[i] = new Shape();
                        break;
                    case "nf28.touchmaze.util.enigmaActivity.tacticon.Snow":
                        explorerComplementaryTab[i] = new Snow();
                        break;
                    case "nf28.touchmaze.util.enigmaActivity.tacticon.Split":
                        explorerComplementaryTab[i] = new Split();
                        break;
                    case "nf28.touchmaze.util.enigmaActivity.tacticon.Stick":
                        explorerComplementaryTab[i] = new Stick();
                        break;
                    case "nf28.touchmaze.util.enigmaActivity.tacticon.Wave":
                        explorerComplementaryTab[i] = new Wave();
                        break;
                    default:
                        break;
                }
                explorerComplementaryTab[i].setStatus(Tacticon.Status.COMPLEMENTARY);
                i++;
            }
        }
        randomizeComplementaryTab();
    }

    /**
     * Permet de modifier le tacticon d'une case du tableau explorateur
     * @param p_tacticon
     * @param p_exploTabIndex
     */
    public void proposeTacticon(Tacticon p_tacticon, int p_exploTabIndex){
        Class<? extends Tacticon> c = p_tacticon.getClass();
        switch (c.getName()){
            case "nf28.touchmaze.util.enigmaActivity.tacticon.Circle":
                explorerTab[p_exploTabIndex] = new Circle();
                break;
            case "nf28.touchmaze.util.enigmaActivity.tacticon.Alternation":
                explorerTab[p_exploTabIndex] = new Alternation();
                break;
            case "nf28.touchmaze.util.enigmaActivity.tacticon.Cube":
                explorerTab[p_exploTabIndex] = new Cube();
                break;
            case "nf28.touchmaze.util.enigmaActivity.tacticon.Pointbypoint":
                explorerTab[p_exploTabIndex] = new Pointbypoint();
                break;
            case "nf28.touchmaze.util.enigmaActivity.tacticon.Rotation":
                explorerTab[p_exploTabIndex] = new Rotation();
                break;
            case "nf28.touchmaze.util.enigmaActivity.tacticon.Shape":
                explorerTab[p_exploTabIndex] = new Shape();
                break;
            case "nf28.touchmaze.util.enigmaActivity.tacticon.Snow":
                explorerTab[p_exploTabIndex] = new Snow();
                break;
            case "nf28.touchmaze.util.enigmaActivity.tacticon.Split":
                explorerTab[p_exploTabIndex] = new Split();
                break;
            case "nf28.touchmaze.util.enigmaActivity.tacticon.Stick":
                explorerTab[p_exploTabIndex] = new Stick();
                break;
            case "nf28.touchmaze.util.enigmaActivity.tacticon.Wave":
                explorerTab[p_exploTabIndex] = new Wave();
                break;
            default:
                break;
        }
        // Passage du tacticon au status ADDED
        explorerTab[p_exploTabIndex].setStatus(Tacticon.Status.ADDED);
    }

    /**
     * Vérifie si le tableau explorateur modifié correspond au tableau initial.
     * @return
     */
    public boolean isCompleted(){
        int i = 0;
        boolean completed = true;

        while (completed && i < chosenGuideTab.length){
            // Si le tacticon est "Off" ou si le tacticon ne correspond pas à celui attendu

            Log.d("completed", String.valueOf(explorerTab[i].getStatus()));
            Log.d("completed", String.valueOf(explorerTab[i].getClass()));
            Log.d("completed", String.valueOf(chosenGuideTab[i].getClass()));

            if (!chosenGuideTab[i].getClass().equals(explorerTab[i].getClass())){
                completed = false;
            }

            i++;
        }
        return completed;
    }

    public boolean isFull(){
        int i = 0;
        boolean full = true;

        while (full && i < chosenGuideTab.length){
            // Si il y a au moins un tacticon Off
            if (explorerTab[i].getStatus().equals(Tacticon.Status.REPLECEABLE)){
                full = false;
            }

            i++;
        }
        return full;
    }

    public void randomizeComplementaryTab(){
        Tacticon savedTacticon;
        ArrayList<Integer> position = new ArrayList<Integer>();
        position.add(1);
        position.add(2);
        position.add(3);

        Random rand = new Random();
        // random entre 0 et position.size
        int index = rand.nextInt(position.size());

        savedTacticon = explorerComplementaryTab[index];
        explorerComplementaryTab[index]=explorerComplementaryTab[0];
        explorerComplementaryTab[0]=savedTacticon;

        position.remove(index);

        index = rand.nextInt(position.size());

        savedTacticon = explorerComplementaryTab[index];
        explorerComplementaryTab[index]=explorerComplementaryTab[1];
        explorerComplementaryTab[1]=savedTacticon;

        index = rand.nextInt(position.size());

        savedTacticon = explorerComplementaryTab[index];
        explorerComplementaryTab[index]=explorerComplementaryTab[2];
        explorerComplementaryTab[2]=savedTacticon;
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

}
