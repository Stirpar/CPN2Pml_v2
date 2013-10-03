package su.nsk.iis.cpn.ml;

/**
 * Enumeration of operators of CPN ML language.
 * @author stirpar
 */
public enum Operator {

    //int
    NEG,
    MUL, DIV, MOD,
    PLUS, MINUS,
    LESS, GREATER, EQ, NEQ, LESSEQ, GREATEREQ,

    //bool
    NOT,
    AND,
    OR,

    //misc
    TUPLE, LIST,
    //ctrl
    IF, THEN, ELSE,

    //tuple
    ELEM,

    //list
    NIL, CONS, CONCAT,

    //unit
    UNIT,

    //ms
    MS,
    MSSUM, //, MSSUB, MSMUL,
    EMPTY,

    // time
    TIME, ADDTIME,

    //util
    OPEN, CLOSE, OPEN_SQ, CLOSE_SQ, OPEN_C, CLOSE_C, COMMA,
    ;
    
    /**
     * Gets the Promela equivalent of the CPN ML operator.
     * @return the string with Promela operator
     */
    public String cEquivalent() {
        switch (this) {
            case PLUS: return "+";
            case MINUS: return "-";
            case MUL: return "*";
            case DIV: return "/";
            case MOD: return "%";
            case EQ: return "==";
            case NEQ: return "!=";
            case LESS: return "<";
            case GREATER: return ">";
            case LESSEQ: return "<=";
            case GREATEREQ: return ">=";
            case AND: return "&&";
            case OR: return "||";
            
            case NEG: return "-";
            case NOT: return "!";
                
            default: throw new RuntimeException("No promela equivalen for operator " + this);
        }
    }
    
}
