package su.nsk.iis.cpn.ml;

import su.nsk.iis.cpn.ml.types.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Class representing a node of expression tree of CPN ML language.
 * @author Alexander Stenenko
 */

public class ExpressionNode {

    private ExpressionNodeType type;
    private int value;
    private String identifier;
    private Operator operator;
    private Function function;
    private Variable variable;
    private List<ExpressionNode> children;
    private Type expressionType;
    private boolean inlinePromelaExpression;
    private int treeSize = 1;
    private boolean allowAddingChildren;
    
    /**
     * Constructs the tree node of type 'constant' with the given value.
     * @param value the value
     */
    public ExpressionNode(int value) {
        this.type = ExpressionNodeType.CONSTANT;
        this.value = value;
        this.identifier = null;
        this.operator = null;
        this.variable = null;
        children = null;
        expressionType = Type.getIntType();
        inlinePromelaExpression = true;
        allowAddingChildren = false;
    }
    
    /**
     * Constructs the tree node of type 'identifier' (enum element). --------- TODO: replace id6irs by variables
     * @param identifier 
     */
    public ExpressionNode(String identifier) {
        this.type = ExpressionNodeType.IDENTIFIER;
        this.value = 0;
        this.identifier = identifier;
        this.operator = null;
        this.variable = null;
        children = null;
        expressionType = EnumType.getElementType(identifier);
        inlinePromelaExpression = true;
        allowAddingChildren = false;
    }
    
    /**
     * Constructs the tree node of type 'variable'.
     * @param variable the variable
     */
    public ExpressionNode(Variable variable) {
        this.type = ExpressionNodeType.VARIABLE;
        this.value = 0;
        this.identifier = null;
        this.operator = null;
        this.variable = variable;
        children = null;
        expressionType = variable.getType();
        //inlinePromelaExpression = true; // ? --2do:-- check if the following two lines are correct?
        Type varType = variable.getType();
        inlinePromelaExpression = varType instanceof IntType || varType instanceof BoolType || varType instanceof EnumType;
        allowAddingChildren = false;
    }


    public ExpressionNode(Function function) {
        this.type = ExpressionNodeType.FUNCTION;
        this.value = function.getArity();
        this.identifier = null;
        this.operator = null;
        this.function = function;
        children = new LinkedList<ExpressionNode>();
        expressionType = function.getType();
        //inlinePromelaExpression = true; // ? --2do:-- check if the following two lines are correct?
        Type varType = variable.getType();
        inlinePromelaExpression = varType instanceof IntType || varType instanceof BoolType || varType instanceof EnumType;
        allowAddingChildren = true;
    }
    
    /**
     * Constructs the tree node of type 'operator' with the given arity.
     * @param operator the operator
     * @param value the arity
     */
    public ExpressionNode(Operator operator, int value) { // do we need this arity - it is same as the number of children
        this.type = ExpressionNodeType.OPERATOR;
        this.value = value;
        this.identifier = null;
        this.operator = operator;
        this.variable = null;
        children = new LinkedList<ExpressionNode>();
        
        inlinePromelaExpression = false;
        allowAddingChildren = true;
        
        switch (operator) {
            case UNIT:
                expressionType = Type.getUnitType();
                //inline?
                break;
            case EMPTY:
            case MS:
            case MSSUM:
                expressionType = Type.getMsType(Type.getAnyType());
                break;
            case NEG:
            case PLUS: case MINUS:
            case MUL:
                inlinePromelaExpression = true;
            // --- fall through ---
            case DIV: case MOD:
                expressionType = Type.getIntType();
                break;
            case NOT:
            case AND:
            case OR:
            case LESS: case GREATER: case LESSEQ: case GREATEREQ:
                inlinePromelaExpression = true;
            // --- fall through ---
            case EQ: case NEQ:
                expressionType = Type.getBoolType();
                break;
            case TUPLE:
                expressionType = Type.getProductType(new LinkedList<Type>()); // hmmm...
                break;
            case LIST:
                expressionType = Type.getListType(Type.getAnyType());
                break;
            case IF:
            case ELEM:
            case CONCAT:
            case CONS:
                expressionType = null;
            default:
                expressionType = null;
        }        
    }
    
    /**
     * Gets the type of the tree node.
     * @return the type
     */
    public ExpressionNodeType getType() {
        return type;
    }

    /**
     * Checks if the tree node has type 'operator'.
     * @return true if the type is 'operator', false otherwise
     */
    public boolean isOperator() {
        return type == ExpressionNodeType.OPERATOR;
    }
    
    /**
     * Gets the operator.
     * @return the opertor if the tree node's type is 'operator', null otherwise
     */
    public Operator getOperator() {
        return operator;
    }
    
    /**
     * Gets the variable.
     * @return the variable if the tree node's type is 'variable', null otherwise
     */
    public Variable getVariable() {
        return variable;
    }
    
    /**
     * Gets the identifier.
     * @return the identifier if the tree node's type is 'identifier', null otherwise
     */
    public String getIdentifier() {
        return identifier;
    }
    
    /**
     * Gets the value.
     * @return the constant value if the tree node's type is 'constant', the op.arity if the tree node's type is 'operator', 0 otherwise
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Gets the type of the expression represented by the tree node.
     * @return the type of the expression
     */
    public Type getExpressionType() {
        return expressionType;
    }
    
    /**
     * Gets the list of children nodes of the tree node.
     * @return the list of children nodes if the tree node's type is 'operator', null otherwise
     */
    public List<ExpressionNode> getChildren() {
        return children;
    }
    
    /**
     * Checks if the expression represented by the tree can be calculated as Promela expression.
     * @return 
     */
    public boolean isInlinePromelaExpression() {
        return inlinePromelaExpression;
    }
    
    /**
     * Gets the size of the tree having the node as a root.
     * Size of the tree is the number of nodes in the tree
     * @return the size of the tree
     */
    public int getTreeSize() {
        return treeSize;
    }
    
    /**
     * Adds the child node to the tree node.
     * @param child the child node
     */
    void addChild(ExpressionNode child) {
        /*
         * after adding node Ch as a child to some node
         * adding children to Ch should be disabled
         */
        if (! allowAddingChildren) throw new RuntimeException("Adding children to node " + this + "is not allowed, because of this node is a child of some other node");
        child.allowAddingChildren = false;

        //

        treeSize += child.getTreeSize();
        if (! child.isInlinePromelaExpression()) inlinePromelaExpression = false;
        
        switch (operator) {
            case EMPTY:
            case UNIT:
                throw new RuntimeException("Adding child to 0-arity operator " + operator + " is not allowed");
            case NEG:
                if (children.size() > 0) throw new RuntimeException("Adding too much children to unary operator " + operator + " is not allowed");
        // ------ fall through ------
            case MUL:
            case DIV:
            case MOD:
            case PLUS:
            case MINUS:
            case LESS:
            case GREATER:
            case LESSEQ:
            case GREATEREQ:    
                if (children.size() > 1) throw new RuntimeException("Adding too much children to binary operator " + operator + " is not allowed");
                if (! (child.getExpressionType() instanceof IntType)) throw new RuntimeException("Child of operator " + operator + " should have INT type");
                break;
            case EQ:
            case NEQ:
                if (children.size() > 1) throw new RuntimeException("Adding too much children to binary operator " + operator + " is not allowed");
                if (children.size() == 1) {
                    if (! children.get(0).getExpressionType().meets(child.getExpressionType())) throw new RuntimeException("Uncompatible types of " + operator + " children");
                }
                break;
            case NOT:
                if (children.size() > 0) throw new RuntimeException("Adding too much children to unary operator " + operator + " is not allowed");
                // --- fall through ---
            case AND:
            case OR:
                if (children.size() > 1) throw new RuntimeException("Adding too much children to binary operator " + operator + " is not allowed");
                if (! (child.getExpressionType() instanceof BoolType)) throw new RuntimeException("Child of operator " + operator + " should have BOOL type");
                break;
            case TUPLE:
                List<Type> ts = new LinkedList<Type>( ((ProductType) expressionType).getTypes() );
                ts.add(child.getExpressionType());
                expressionType = Type.getProductType(ts); // optimize?
                break;
            case LIST:
                Type elType = ((ListType) expressionType).getElementType();
                if (elType instanceof AnyType) expressionType = Type.getListType(child.getExpressionType());
                else {
                    if (! elType.meets(child.getExpressionType())) throw new RuntimeException("Uncompatible types of _list_ children");
                }
                break;
            case IF:
                switch (children.size()) {
                    case 0:
                        if (! (child.getExpressionType() instanceof BoolType)) throw new RuntimeException("First child of operator " + operator + " should have BOOL type");
                        break;
                    case 1:
                        expressionType = child.getExpressionType();
                        break;
                    case 2:
                        if (! expressionType.meets(child.getExpressionType())) throw new RuntimeException("Uncompatible types of _if_ children");
                        break;
                    default:
                        throw new RuntimeException("Adding too much children to ternary operator " + operator + " is not allowed");
                }
                break;
            case ELEM:
                switch (children.size()) {
                    case 0:
                        if (child.getType() != ExpressionNodeType.CONSTANT) throw new RuntimeException("#<num> operator incorrect");
                        break;
                    case 1:
                        if (! (child.getExpressionType() instanceof ProductType)) throw new RuntimeException("#<num> child should be tuple");
                        ProductType pt = (ProductType) child.getExpressionType();
                        int v = children.get(0).getValue();
                        if (v <= 0 || v > pt.getTypes().size()) throw new RuntimeException("#<num> number does not meet the tuple size");
                        expressionType = pt.getTypes().get(v - 1);
                        break;
                    default:
                        throw new RuntimeException("Adding too much children to unary operator #<num> is not allowed");
                }
                break;
            case CONCAT:
                switch (children.size()) {
                    case 0:
                        expressionType = child.getExpressionType();
                        break;
                    case 1:
                        if (! expressionType.meets(child.getExpressionType())) throw new RuntimeException("Children types of operator " + operator + " do not meet");
                        break;
                    default:
                        throw new RuntimeException("Adding too much children to binary operator " + operator + " is not allowed");
                }
                break;
            case CONS:
                switch (children.size()) {
                    case 0:
                        expressionType = Type.getListType(child.getExpressionType());
                        break;
                    case 1:
                        if (! expressionType.meets(child.getExpressionType())) throw new RuntimeException("Children types of operator " + operator + " do not meet");
                        break;
                    default:
                        throw new RuntimeException("Adding too much children to binary operator " + operator + " is not allowed");
                }                
                break;
            case MS:
                switch (children.size()) {
                    case 0:
                        if (! (child.getExpressionType() instanceof IntType)) throw new RuntimeException("First child of operator " + operator + " should be INT");
                        break;
                    case 1:
                        expressionType = Type.getMsType(child.getExpressionType());
                        break;
                    default:
                        throw new RuntimeException("Adding too much children to binary operator " + operator + " is not allowed");
                }
                break;
            case MSSUM:
                if (! (child.getExpressionType() instanceof MsType)) throw new RuntimeException("Mssum child should be multiset");
                switch (children.size()) {
                    case 0:
                        expressionType = child.getExpressionType();
                        break;
                    case 1:
                        if (! expressionType.meets(child.getExpressionType())) {
                            throw new RuntimeException("Uncompatible types of mssum children");
                        }
                        break;
                    default:
                        throw new RuntimeException("Adding too much children to binary operator " + operator + " is not allowed");
                }
                break;
            default:
                throw new RuntimeException("Default alternative is not allowed");
        }
        children.add(child);
    }
    
    private boolean expressionTypeMeets(ExpressionNode node) {
        // 2do
        return true;
    }

    /**
     * Adds to the given set the free variables of the expression tree.
     * @param freeVariables the set
     */
    void getFreeVariables(Set<Variable> freeVariables) {
        switch (type)
        {
            case VARIABLE:
                freeVariables.add(variable);
                //fall through
            case CONSTANT:
                return;
            case OPERATOR:
                for (ExpressionNode node : children) {
                    node.getFreeVariables(freeVariables);
                }
        }
    }

    /**
     * Adds to the given set the variables, values of which can be bound by the expression tree.
     * @param boundVariables the set
     */
    void getBoundVariables(Set<Variable> boundVariables) {
        switch (type)
        {
            case VARIABLE:
                boundVariables.add(variable);
                //fall through
            case CONSTANT:
                return;
            case OPERATOR:
                switch (operator) {
                    case LIST:
                    case TUPLE:
                    case CONS:
                        for (ExpressionNode node : children) {
                            node.getBoundVariables(boundVariables);
                        }
                }
        }        
    }
    
    @Override
    public String toString() {
        switch (type)
        {
            case VARIABLE:
                return variable.getName() + "(var)";
            case IDENTIFIER:
                return identifier + "(id)";
            case CONSTANT:
                return String.valueOf(value);
            case OPERATOR:
                return "[" + operator.toString() + "] " + value;
            default: return "";
        }
    }
    
    /**
     * Prints tree node with children as XML document.
     */
    void printXML() {
        if (children == null) {
            System.err.println("<" + this + " />");
            return;
        }
        if (children.isEmpty()) {
            System.err.println("<" + this + " />");
            return;
        }
        System.err.println("<" + this + ">");
        for (ExpressionNode tr : children) {
            tr.printXML();
        }
        System.err.println("</" + this + ">");
    }
}
