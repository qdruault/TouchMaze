package nf28.touchmaze.util.enigmaActivity.tacticon;

/**
 * Created by Baptiste on 08/06/2017.
 */

public class Tacticon{

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

    public enum Type {
        ALTERNATION,
        CIRCLE,
        CUBE,
        POINT,
        ROTATION,
        SHAPE,
        SNOW,
        SPLIT,
        STICK,
        WAVE
    }

    private Type type;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
