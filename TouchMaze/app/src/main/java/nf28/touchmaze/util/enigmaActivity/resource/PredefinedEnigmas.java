package nf28.touchmaze.util.enigmaActivity.resource;

import nf28.touchmaze.util.enigmaActivity.enigma.ExplorerEnigma;
import nf28.touchmaze.util.enigmaActivity.enigma.GuideEnigma;
import nf28.touchmaze.util.enigmaActivity.tacticon.Alternation;
import nf28.touchmaze.util.enigmaActivity.tacticon.Circle;
import nf28.touchmaze.util.enigmaActivity.tacticon.Pointbypoint;
import nf28.touchmaze.util.enigmaActivity.tacticon.Rotation;
import nf28.touchmaze.util.enigmaActivity.tacticon.Shape;
import nf28.touchmaze.util.enigmaActivity.tacticon.Snow;
import nf28.touchmaze.util.enigmaActivity.tacticon.Split;
import nf28.touchmaze.util.enigmaActivity.tacticon.Stick;
import nf28.touchmaze.util.enigmaActivity.tacticon.Tacticon;
import nf28.touchmaze.util.enigmaActivity.tacticon.Wave;

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

        exEnigmas[0].getchosenGuideTab()[0] = new Alternation();
        exEnigmas[0].getchosenGuideTab()[1] = new Circle();
        exEnigmas[0].getchosenGuideTab()[2] = new Pointbypoint();
        exEnigmas[0].getchosenGuideTab()[3] = new Rotation();
        exEnigmas[0].getchosenGuideTab()[4] = new Split();
        exEnigmas[0].getchosenGuideTab()[5] = new Stick();

        exEnigmas[0].getExplorerTab()[0] = new Alternation();
        exEnigmas[0].getExplorerTab()[1] = new Circle();
        exEnigmas[0].getExplorerTab()[2] = new Pointbypoint();
        exEnigmas[0].getExplorerTab()[3] = new Rotation();
        exEnigmas[0].getExplorerTab()[4] = new Split();
        exEnigmas[0].getExplorerTab()[5] = new Stick();

        exEnigmas[0].getExplorerTab()[3].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[0].getExplorerTab()[4].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[0].getExplorerTab()[5].setStatus(Tacticon.Status.REPLECEABLE);

        guideEnigmas[0] = new GuideEnigma();

        guideEnigmas[0].setchosenGuideTab(exEnigmas[0].getchosenGuideTab());

        guideEnigmas[0].getSecondGuideTab()[0] = new Wave();
        guideEnigmas[0].getSecondGuideTab()[1] = new Snow();
        guideEnigmas[0].getSecondGuideTab()[2] = new Shape();
        guideEnigmas[0].getSecondGuideTab()[3] = new Alternation();
        guideEnigmas[0].getSecondGuideTab()[4] = new Circle();
        guideEnigmas[0].getSecondGuideTab()[5] = new Pointbypoint();

        guideEnigmas[0].getThirdGuideTab()[0] = new Alternation();
        guideEnigmas[0].getThirdGuideTab()[1] = new Split();
        guideEnigmas[0].getThirdGuideTab()[2] = new Stick();
        guideEnigmas[0].getThirdGuideTab()[3] = new Rotation();
        guideEnigmas[0].getThirdGuideTab()[4] = new Snow();
        guideEnigmas[0].getThirdGuideTab()[5] = new Shape();

        //Enigme predefinie 2
        exEnigmas[1] = new ExplorerEnigma();

        exEnigmas[1].getchosenGuideTab()[0] = new Rotation();
        exEnigmas[1].getchosenGuideTab()[1] = new Wave();
        exEnigmas[1].getchosenGuideTab()[2] = new Shape();
        exEnigmas[1].getchosenGuideTab()[3] = new Alternation();
        exEnigmas[1].getchosenGuideTab()[4] = new Circle();
        exEnigmas[1].getchosenGuideTab()[5] = new Pointbypoint();

        exEnigmas[1].getExplorerTab()[0] = new Rotation();
        exEnigmas[1].getExplorerTab()[1] = new Wave();
        exEnigmas[1].getExplorerTab()[2] = new Shape();
        exEnigmas[1].getExplorerTab()[3] = new Alternation();
        exEnigmas[1].getExplorerTab()[4] = new Circle();
        exEnigmas[1].getExplorerTab()[5] = new Pointbypoint();

        exEnigmas[1].getExplorerTab()[0].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[1].getExplorerTab()[2].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[1].getExplorerTab()[4].setStatus(Tacticon.Status.REPLECEABLE);

        guideEnigmas[1] = new GuideEnigma();

        guideEnigmas[1].setchosenGuideTab(exEnigmas[1].getchosenGuideTab());

        guideEnigmas[1].getSecondGuideTab()[0] = new Rotation();
        guideEnigmas[1].getSecondGuideTab()[1] = new Circle();
        guideEnigmas[1].getSecondGuideTab()[2] = new Pointbypoint();
        guideEnigmas[1].getSecondGuideTab()[3] = new Wave();
        guideEnigmas[1].getSecondGuideTab()[4] = new Alternation();
        guideEnigmas[1].getSecondGuideTab()[5] = new Stick();

        guideEnigmas[1].getThirdGuideTab()[0] = new Rotation();
        guideEnigmas[1].getThirdGuideTab()[1] = new Stick();
        guideEnigmas[1].getThirdGuideTab()[2] = new Wave();
        guideEnigmas[1].getThirdGuideTab()[3] = new Alternation();
        guideEnigmas[1].getThirdGuideTab()[4] = new Pointbypoint();
        guideEnigmas[1].getThirdGuideTab()[5] = new Split();

        //Enigme predefinie 3
        exEnigmas[2] = new ExplorerEnigma();

        exEnigmas[2].getchosenGuideTab()[0] = new Split();
        exEnigmas[2].getchosenGuideTab()[1] = new Stick();
        exEnigmas[2].getchosenGuideTab()[2] = new Shape();
        exEnigmas[2].getchosenGuideTab()[3] = new Rotation();
        exEnigmas[2].getchosenGuideTab()[4] = new Pointbypoint();
        exEnigmas[2].getchosenGuideTab()[5] = new Alternation();

        exEnigmas[2].getExplorerTab()[0] = new Split();
        exEnigmas[2].getExplorerTab()[1] = new Stick();
        exEnigmas[2].getExplorerTab()[2] = new Shape();
        exEnigmas[2].getExplorerTab()[3] = new Rotation();
        exEnigmas[2].getExplorerTab()[4] = new Pointbypoint();
        exEnigmas[2].getExplorerTab()[5] = new Alternation();

        exEnigmas[2].getExplorerTab()[2].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[2].getExplorerTab()[4].setStatus(Tacticon.Status.REPLECEABLE);
        exEnigmas[2].getExplorerTab()[5].setStatus(Tacticon.Status.REPLECEABLE);


        guideEnigmas[2] = new GuideEnigma();

        guideEnigmas[2].setchosenGuideTab(exEnigmas[2].getchosenGuideTab());

        guideEnigmas[2].getSecondGuideTab()[0] = new Split();
        guideEnigmas[2].getSecondGuideTab()[1] = new Circle();
        guideEnigmas[2].getSecondGuideTab()[2] = new Wave();
        guideEnigmas[2].getSecondGuideTab()[3] = new Rotation();
        guideEnigmas[2].getSecondGuideTab()[4] = new Pointbypoint();
        guideEnigmas[2].getSecondGuideTab()[5] = new Shape();

        guideEnigmas[2].getThirdGuideTab()[0] = new Circle();
        guideEnigmas[2].getThirdGuideTab()[1] = new Stick();
        guideEnigmas[2].getThirdGuideTab()[2] = new Pointbypoint();
        guideEnigmas[2].getThirdGuideTab()[3] = new Rotation();
        guideEnigmas[2].getThirdGuideTab()[4] = new Wave();
        guideEnigmas[2].getThirdGuideTab()[5] = new Alternation();

    }

}
