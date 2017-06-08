package enigma;

import tacticon.Circle;
import tacticon.Tacticon;

/**
 * Classe Enigma pour l'explorateur.
 * Contient le tableau de l'explorateur et son tableau compl�mentaire.
 * 
 * @author Baptiste
 *
 */
public class ExplorerEnigma extends Enigma {
	
	private Tacticon[] explorerTab;
	
	private Tacticon[] explorerComplementaryTab;

	// Indicateur de la r�solution de l'�nigme.
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
	 * Copie les tableaux de l'enigme pass�e en param�tre dans ceux de l'objet.
	 * @param p_enigma
	 */
	public void loadTabs(ExplorerEnigma p_enigma) {
		chosenGuideTab = p_enigma.getchosenGuideTab();
		
		explorerTab = p_enigma.getExplorerTab();

		generateComplementaryTab();
	}
	
	/**
	 * G�n�re le tableau compl�mentaire � partir du tableau explorateur.
	 */
	public void generateComplementaryTab() {
		int i = 0;
		// Pour chacun des tacticons "Off" du tableau explorateur
		for (Tacticon tacticon : explorerTab) {
			if (!tacticon.isOn()) {
				// On cr�e un tacticon de meme type dans le tableau compl�mentaire
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
	 * V�rifie si le tableau explorateur modifi� correspond au tableau initial.
	 * @return
	 */
	public boolean isCompleted(){
		for (int i = 0; i < chosenGuideTab.length; i++) {
			// Si le tacticon est "Off" ou si le tacticon ne correspond pas � celui attendu
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
