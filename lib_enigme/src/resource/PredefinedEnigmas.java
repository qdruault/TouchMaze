package resource;

import enigma.Enigma;
import tacticon.Circle;

public class PredefinedEnigmas {
	
	public static PredefinedEnigmas instance;
	
	public final int NB_PREDEFINED_ENIGMA = 3;
	
	public final Enigma[] enigmas = new Enigma[NB_PREDEFINED_ENIGMA];
	
	public static PredefinedEnigmas getInstance(){
		if (instance==null)
			instance = new PredefinedEnigmas();
		return instance;
	}
	
	private PredefinedEnigmas(){
		//Enigme predefinie 1
		enigmas[0] = new Enigma();
		
		enigmas[0].getchosenGuideTab()[0] = new Circle();
		enigmas[0].getchosenGuideTab()[1] = new Circle();
		enigmas[0].getchosenGuideTab()[2] = new Circle();
		enigmas[0].getchosenGuideTab()[3] = new Circle();
		enigmas[0].getchosenGuideTab()[4] = new Circle();
		enigmas[0].getchosenGuideTab()[5] = new Circle();
		
		enigmas[0].getsecondGuideTab()[0] = new Circle();
		enigmas[0].getsecondGuideTab()[1] = new Circle();
		enigmas[0].getsecondGuideTab()[2] = new Circle();
		enigmas[0].getsecondGuideTab()[3] = new Circle();
		enigmas[0].getsecondGuideTab()[4] = new Circle();
		enigmas[0].getsecondGuideTab()[5] = new Circle();
		
		enigmas[0].getthirdGuideTab()[0] = new Circle();
		enigmas[0].getthirdGuideTab()[1] = new Circle();
		enigmas[0].getthirdGuideTab()[2] = new Circle();
		enigmas[0].getthirdGuideTab()[3] = new Circle();
		enigmas[0].getthirdGuideTab()[4] = new Circle();
		enigmas[0].getthirdGuideTab()[5] = new Circle();
		
		enigmas[0].setExplorerTab(enigmas[0].getchosenGuideTab());
		enigmas[0].getExplorerTab()[3].setOn(false);
		enigmas[0].getExplorerTab()[4].setOn(false);
		enigmas[0].getExplorerTab()[5].setOn(false);
		enigmas[0].getExplorerTab()[3].setReplaceable(true);
		enigmas[0].getExplorerTab()[4].setReplaceable(true);
		enigmas[0].getExplorerTab()[5].setReplaceable(true);
		
		//Enigme predefinie 2
		enigmas[1] = new Enigma();
		
		enigmas[1].getchosenGuideTab()[0] = new Circle();
		enigmas[1].getchosenGuideTab()[1] = new Circle();
		enigmas[1].getchosenGuideTab()[2] = new Circle();
		enigmas[1].getchosenGuideTab()[3] = new Circle();
		enigmas[1].getchosenGuideTab()[4] = new Circle();
		enigmas[1].getchosenGuideTab()[5] = new Circle();
		
		enigmas[1].getsecondGuideTab()[0] = new Circle();
		enigmas[1].getsecondGuideTab()[1] = new Circle();
		enigmas[1].getsecondGuideTab()[2] = new Circle();
		enigmas[1].getsecondGuideTab()[3] = new Circle();
		enigmas[1].getsecondGuideTab()[4] = new Circle();
		enigmas[1].getsecondGuideTab()[5] = new Circle();
		
		enigmas[1].getthirdGuideTab()[0] = new Circle();
		enigmas[1].getthirdGuideTab()[1] = new Circle();
		enigmas[1].getthirdGuideTab()[2] = new Circle();
		enigmas[1].getthirdGuideTab()[3] = new Circle();
		enigmas[1].getthirdGuideTab()[4] = new Circle();
		enigmas[1].getthirdGuideTab()[5] = new Circle();
		
		enigmas[1].setExplorerTab(enigmas[1].getthirdGuideTab());
		enigmas[1].getExplorerTab()[0].setOn(false);
		enigmas[1].getExplorerTab()[2].setOn(false);
		enigmas[1].getExplorerTab()[4].setOn(false);
		enigmas[1].getExplorerTab()[0].setReplaceable(true);
		enigmas[1].getExplorerTab()[2].setReplaceable(true);
		enigmas[1].getExplorerTab()[4].setReplaceable(true);
		
		//Enigme predefinie 3
		enigmas[2] = new Enigma();
		
		enigmas[2].getchosenGuideTab()[0] = new Circle();
		enigmas[2].getchosenGuideTab()[1] = new Circle();
		enigmas[2].getchosenGuideTab()[2] = new Circle();
		enigmas[2].getchosenGuideTab()[3] = new Circle();
		enigmas[2].getchosenGuideTab()[4] = new Circle();
		enigmas[2].getchosenGuideTab()[5] = new Circle();
		
		enigmas[2].getsecondGuideTab()[0] = new Circle();
		enigmas[2].getsecondGuideTab()[1] = new Circle();
		enigmas[2].getsecondGuideTab()[2] = new Circle();
		enigmas[2].getsecondGuideTab()[3] = new Circle();
		enigmas[2].getsecondGuideTab()[4] = new Circle();
		enigmas[2].getsecondGuideTab()[5] = new Circle();
		
		enigmas[2].getthirdGuideTab()[0] = new Circle();
		enigmas[2].getthirdGuideTab()[1] = new Circle();
		enigmas[2].getthirdGuideTab()[2] = new Circle();
		enigmas[2].getthirdGuideTab()[3] = new Circle();
		enigmas[2].getthirdGuideTab()[4] = new Circle();
		enigmas[2].getthirdGuideTab()[5] = new Circle();
		
		enigmas[2].setExplorerTab(enigmas[2].getsecondGuideTab());
		enigmas[2].getExplorerTab()[2].setOn(false);
		enigmas[2].getExplorerTab()[4].setOn(false);
		enigmas[2].getExplorerTab()[5].setOn(false);
		enigmas[2].getExplorerTab()[2].setReplaceable(true);
		enigmas[2].getExplorerTab()[4].setReplaceable(true);
		enigmas[2].getExplorerTab()[5].setReplaceable(true);
		
	}
	
}
