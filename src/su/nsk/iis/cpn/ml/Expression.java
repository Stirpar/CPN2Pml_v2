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
        operators.put(",", Operator.comma);
        operators.put("(", Operator.open);
        operators.put(")", Operator.close);
        operators.put("[", Operator.open_sq);
        operators.put("]", Operator.close_sq);
        operators.put("{", Operator.open_c);
        operators.put("}", Operator.close_c);
        
        operators.put("abs", Operator.ABS);
        operators.put("min", Operator.MIN);
        operators.put("max", Operator.MAX);
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
        operators.put("then", Operator.then);
        operators.put("else", Operator.els);
        operators.put("nil", Operator.nil);
        operators.put("empty", Operator.EMPTY);
        operators.put("true", Operator.TRUE);
        operators.put("false", Operator.FALSE);
        operators.put("#", Operator.ELEM);
        operators.put("::", Operator.CONS);
        operators.put("^^", Operator.CONCAT);
        operators.put("rev", Operator.REV);
        operators.put("length", Operator.LENGTH);
        operators.put("hd", Operator.HD);
        operators.put("tl", Operator.TL);
        operators.put("nth", Operator.NTH);
        operators.put("nthtail", Operator.NTHTAIL);
        operators.put("nthreplace", Operator.NTHREPLACE);
        operators.put("rmall", Operator.RMALL);
        operators.put("null", Operator.NULL);
        operators.put("`", Operator.MS);
        operators.put("**", Operator.MSMUL);
        operators.put("++", Operator.MSSUM);
        operators.put("--", Operator.MSSUB);
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
        priority.put(Operator.MSMUL, i);
        ++i;
        priority.put(Operator.MSSUM, i);
        priority.put(Operator.MSSUB, i);
        ++i;
        priority.put(Operator.CONS, i);
        ++i;
        priority.put(Operator.IF, i);
        priority.put(Operator.then, i);
        priority.put(Operator.els, i);
        ++i;
        priority.put(Operator.open, i);
        priority.put(Operator.open_sq, i);
        priority.put(Operator.open_c, i);
        ++i;
        priority.put(Operator.TIME, i); // ?
        priority.put(Operator.ADDTIME, i); // ?


        arity.put(Operator.ABS, 1);
        arity.put(Operator.MIN, 2);
        arity.put(Operator.MAX, 2);
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
        arity.put(Operator.REV, 1);
        arity.put(Operator.LENGTH, 1);
        arity.put(Operator.HD, 1);
        arity.put(Operator.TL, 1);
        arity.put(Operator.NTH, 2);
        arity.put(Operator.NTHTAIL, 2);
        arity.put(Operator.NTHREPLACE, 3);
        arity.put(Operator.RMALL, 3);
        arity.put(Operator.NULL, 1);
        arity.put(Operator.MS, 2);
        arity.put(Operator.MSMUL, 2);
        arity.put(Operator.MSSUM, 2);
        arity.put(Operator.MSSUB, 2);
        arity.put(Operator.EMPTY, 0);
        arity.put(Operator.UNIT, 0);
        arity.put(Operator.TRUE, 0);
        arity.put(Operator.FALSE, 0);
        arity.put(Operator.TIME, 2);
        arity.put(Operator.ADDTIME, 2);


        fixity.put(Operator.ABS, Fixity.PREFIX);
        fixity.put(Operator.MIN, Fixity.PREFIX);
        fixity.put(Operator.MAX, Fixity.PREFIX);
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
        fixity.put(Operator.REV, Fixity.PREFIX);
        fixity.put(Operator.LENGTH, Fixity.PREFIX);
        fixity.put(Operator.HD, Fixity.PREFIX);
        fixity.put(Operator.TL, Fixity.PREFIX);
        fixity.put(Operator.NTH, Fixity.PREFIX);
        fixity.put(Operator.NTHTAIL, Fixity.PREFIX);
        fixity.put(Operator.NTHREPLACE, Fixity.PREFIX);
        fixity.put(Operator.RMALL, Fixity.PREFIX);
        fixity.put(Operator.NULL, Fixity.PREFIX);
        fixity.put(Operator.MS, Fixity.INFIX_LTR);
        fixity.put(Operator.MSMUL, Fixity.INFIX_LTR);
        fixity.put(Operator.MSSUM, Fixity.INFIX_LTR);
        fixity.put(Operator.MSSUB, Fixity.INFIX_LTR);
        
        fixity.put(Operator.EMPTY, Fixity.PREFIX);
        fixity.put(Operator.UNIT, Fixity.PREFIX);
        fixity.put(Operator.TRUE, Fixity.PREFIX);
        fixity.put(Operator.FALSE, Fixity.PREFIX);

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
         * If the given node is an operator then pops from the stack as many nodes from the stack,
         * as there must be operator's arguments and adds them to the node as children node;
         * pushes the given node to the top of stack.
         * @param node the node
         * @return the pushed node
         * @throws su.nsk.iis.cpn.ml.SyntaxError if syntax error occures
         */
        public ExpressionNode smartPush(ExpressionNode node) throws SyntaxError {
            if (node.isOperator()) {
                if (node.getOperator() == Operator.IF) {
                    if (node.getValue() != 3) throw new SyntaxError("Incorrect IF-THEN-ELSE statement");
                }

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
    private void pushedArg(SmartStack stack, Stack <ExpressionNode> opStack) throws SyntaxError {
        if (! opStack.empty()) {
            ExpressionNode top = opStack.peek();
            Operator op = top.getOperator();
            if (fixity.get(op) == Fixity.PREFIX) {
                opStack.pop();
                int v = top.getValue() + 1;
                top = new ExpressionNode(op, v);
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
    public Expression(Lexer lexer) throws SyntaxError {
        SmartStack stack = new SmartStack();
        Stack <ExpressionNode> opStack = new Stack<ExpressionNode>();
        Lexem prevLexem = null;
        for (Lexem lexem = lexer.curLexem(); lexem.getType() != LexemType.EOF; lexem = lexer.nextLexem()) {
            switch (lexem.getType()) {
                case OPERATOR:
                    Operator op = operators.get(lexem.getString());
                    int argsNum;
                    switch (op) {
                        case nil:
                            stack.smartPush( new ExpressionNode(Operator.LIST, 0) );
                            pushedArg(stack, opStack);
                            break;
                        case EMPTY:
                            stack.smartPush( new ExpressionNode(op, arity.get(op)) );
                            pushedArg(stack, opStack);
                            break;
                        case IF:
                            if (! opStack.empty()) {
                                switch (opStack.peek().getOperator()) {
                                    case open:
                                    case open_sq:
                                    case open_c:
                                        break;
                                    default:
                                        throw new SyntaxError("IF-THEN-ELSE statement must be in brackets");
                                }
                            }
                            opStack.push( new ExpressionNode(op, 1) );
                            break;
                        case then:
                        case els:
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
                            if (op == Operator.then && argsNum != 2 || op == Operator.els && argsNum != 3) {
                                throw new SyntaxError("Incorrect IF-THEN-ELSE statement");
                            }
                            opStack.push( new ExpressionNode(Operator.IF, argsNum) );
                            break;
                        case open:
                        case open_sq:
                        case open_c:
                            opStack.push( new ExpressionNode(op, 0) );
                            break;
                        case comma:
                        case close:
                        case close_sq:
                        case close_c:
                            // <>
                            if (prevLexem.getString().equals("[") && op == Operator.close_sq) {
                                opStack.pop();
                                stack.smartPush(new ExpressionNode(Operator.LIST, 0));
                                pushedArg(stack, opStack);
                                break;
                            }
                            if (prevLexem.getString().equals("(") && op == Operator.close) {
                                opStack.pop();
                                stack.smartPush(new ExpressionNode(Operator.UNIT, 0));
                                pushedArg(stack, opStack);
                                break;
                            }
                            // </>
                            
                            while (! opStack.empty()) {
                                ExpressionNode top = opStack.pop();
                                if (top.getOperator() == Operator.open || top.getOperator() == Operator.open_sq || top.getOperator() == Operator.open_c) {
                                    opStack.push(top);
                                    break;
                                }
                                if (opStack.peek().getOperator() != Operator.open_c) {
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
                            if (op == Operator.comma) {
                                opStack.push( new ExpressionNode(op2, argsNum) );
                            }
                            else {
                                if (op2 == Operator.open && op != Operator.close || op2 == Operator.open_sq && op != Operator.close_sq || op2 == Operator.open_c && op != Operator.close_c) {
                                    throw new SyntaxError("Incorrect brackets");
                                }
                                
                                if (op == Operator.close && argsNum > 1) {
                                    stack.smartPush(new ExpressionNode(Operator.TUPLE, argsNum));
                                }
                                else if (op == Operator.close_sq) {
                                    stack.smartPush(new ExpressionNode(Operator.LIST, argsNum));
                                }
                                else if (op == Operator.close_c) {
                                    stack.smartPush(new ExpressionNode(Operator.TUPLE, argsNum));
                                }
                                pushedArg(stack, opStack);
                            }
                            break;
                        default: // other operators
                            int curPriority;
                            switch (fixity.get(op)) {
                                case PREFIX:
                                    opStack.push( new ExpressionNode(op, 0) );
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
                                    opStack.push( new ExpressionNode(op, arity.get(op)) );
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
                                    opStack.push( new ExpressionNode(op, arity.get(op)) );
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
                case IDENTIFIER:
                    stack.smartPush( new ExpressionNode( lexem.getString() ) );
                    pushedArg(stack, opStack);
                    break;
                case DECIMAL_LITERAL:
                    //
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
                if (opStack.peek().getOperator() != Operator.open_c) {
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
