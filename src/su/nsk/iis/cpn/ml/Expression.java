package su.nsk.iis.cpn.ml;

import su.nsk.iis.cpn.ml.types.Type;

import java.util.*;

/**
 * Class representing an expression of CPN ML language.
 * @author Alexander Stenenko
 */
public class Expression {

    // =========== begin static ===========
    /**
     * Enumeration of operators "fixity" types.
     */
    enum Fixity {
        PREFIX, INFIX_LTR, INFIX_RTL
    }

    /**
     * operators' priority
     */
    private static final Map<Operator, Integer> priority = new EnumMap<Operator, Integer>(Operator.class);

    /**
     * operators' arity
     */
    private static final Map<Operator, Integer> arity = new EnumMap<Operator, Integer>(Operator.class);

    /**
     * operators' fixity
     */
    private static final Map<Operator, Fixity> fixity = new EnumMap<Operator, Fixity>(Operator.class);

    /**
     * conversion from strings to operators
     */
    private static final Map<String, Operator> operators = new TreeMap<String, Operator>();
    
    static {
        operators.put(",", Operator.COMMA);
        operators.put("(", Operator.OPEN);
        operators.put(")", Operator.CLOSE);
        operators.put("[", Operator.OPEN_SQ);
        operators.put("]", Operator.CLOSE_SQ);
        operators.put("{", Operator.OPEN_C);
        operators.put("}", Operator.CLOSE_C);

        operators.put("~", Operator.NEG);
        operators.put("*", Operator.MUL);
        operators.put("div", Operator.DIV);
        operators.put("mod", Operator.MOD);
        operators.put("+", Operator.PLUS);
        operators.put("-", Operator.MINUS);
        operators.put("<", Operator.LESS);
        operators.put("&lt;", Operator.LESS);
        operators.put(">", Operator.GREATER);
        operators.put("&gt;", Operator.GREATER);
        operators.put("<=", Operator.LESSEQ);
        operators.put("&lt;=", Operator.LESSEQ);
        operators.put(">=", Operator.GREATEREQ);
        operators.put("&gt;=", Operator.GREATEREQ);
        operators.put("=", Operator.EQ);
        operators.put("<>", Operator.NEQ);
        operators.put("&lt;&gt;", Operator.NEQ);
        operators.put("not", Operator.NOT);
        operators.put("andalso", Operator.AND);
        operators.put("orelse", Operator.OR);
        operators.put("if", Operator.IF);
        operators.put("then", Operator.THEN);
        operators.put("else", Operator.ELSE);
        operators.put("nil", Operator.NIL);
        operators.put("#", Operator.ELEM);
        operators.put("::", Operator.CONS);
        operators.put("^^", Operator.CONCAT);
        operators.put("`", Operator.MS);
        operators.put("++", Operator.MSSUM);
        operators.put("@", Operator.TIME);
        operators.put("@+", Operator.ADDTIME);


        int i = 0;
        priority.put(Operator.NEG, i);
        ++i;
        priority.put(Operator.MUL, i);
        priority.put(Operator.DIV, i);
        priority.put(Operator.MOD, i);
        ++i;
        priority.put(Operator.PLUS, i);
        priority.put(Operator.MINUS, i);
        ++i;
        priority.put(Operator.LESS, i);
        priority.put(Operator.GREATER, i);
        priority.put(Operator.LESSEQ, i);
        priority.put(Operator.GREATEREQ, i);
        priority.put(Operator.EQ, i);
        priority.put(Operator.NEQ, i);
        ++i;
        priority.put(Operator.NOT, i);
        priority.put(Operator.AND, i);
        priority.put(Operator.OR, i);
        ++i;
        priority.put(Operator.CONCAT, i);
        ++i;
        priority.put(Operator.MS, i);
        ++i;
        priority.put(Operator.MSSUM, i);
        ++i;
        priority.put(Operator.CONS, i);
        ++i;
        priority.put(Operator.TIME, i); // where should it be?
        priority.put(Operator.ADDTIME, i);
        ++i;
        priority.put(Operator.IF, i);
        priority.put(Operator.THEN, i);
        priority.put(Operator.ELSE, i);
        ++i;
        priority.put(Operator.OPEN, i);
        priority.put(Operator.OPEN_SQ, i);
        priority.put(Operator.OPEN_C, i);


        arity.put(Operator.NEG, 1);
        arity.put(Operator.MUL, 2);
        arity.put(Operator.DIV, 2);
        arity.put(Operator.MOD, 2);
        arity.put(Operator.PLUS, 2);
        arity.put(Operator.MINUS, 2);
        arity.put(Operator.LESS, 2);
        arity.put(Operator.GREATER, 2);
        arity.put(Operator.LESSEQ, 2);
        arity.put(Operator.GREATEREQ, 2);
        arity.put(Operator.EQ, 2);
        arity.put(Operator.NEQ, 2);
        arity.put(Operator.NOT, 1);
        arity.put(Operator.AND, 2);
        arity.put(Operator.OR, 2);
        arity.put(Operator.ELEM, 2);
        arity.put(Operator.CONS, 2);
        arity.put(Operator.CONCAT, 2);
        arity.put(Operator.MS, 2);
        arity.put(Operator.MSSUM, 2);
        arity.put(Operator.UNIT, 0);
        arity.put(Operator.TIME, 2);
        arity.put(Operator.ADDTIME, 2);


        fixity.put(Operator.NEG, Fixity.PREFIX);
        fixity.put(Operator.MUL, Fixity.INFIX_LTR);
        fixity.put(Operator.DIV, Fixity.INFIX_LTR);
        fixity.put(Operator.MOD, Fixity.INFIX_LTR);
        fixity.put(Operator.PLUS, Fixity.INFIX_LTR);
        fixity.put(Operator.MINUS, Fixity.INFIX_LTR);
        fixity.put(Operator.LESS, Fixity.INFIX_LTR);
        fixity.put(Operator.GREATER, Fixity.INFIX_LTR);
        fixity.put(Operator.LESSEQ, Fixity.INFIX_LTR);
        fixity.put(Operator.GREATEREQ, Fixity.INFIX_LTR);
        fixity.put(Operator.EQ, Fixity.INFIX_LTR);
        fixity.put(Operator.NEQ, Fixity.INFIX_LTR);
        fixity.put(Operator.NOT, Fixity.PREFIX);
        fixity.put(Operator.AND, Fixity.INFIX_LTR);
        fixity.put(Operator.OR, Fixity.INFIX_LTR);
        fixity.put(Operator.ELEM, Fixity.PREFIX);
        fixity.put(Operator.CONS, Fixity.INFIX_RTL);
        fixity.put(Operator.CONCAT, Fixity.INFIX_LTR);
        fixity.put(Operator.MS, Fixity.INFIX_LTR);
        fixity.put(Operator.MSSUM, Fixity.INFIX_LTR);
        fixity.put(Operator.UNIT, Fixity.PREFIX);
        fixity.put(Operator.TIME, Fixity.INFIX_LTR);
        fixity.put(Operator.ADDTIME, Fixity.INFIX_LTR);
    }
    // =========== end static ===========


    private ExpressionNode root; // the root of the expression tree
    private Set<Variable> freeVariables; // all the variables of the expression
    private Set<Variable> boundVariables; // the variables of the expression, that can be bound with the value of the expression

    /**
     * Stack which builds a tree with "smart" push method.
     */
    class SmartStack extends Stack<ExpressionNode> {
        
        private Stack<ExpressionNode> rev = new Stack<ExpressionNode>();
        
        /**
         * If the given node is an operator THEN pops from the stack as many nodes from the stack,
         * as there must be operator's arguments and adds them to the node as children node;
         * pushes the given node to the top of stack.
         * @param node the node
         * @return the pushed node
         * @throws su.nsk.iis.cpn.ml.SyntaxError if syntax error occures
         */
        public ExpressionNode smartPush(ExpressionNode node) throws SyntaxError, TypeError {
            if (node.isOperator() || node.isFunction()) {
                for (int i = 0; i < node.getValue(); ++i) {
                    if (empty()) throw new SyntaxError("Incorrect number of arguments for operator \'" + node.getOperator() + "\' (must be " + node.getValue() + ")");
                    rev.push(pop());
                }
                while (! rev.empty()) {
                    node.addChild( rev.pop() );
                }
            }
            return super.push(node);
        }
    }    
    
    /**
     * This method should be called every time when new argument to the possible prefix operator was pushed
     * to the tree-building stack.
     * @param stack the tree-building stack
     * @param opStack the operators stack
     * @throws su.nsk.iis.cpn.ml.SyntaxError if syntax error occures
     */
    private void pushedArg(SmartStack stack, Stack <ExpressionNode> opStack) throws SyntaxError, TypeError {
        if (! opStack.empty()) {
            ExpressionNode top = opStack.peek();
            Operator op = top.getOperator();
            if (fixity.get(op) == Fixity.PREFIX) {
                opStack.pop();
                int v = top.getValue() + 1;
                top = new ExpressionNode(new Function(op));
                if (v == arity.get(op)) {
                    stack.smartPush(top);
                    pushedArg(stack, opStack);
                }
                else opStack.push(top);
            }
        }
    }
    
    /**
     * Constructs the expression from the given string.
     * @param lexer the lexer
     * @throws su.nsk.iis.cpn.ml.SyntaxError if syntax error occures
     */
    public Expression(Lexer lexer) throws SyntaxError, TypeError {
        SmartStack stack = new SmartStack();
        Stack <ExpressionNode> opStack = new Stack<ExpressionNode>();
        Lexem prevLexem = null;
        for (Lexem lexem = lexer.curLexem(); lexem.getType() != LexemType.EOF; lexem = lexer.nextLexem()) {
            switch (lexem.getType()) {
                case OPERATOR:
                    Operator op = operators.get(lexem.getString());
                    int argsNum;
                    switch (op) {
                        case NIL:
                            stack.smartPush( new ExpressionNode(Operator.LIST, 0) );
                            pushedArg(stack, opStack);
                            break;
                        case IF:
                            if (! opStack.empty()) {
                                switch (opStack.peek().getOperator()) {
                                    case OPEN:
                                    case OPEN_SQ:
                                    case OPEN_C:
                                        break;
                                    default:
                                        throw new SyntaxError("IF-THEN-ELSE statement must be in brackets");
                                }
                            }
                            opStack.push( new ExpressionNode(Operator.IF, 1) );
                            break;
                        case THEN:
                        case ELSE:
                            while (! opStack.empty()) {
                                ExpressionNode top = opStack.peek();
                                if (top.getOperator() == Operator.IF) break;
                                stack.smartPush(top);
                                pushedArg(stack, opStack);
                                opStack.pop();
                            }
                            if (opStack.empty()) throw new SyntaxError("Incorrect IF-THEN-ELSE statement");
                            ExpressionNode IF = opStack.pop();
                            argsNum = IF.getValue() + 1;
                            if (op == Operator.THEN && argsNum != 2 || op == Operator.ELSE && argsNum != 3) {
                                throw new SyntaxError("Incorrect IF-THEN-ELSE statement");
                            }
                            opStack.push( new ExpressionNode(Operator.IF, argsNum) );
                            break;
                        case OPEN:
                        case OPEN_SQ:
                        case OPEN_C:
                            opStack.push( new ExpressionNode(op, 0) );
                            break;
                        case COMMA:
                        case CLOSE:
                        case CLOSE_SQ:
                        case CLOSE_C:
                            // <>
                            if (prevLexem.getString().equals("[") && op == Operator.CLOSE_SQ) { // detect nil
                                opStack.pop();
                                stack.smartPush(new ExpressionNode(Operator.LIST, 0));
                                pushedArg(stack, opStack);
                                break;
                            }
                            if (prevLexem.getString().equals("(") && op == Operator.CLOSE) { // detect unit
                                opStack.pop();
                                stack.smartPush(new ExpressionNode(new Function(Operator.UNIT)));
                                pushedArg(stack, opStack);
                                break;
                            }
                            // </>
                            
                            while (! opStack.empty()) {
                                ExpressionNode top = opStack.pop();
                                if (top.getOperator() == Operator.OPEN || top.getOperator() == Operator.OPEN_SQ || top.getOperator() == Operator.OPEN_C) {
                                    opStack.push(top);
                                    break;
                                }
                                if (opStack.peek().getOperator() != Operator.OPEN_C) {
                                    stack.smartPush(top);
                                    pushedArg(stack, opStack); /* ? */
                                }
                                else {
                                    ExpressionNode n1 = stack.pop();
                                    ExpressionNode n2 = stack.pop();
                                    stack.smartPush(n1);
                                }
                            }
                            if (opStack.empty()) throw new SyntaxError("Incorrect brackets");
                            
                            ExpressionNode open = opStack.pop();
                            Operator op2 = open.getOperator();
                            argsNum = open.getValue() + 1;
                            if (op == Operator.COMMA) {
                                opStack.push( new ExpressionNode(op2, argsNum) );
                            }
                            else {
                                if (op2 == Operator.OPEN && op != Operator.CLOSE || op2 == Operator.OPEN_SQ && op != Operator.CLOSE_SQ || op2 == Operator.OPEN_C && op != Operator.CLOSE_C) {
                                    throw new SyntaxError("Incorrect brackets");
                                }
                                
                                if (op == Operator.CLOSE && argsNum > 1) {
                                    stack.smartPush(new ExpressionNode(Operator.TUPLE, argsNum));
                                }
                                else if (op == Operator.CLOSE_SQ) {
                                    stack.smartPush(new ExpressionNode(Operator.LIST, argsNum));
                                }
                                else if (op == Operator.CLOSE_C) {
                                    stack.smartPush(new ExpressionNode(Operator.TUPLE, argsNum));
                                }
                                //nothing forgot?
                                pushedArg(stack, opStack);
                            }
                            break;
                        default: // other operators
                            int curPriority;
                            switch (fixity.get(op)) {
                                case PREFIX:
                                    opStack.push( new ExpressionNode(new Function(op)));
                                    break;
                                case INFIX_LTR:
                                    curPriority = priority.get( op );
                                    while (! opStack.empty()) {
                                        ExpressionNode top = opStack.peek();
                                        if (fixity.get(top.getOperator()) == Fixity.PREFIX) break;
                                        if (priority.get(top.getOperator()) > curPriority) break;
                                        stack.smartPush(top);
                                        opStack.pop();
                                    }
                                    opStack.push( new ExpressionNode(new Function(op)) );
                                    break;
                                case INFIX_RTL:
                                    curPriority = priority.get( op );
                                    while (! opStack.empty()) {
                                        ExpressionNode top = opStack.peek();
                                        if (fixity.get(top.getOperator()) == Fixity.PREFIX) break;
                                        if (priority.get(top.getOperator()) >= curPriority) break;
                                        stack.smartPush(top);
                                        opStack.pop();
                                    }
                                    opStack.push( new ExpressionNode(new Function(op)) );
                                    break;
                                default:
                            }
                    }
                    break;
                case VARIABLE:
                case CONSTANT:
                    stack.smartPush( new ExpressionNode( Variable.getVariableByName(lexem.getString()) ) );
                    pushedArg(stack, opStack);
                    break;
                /*case IDENTIFIER:
                    stack.smartPush( new ExpressionNode( lexem.getString() ) );
                    pushedArg(stack, opStack);
                    break;*/
                case DECIMAL_LITERAL:
                    // todo check litaral
                    stack.smartPush( new ExpressionNode( Integer.parseInt(lexem.getString()) ) );
                    pushedArg(stack, opStack);
                    break;
            }
            prevLexem = lexem;
        }
        while (!opStack.empty()) {
            ExpressionNode top = opStack.pop();
            if (opStack.isEmpty()) {
                stack.smartPush(top);
            }
            else {
                if (opStack.peek().getOperator() != Operator.OPEN_C) {
                    stack.smartPush(top);
                }
                else {
                    ExpressionNode topNode = stack.pop();
                    stack.pop();
                    stack.smartPush(topNode);
                }
            }
        }
        root = stack.pop();
        if (! stack.empty()) throw new SyntaxError("Incorrect expression");
        
        //if (DebugLevel.printExprAsXml()) root.printXML();
        freeVariables = new TreeSet<Variable>();
        boundVariables = new TreeSet<Variable>();
        root.getFreeVariables(freeVariables);
        root.getBoundVariables(boundVariables);
    }
    
    /**
     * Gets the set of free variables of the expression.
     * @return the set of free variables, should not be modified
     */
    public Set<Variable> getFreeVariables() {
        return freeVariables;
    }
    
    /**
     * Gets the set of the variables, values of which can be bound with the expression.
     * @return the set of bound variables, should not be modified
     */
    public Set<Variable> getBoundVariables() {
        return boundVariables;
    }
    
    /**
     * Gets the root node of the expression tree.
     * @return the root
     */
    public ExpressionNode getRoot() {
        return root;
    }
    
    /**
     * Gets the expression type.
     * @return the type
     */
    public Type getType() {
        return root.getExpressionType();
    }
}
