package enigma;

import tacticon.Circle;
import tacticon.Tacticon;

public class GameEnigma extends Enigma {

	private Tacticon[] explorerComplementaryTab;

	private boolean isOver;

	public GameEnigma(Enigma PredefinedEnigma) {
		super();
		explorerComplementaryTab = new Tacticon[3];
		
		loadTabs(PredefinedEnigma);
	}

	public void loadTabs(Enigma p_enigma) {
		chosenGuideTab = p_enigma.getchosenGuideTab();
		secondGuideTab = p_enigma.getsecondGuideTab();
		thirdGuideTab = p_enigma.getthirdGuideTab();
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
	
	//peut etre plutot faire une classe pour l'explo pour avoir les infos et traiter le business chez lui
	
	//classe pour envoyer les trucs en json
	
	//jsonContent = mapper.writeValueAsString(grille.getEnsembles()[ID]);
	//Ensemble afterAnalysisEns = mapper.readValue(content, Ensemble.class);
	
	//lorsque le guide recoit les x y de l'explor quii est au bon endroit
	//on cree un guide enigma
	//puis un explorator enigma
	//l'explorator enigma est envoyé en json
	
	//l'enigme est resolue blabla
	//verif sur l'explorateur
	//une fois que la grille est complete
	//envoie de message au guide
	//si reponse ok on renvoi le truc et ca repart
	

}
