package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import resource.PredefinedEnigmas;

public class EnigmaManager {
	
	public static EnigmaManager instance;
	
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
		
		int index = ThreadLocalRandom.current().nextInt(0, usablePredefinedTabs.size());
		int enigmaNb = usablePredefinedTabs.get(index);
		
		ExplorerEnigma predefinedExEnigma = PredefinedEnigmas.getInstance().exEnigmas[enigmaNb];
		GuideEnigma predefinedGuideEnigma = PredefinedEnigmas.getInstance().guideEnigmas[enigmaNb];

		HashMap<ExplorerEnigma, GuideEnigma> enigma = new HashMap<ExplorerEnigma, GuideEnigma>();
		
		enigma.put(predefinedExEnigma, predefinedGuideEnigma);
		
		System.out.println(enigmaNb);
		System.out.println("restants");
		
		usablePredefinedTabs.remove(enigmaNb);
		for (Integer integer : usablePredefinedTabs) {
			System.out.println(integer);
		}
		
		return enigma;
	}
	
	
}
