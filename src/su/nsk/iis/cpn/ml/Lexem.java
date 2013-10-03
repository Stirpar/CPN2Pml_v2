package su.nsk.iis.cpn.ml;

import java.util.HashSet;
import java.util.Set;

/**
 * Class representing a lexem of CPN ML language.
 * @author stirpar
 */
public class Lexem {

    // =========== begin static ===========
    private final static Set<String> ops;

    static {
        ops = new HashSet<String>();
        String[] keywords = {
                "div", "mod",
                "abs", "min", "max",
                "not", "andalso","orelse",
                //"true", "false",
                "hd", "tl",
                "length",
                "nth", "nthtail", "nthreplace",
                "rmall", "rev", "null", "nil",
                "if", "then", "else",
                "case", "of",
                "let", "in",
                "val", "end",
                "fun",
                "empty",
        };

        for (int i = 0; i < keywords.length; ++i) {
            ops.add(keywords[i]);
        }
    }
    // =========== end static ===========

    private String s;
    private LexemType type;
    
    /**
     * Constructs a lexem of the given string
     * @param s the given string
     */
    public Lexem(String s) {
        this.s = s;
        if (s.equals("")) type = LexemType.EOF;
        else {
            char c = s.charAt(0);
            if (c >= '0' && c <= '9') type = LexemType.DECIMAL_LITERAL;
            else if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_' || c == '\'') {
                if (ops.contains(s)) type = LexemType.OPERATOR;
                else {
                    if (Variable.getVariableByName(s) == null) {
                        type = LexemType.IDENTIFIER; // ?
                    }
                    else {
                        type = LexemType.VARIABLE;
                    }
                }
            }
            else type = LexemType.OPERATOR;
        }
    }
    
    /**
     * Gets the string representation of the lexem.
     * @return the lexem string representation
     */
    public String getString() {
        return s;
    }
    
    /**
     * Gets the type of the lexem.
     * @return the lexem type
     */
    public LexemType getType() {
        return type;
    }
    
}
