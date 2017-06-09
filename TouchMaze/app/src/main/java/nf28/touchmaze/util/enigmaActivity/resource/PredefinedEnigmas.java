package nf28.touchmaze.util.enigmaActivity.resource;

import nf28.touchmaze.util.enigmaActivity.enigma.ExplorerEnigma;
import nf28.touchmaze.util.enigmaActivity.enigma.GuideEnigma;
import nf28.touchmaze.util.enigmaActivity.tacticon.Circle;
import nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class PredefinedEnigmas {

    public static PredefinedEnigmas instance;

    public final int NB_PREDEFINED_ENIGMA = 3;

    public final ExplorerEnigma[] exEnigmas = new ExplorerEnigma[NB_PREDEFINED_ENIGMA];
    public final GuideEnigma[] guideEnigmas = new GuideEnigma[NB_PREDEFINED_ENIGMA];

    public static PredefinedEnigmas getInstance(){
        if (instance==null)
            instance = new PredefinedEnigmas();
        return instance;
    }

    private PredefinedEnigmas(){
        //Enigme predefinie 1
        exEnigmas[0] = new ExplorerEnigma();

        exEnigmas[0].getchosenGuideTab()[0] = new Circle();
        exEnigmas[0].getchosenGuideTab()[1] = new Circle();
        exEnigmas[0].getchosenGuideTab()[2] = new Circle();
        exEnigmas[0].getchosenGuideTab()[3] = new Circle();
        exEnigmas[0].getchosenGuideTab()[4] = new Circle();
        exEnigmas[0].getchosenGuideTab()[5] = new Circle();

        exEnigmas[0].getExplorerTab()[0] = new Circle();
        exEnigmas[0].getExplorerTab()[1] = new Circle();
        exEnigmas[0].getExplorerTab()[2] = new Circle();
        exEnigmas[0].getExplorerTab()[3] = new Circle();
        exEnigmas[0].getExplorerTab()[4] = new Circle();
        exEnigmas[0].getExplorerTab()[5] = new Circle();

        exEnigmas[0].getExplorerTab()[3].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[0].getExplorerTab()[4].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[0].getExplorerTab()[5].setStatus(Tacticon.Status.REPLECEABLE);

        guideEnigmas[0] = new GuideEnigma();

        guideEnigmas[0].setchosenGuideTab(exEnigmas[0].getchosenGuideTab());

        guideEnigmas[0].getSecondGuideTab()[0] = new Circle();
        guideEnigmas[0].getSecondGuideTab()[1] = new Circle();
        guideEnigmas[0].getSecondGuideTab()[2] = new Circle();
        guideEnigmas[0].getSecondGuideTab()[3] = new Circle();
        guideEnigmas[0].getSecondGuideTab()[4] = new Circle();
        guideEnigmas[0].getSecondGuideTab()[5] = new Circle();

        guideEnigmas[0].getThirdGuideTab()[0] = new Circle();
        guideEnigmas[0].getThirdGuideTab()[1] = new Circle();
        guideEnigmas[0].getThirdGuideTab()[2] = new Circle();
        guideEnigmas[0].getThirdGuideTab()[3] = new Circle();
        guideEnigmas[0].getThirdGuideTab()[4] = new Circle();
        guideEnigmas[0].getThirdGuideTab()[5] = new Circle();

        //Enigme predefinie 2
        exEnigmas[1] = new ExplorerEnigma();

        exEnigmas[1].getchosenGuideTab()[0] = new Circle();
        exEnigmas[1].getchosenGuideTab()[1] = new Circle();
        exEnigmas[1].getchosenGuideTab()[2] = new Circle();
        exEnigmas[1].getchosenGuideTab()[3] = new Circle();
        exEnigmas[1].getchosenGuideTab()[4] = new Circle();
        exEnigmas[1].getchosenGuideTab()[5] = new Circle();

        exEnigmas[1].getExplorerTab()[0] = new Circle();
        exEnigmas[1].getExplorerTab()[1] = new Circle();
        exEnigmas[1].getExplorerTab()[2] = new Circle();
        exEnigmas[1].getExplorerTab()[3] = new Circle();
        exEnigmas[1].getExplorerTab()[4] = new Circle();
        exEnigmas[1].getExplorerTab()[5] = new Circle();

        exEnigmas[1].getExplorerTab()[0].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[1].getExplorerTab()[2].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[1].getExplorerTab()[4].setStatus(Tacticon.Status.REPLECEABLE);

        guideEnigmas[1] = new GuideEnigma();

        guideEnigmas[1].setchosenGuideTab(exEnigmas[1].getchosenGuideTab());

        guideEnigmas[1].getSecondGuideTab()[0] = new Circle();
        guideEnigmas[1].getSecondGuideTab()[1] = new Circle();
        guideEnigmas[1].getSecondGuideTab()[2] = new Circle();
        guideEnigmas[1].getSecondGuideTab()[3] = new Circle();
        guideEnigmas[1].getSecondGuideTab()[4] = new Circle();
        guideEnigmas[1].getSecondGuideTab()[5] = new Circle();

        guideEnigmas[1].getThirdGuideTab()[0] = new Circle();
        guideEnigmas[1].getThirdGuideTab()[1] = new Circle();
        guideEnigmas[1].getThirdGuideTab()[2] = new Circle();
        guideEnigmas[1].getThirdGuideTab()[3] = new Circle();
        guideEnigmas[1].getThirdGuideTab()[4] = new Circle();
        guideEnigmas[1].getThirdGuideTab()[5] = new Circle();

        //Enigme predefinie 3
        exEnigmas[2] = new ExplorerEnigma();

        exEnigmas[2].getchosenGuideTab()[0] = new Circle();
        exEnigmas[2].getchosenGuideTab()[1] = new Circle();
        exEnigmas[2].getchosenGuideTab()[2] = new Circle();
        exEnigmas[2].getchosenGuideTab()[3] = new Circle();
        exEnigmas[2].getchosenGuideTab()[4] = new Circle();
        exEnigmas[2].getchosenGuideTab()[5] = new Circle();

        exEnigmas[2].getExplorerTab()[0] = new Circle();
        exEnigmas[2].getExplorerTab()[1] = new Circle();
        exEnigmas[2].getExplorerTab()[2] = new Circle();
        exEnigmas[2].getExplorerTab()[3] = new Circle();
        exEnigmas[2].getExplorerTab()[4] = new Circle();
        exEnigmas[2].getExplorerTab()[5] = new Circle();

        exEnigmas[2].getExplorerTab()[2].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[2].getExplorerTab()[4].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[2].getExplorerTab()[5].setStatus(Tacticon.Status.REPLECEABLE);


        guideEnigmas[2] = new GuideEnigma();

        guideEnigmas[2].setchosenGuideTab(exEnigmas[2].getchosenGuideTab());

        guideEnigmas[2].getSecondGuideTab()[0] = new Circle();
        guideEnigmas[2].getSecondGuideTab()[1] = new Circle();
        guideEnigmas[2].getSecondGuideTab()[2] = new Circle();
        guideEnigmas[2].getSecondGuideTab()[3] = new Circle();
        guideEnigmas[2].getSecondGuideTab()[4] = new Circle();
        guideEnigmas[2].getSecondGuideTab()[5] = new Circle();

        guideEnigmas[2].getThirdGuideTab()[0] = new Circle();
        guideEnigmas[2].getThirdGuideTab()[1] = new Circle();
        guideEnigmas[2].getThirdGuideTab()[2] = new Circle();
        guideEnigmas[2].getThirdGuideTab()[3] = new Circle();
        guideEnigmas[2].getThirdGuideTab()[4] = new Circle();
        guideEnigmas[2].getThirdGuideTab()[5] = new Circle();

    }

}
