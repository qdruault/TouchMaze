package enigma;

import tacticon.Tacticon;

/**
 * Classe Enigma pour le guide.
 * Contient les deux tableaux suppl�mentaires pour le guide.
 * 
 * @author Baptiste
 *
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
	 * Copie les tableaux de l'enigme pass�e en param�tre dans ceux de l'objet.
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
