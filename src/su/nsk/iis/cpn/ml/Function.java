package su.nsk.iis.cpn.ml;

import su.nsk.iis.cpn.ml.types.Type;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexander Stenenko
 */
public class Function {

    // =========== begin static ===========
    public static void declare(Lexer lexer) throws SyntaxError {
        //
        List<Expression> argumentList = new LinkedList<Expression>();

        Lexem nameLexem = lexer.curLexem();
        lexer.nextLexem("(");
        //lexer.getLexems();
        lexer.nextLexem(")");
        lexer.nextLexem("=");
        //Expression def = new Expression(new FnLexer(lexer));


        //TODO
    }

    /*class FnLexer extends Lexer {

        private Lexer lexer;

        FnLexer(Lexer lexer) {
            super();
            this.lexer = lexer;
        }

        @Override
        public Lexem curLexem() {
            Lexem lexem = super.curLexem();
            return "|".equals(lexem.getString()) ? new Lexem("") : lexem;
        }

    }*/
    // =========== end static ===========

    private Type type;
    List<Type> argumentTypes;

    Function(Expression f) {


    }

    public int getArity() {
        return argumentTypes.size();
    }

    public Type getType() {
        // TODO
        return null;
    }
}
