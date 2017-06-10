package nf28.touchmaze.util.enigmaActivity.tacticon;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class Tacticon {

    // FIXED = actif et non mobile et non modifiable
    // REPLECEABLE = non actif et non mobile et modifiable
    // COMPLEMENTARY = actif et mobile et non modifiable
    // ADDED = actif et non mobile et modifiable
    public enum Status {
        FIXED,
        REPLECEABLE,
        COMPLEMENTARY,
        ADDED
    }

    private Status status;

    public Tacticon(){
        status = Tacticon.Status.FIXED;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
