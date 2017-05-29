package enigma;

import tacticon.Circle;
import tacticon.Tacticon;

public class ExplorerEnigma extends Enigma {
	
	private Tacticon[] explorerTab;
	
	private Tacticon[] explorerComplementaryTab;

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
	
	public void loadTabs(ExplorerEnigma p_enigma) {
		chosenGuideTab = p_enigma.getchosenGuideTab();
		
		explorerTab = p_enigma.getExplorerTab();

		generateComplementaryTab();
	}
	
	public void generateComplementaryTab() {
		int i = 0;
		for (Tacticon tacticon : explorerTab) {
			if (!tacticon.isOn()) {
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
	
	public void proposeTacticon(Tacticon p_tacticon, int p_exploTabIndex){
		explorerTab[p_exploTabIndex]=p_tacticon;
	}
	
	public boolean isCompleted(){
		for (int i = 0; i < chosenGuideTab.length; i++) {
			if (explorerTab[i].isOn() && chosenGuideTab[i].getClass().equals(explorerTab[i].getClass())){
				return true;
			}	
		}
		return false;
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
