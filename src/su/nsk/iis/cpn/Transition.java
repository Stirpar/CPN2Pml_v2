package su.nsk.iis.cpn;

import su.nsk.iis.cpn.ml.Expression;
import su.nsk.iis.cpn.ml.Variable;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class representing a transition in coloured Petri net.
 * @author Alexander Stenenko
 */
public final class Transition {

    private Expression guard;
    private String name;
    private List<Arc> inArcs;
    private List<Arc> outArcs;
    private Set<Variable> freeVariables;
    
    /**
     * Constructs the transition with the given name and guard.
     * @param guard the guard, may be null (then it means that the transition is always enabled)
     * @param name the name, should be unique
     */
    public Transition(Expression guard, String name) {
        freeVariables = new TreeSet<Variable>();
        this.guard = guard;
        this.name = name;
        inArcs = new LinkedList<Arc>();
        outArcs = new LinkedList<Arc>();
        if (guard != null) addFreeVariables(guard);
    }
    
    /**
     * Adds an input arc to the transition.
     * @param arc the arc
     */
    public void addInArc(Arc arc) {
        inArcs.add(arc);
        addFreeVariables(arc.getExpression());
    }
    
    /**
     * Adds an output arc to the transition.
     * @param arc the arc
     */
    public void addOutArc(Arc arc) {
        outArcs.add(arc);
        addFreeVariables(arc.getExpression());
    }
    
    /**
     * Gets the list of input arcs of the transition.
     * @return the list of input arcs, should not be modified
     */
    List<Arc> getInArcs() {
        return inArcs;
    }
    
    /**
     * Gets the list of output arcs of the transition.
     * @return the list of output arcs, should not be modified
     */
    List<Arc> getOutArcs() {
        return outArcs;
    }
    
    /**
     * Gets the transition name.
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the transition guard.
     * @return the guard expression
     */
    public Expression getGuard() {
        return guard;
    }
    
    /**
     * Gets the set of free variables of the transition.
     * @return the set of free variables, should not be modified
     */
    Set<Variable> getFreeVariables() {
        return freeVariables;
    }
    
    /**
     * Adds the free variables of the expression into the set of free variables of the transition.
     * @param expression the expression
     */
    private void addFreeVariables(Expression expression) {
        Set<Variable> vs = expression.getFreeVariables();
        for (Variable v : vs) {
            freeVariables.add(v);
        }
    }
}
