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
    private boolean isOn;
    private boolean isReplaceable;

    public Tacticon(){
        isOn=true;
        isReplaceable=false;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean isOn) {
        this.isOn = isOn;
    }

    public boolean isReplaceable() {
        return isReplaceable;
    }

    public void setReplaceable(boolean isReplaceable) {
        this.isReplaceable = isReplaceable;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
