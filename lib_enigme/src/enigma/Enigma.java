package enigma;

import tacticon.Tacticon;

/**
 * Classe mère Enigma, contient le tableau de tacticon initial.
 * 
 * @author Baptiste
 *
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
