package su.nsk.iis.cpn;

import su.nsk.iis.cpn.ml.Expression;
import su.nsk.iis.cpn.ml.types.Type;

/**
 * Class representing an arc in coloured Petri net.
 * @author Alexander Stenenko
 */

public class Arc {

    private Place place;
    private Expression expression;
    
    /**
     * Constructs the arc with the given expression and connected to the given place.
     * @param place the place
     * @param expression the expression
     */
    Arc(Place place, Expression expression) {
        this.place = place;
        this.expression = expression;
    }
    
    /**
     * Gets the place to which the arc is connected.
     * @return the place
     */
    public Place getPlace() {
        return place;
    }
    
    /**
     * Gets the type of the place to which the arc is connected.
     * @return the type
     */
    public Type getType() {
        return place.getType();
    }
    
    /**
     * Gets the name of the type of the place to which the arc is connected.
     * @return the name of the type
     */
    public String getTypeName() {
        return getType().getName();
    }
    
    /**
     * Gets the name of the place to which the arc is connected.
     * @return the name of the place
     */
    public String getPlaceName() {
        return place.getName();    
    }
    
    /**
     * Gets the arc expression.
     * @return the expression
     */
    public Expression getExpression() {
        return expression;
    }
}
