package su.nsk.iis.cpn;

import su.nsk.iis.cpn.ml.Expression;
import su.nsk.iis.cpn.ml.types.Type;

/**
 * Class representing a place in coloured Petri net.
 * @author Alexander Stenenko
 */
public class Place implements Comparable<Place> {

    private Type type;
    private Expression initMarking;
    private String name;
    private boolean containsOnlyOneElement;

    /**
     * Constructs the place with the given name, type, and initial marking.
     * @param type the type
     * @param initMarking the initial marking, may be null
     * @param name the name, should be unique
     */
    public Place(Type type, Expression initMarking, String name) {
        this.type = type;
        this.initMarking = initMarking;
        this.name = name;
        containsOnlyOneElement = false;
    }

    public boolean isContainingOnlyOneElemnt() {
        return containsOnlyOneElement;
    }

    public void setContainingOnlyOneElemnt(boolean b) {
        containsOnlyOneElement = b;
    }

    /**
     * Gets the place name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the place type.
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the name of the place type.
     * @return the name of the type
     */
    public String getTypeName() {
        return type.getName();
    }

    /**
     * Gets the place initial marking.
     * @return the initial marking expression
     */
    public Expression getInitMarking() {
        return initMarking;
    }

    @Override
    public int compareTo(Place o) {
        return name.compareTo(o.name);
    }
}
