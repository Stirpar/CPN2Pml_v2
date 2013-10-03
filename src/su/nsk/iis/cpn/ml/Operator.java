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
    ABS, MIN, MAX,
    //bool
    NOT,
    AND,
    OR,
    TRUE, FALSE,
    //misc
        TUPLE, LIST,
    //ctl
    //CASE, PAT, // (case expr (pat p1 e1) ... (pat pn en) )
    IF, // (if cond e1 e0)
    LET, LOCAL, VAL, // (let (local (val) ... (val)) expr)
    //typle
    ELEM,
    //list
    CONS, CONCAT, HD, TL, LENGTH, NTH, NTHTAIL, REV, NULL, NTHREPLACE, RMALL,
    //unit
    UNIT,
    MAP, FOLD,
    //ms
    MS, MSSUM, MSSUB, MSMUL,
    EMPTY,
    //util
    open, close, open_sq, close_sq, open_c, close_c, comma,
    then, els, of, in, end, alt, semicolon, nil,
    TIME, ADDTIME,
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
