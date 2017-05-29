package enigma;

import tacticon.Tacticon;

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
