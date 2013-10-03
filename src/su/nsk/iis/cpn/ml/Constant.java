package su.nsk.iis.cpn.ml;

/**
 * @author Alexander Stenenko
 */
public class Constant extends Variable {

    // =========== begin static ===========
    public static void declare(Lexer lexer) throws SyntaxError, TypeError {
        Lexem nameLexem = lexer.curLexem();
        lexer.nextLexem("=");
        lexer.nextLexem();

        String name = nameLexem.getString();
        Expression value = new Expression(lexer);
        new Constant(name, value);
    }
    // =========== end static ===========

    private Expression value;

    Constant(String name, Expression value) {
        super(name, value.getType());
        this.value = value;
    }
}
