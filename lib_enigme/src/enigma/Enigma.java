package enigma;

import tacticon.Tacticon;

public class Enigma {
	
	protected Tacticon[] chosenGuideTab;
	protected Tacticon[] secondGuideTab;
	protected Tacticon[] thirdGuideTab;
	
	protected Tacticon[] explorerTab;

	
	public Enigma(){
		chosenGuideTab = new Tacticon[6];
		secondGuideTab = new Tacticon[6];
		thirdGuideTab = new Tacticon[6];
		explorerTab = new Tacticon[6];	
	}


	public Tacticon[] getchosenGuideTab() {
		return chosenGuideTab;
	}


	public void setchosenGuideTab(Tacticon[] chosenGuideTab) {
		this.chosenGuideTab = chosenGuideTab;
	}


	public Tacticon[] getsecondGuideTab() {
		return secondGuideTab;
	}


	public void setsecondGuideTab(Tacticon[] secondGuideTab) {
		this.secondGuideTab = secondGuideTab;
	}


	public Tacticon[] getthirdGuideTab() {
		return thirdGuideTab;
	}


	public void setthirdGuideTab(Tacticon[] thirdGuideTab) {
		this.thirdGuideTab = thirdGuideTab;
	}


	public Tacticon[] getExplorerTab() {
		return explorerTab;
	}


	public void setExplorerTab(Tacticon[] explorerTab) {
		this.explorerTab = explorerTab;
	}



	
	
	
	

}
