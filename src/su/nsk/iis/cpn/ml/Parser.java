package su.nsk.iis.cpn.ml;

import su.nsk.iis.cpn.ml.types.Type;

public class Parser {

    // =========== begin static ===========
    public static void parseDeclaration(String declaration) throws SyntaxError, IdentifierCollision, Warning {
        Lexer lexer = new Lexer(declaration);
        Lexem firstLexem = lexer.curLexem();
        lexer.nextLexem();

        String firstLexemString = firstLexem.getString();

             if (firstLexemString.equals("colset")) Type.declare(lexer);
        else if (firstLexemString.equals("var"))    Variable.declare(lexer);
        else if (firstLexemString.equals("val"))    Constant.declare(lexer);
        else if (firstLexemString.equals("fun"))    Function.declare(lexer);
        else throw new Warning("Declaration: \'" + declaration + "\' was ignored");
    }

    public static Expression getParsedExpression(String declaration) throws SyntaxError {
        Lexer lexer = new Lexer(declaration);
        return new Expression(lexer);
    }
    // =========== end static ===========
}
