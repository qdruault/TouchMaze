package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import resource.PredefinedEnigmas;

/**
 * Classe permettant la création d'Enigme.
 * 
 * @author Baptiste
 *
 */
public class EnigmaManager {
	
	public static EnigmaManager instance;
	
	// Tableau contenant les numéros des enigme pré définies
	private ArrayList<Integer> usablePredefinedTabs;
	
	public static EnigmaManager getInstance(){
		if (instance == null)
			instance = new EnigmaManager();
		return instance;
	}
	
	private EnigmaManager(){
		usablePredefinedTabs = new ArrayList<Integer>();
		for (int i = 0; i < PredefinedEnigmas.getInstance().NB_PREDEFINED_ENIGMA; ++i) {
			usablePredefinedTabs.add(i);
		}
	}
	
	public HashMap<ExplorerEnigma, GuideEnigma> createNewEnigma(){
		
		// Numéro d'enigme déterminé au hasard.
		int index = ThreadLocalRandom.current().nextInt(0, usablePredefinedTabs.size());
		int enigmaNb = usablePredefinedTabs.get(index);
		
		// Création des enigmes à partir de la classe ressource
		ExplorerEnigma predefinedExEnigma = PredefinedEnigmas.getInstance().exEnigmas[enigmaNb];
		GuideEnigma predefinedGuideEnigma = PredefinedEnigmas.getInstance().guideEnigmas[enigmaNb];

		// Création de la variable de retour
		HashMap<ExplorerEnigma, GuideEnigma> enigma = new HashMap<ExplorerEnigma, GuideEnigma>();
		
		enigma.put(predefinedExEnigma, predefinedGuideEnigma);
		
		System.out.println(enigmaNb);
		System.out.println("restants");
		
		// Suppression de l'énigme déjà utilisée du tableau.
		usablePredefinedTabs.remove(enigmaNb);
		for (Integer integer : usablePredefinedTabs) {
			System.out.println(integer);
		}
		
		return enigma;
	}
	
	// Envoi des infos en JSON
	// Sérialisation facile avec Jackson
	
	// Question du destinataire ? Comment communiquent les deux applis ?
	// Pouvoir faire passer les messages
	
	//jsonContent = mapper.writeValueAsString(ExEnigma);
	//ExplorerEnigma receivedEnigma = mapper.readValue(jsonContent, ExplorerEnigma.class);
	
	
	// On crée les deux enigmes avec le manager
	// On envoie l'enigme à l'explorateur en json
	// L'explorateur fait son taff
	// On envoie une notif de fin d'enigme au guide
	// Les deux change d'activité lorsque l'enigme est terminée
	
	// On peut decider d'hoster d'un coté ou de l'autre en fonction de ce qui est plus simple
	
	
}
