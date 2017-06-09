package nf28.touchmaze.util.enigmaActivity.tacticon;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class Tacticon {

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
