package su.nsk.iis.cpn.ml;

import su.nsk.iis.cpn.ml.types.Type;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Stenenko
 */
public class Function {

    // =========== begin static ===========
    public static enum Builtin {
        TRUE,
        FALSE,
        ABS,
        MIN,
        MAX,
        HD,
        TL,
        LENGTH,
        NTH,
        NTHTAIL,
        NTHREPLACE,
        RMALL,
        REV,
        NULL,
        EMPTY,
    }

    private static Map<String, List<Function>> functionByName;

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

    private List<Type> argumentTypes;
    private Type type;

    // [ 1 ]
    private Operator operator;
    // [ /1]
// ---------- OR ----------
    // [ 2 ]
    private Builtin builtin;
    // [ /2]
// ---------- OR ----------
    // [ 3]
    private List<Expression> arguments;
    private Expression value;
    // [/3]

    Function(Expression f) {


    }

    public Operator getOperator() {
        return operator;
    }

    Function(Operator operator) {
        this.operator = operator;
        argumentTypes = new LinkedList<Type>();

        switch (operator) {
            case NEG:
            // int -> int
                type = Type.getIntType();
                argumentTypes.add(Type.getIntType());
                break;

            case MUL:
            case DIV:
            case MOD:
            case PLUS:
            case MINUS:
            // int -> int -> int
                type = Type.getIntType();
                argumentTypes.add(Type.getIntType());
                argumentTypes.add(Type.getIntType());
                break;

            case LESS:
            case GREATER:
            case EQ:
            case NEQ:
            case LESSEQ:
            case GREATEREQ:
            // int -> int -> bool
                type = Type.getBoolType();
                argumentTypes.add(Type.getIntType());
                argumentTypes.add(Type.getIntType());
                break;

            case NOT:
            // bool -> bool
                type = Type.getBoolType();
                argumentTypes.add(Type.getBoolType());
                break;

            case AND:
            case OR:
            // bool -> bool -> bool
                type = Type.getBoolType();
                argumentTypes.add(Type.getBoolType());
                argumentTypes.add(Type.getBoolType());
                break;

            case IF:
            // bool -> 'a -> 'a -> 'a
                type = Type.getWildcard();
                argumentTypes.add(Type.getBoolType());
                argumentTypes.add(type);
                argumentTypes.add(type);
                break;

            case NIL:
            // -> list 'a
                type = Type.getListType( Type.getWildcard() );
                break;

            case UNIT:
                // -> unit
                type = Type.getUnitType();
                break;

            case CONS:
            // 'a -> list 'a -> list 'a
            {
                Type elemType = Type.getWildcard();
                Type listType = Type.getListType(elemType);
                type = listType;
                argumentTypes.add(elemType);
                argumentTypes.add(listType);
                break;
            }

            case CONCAT:
            // list 'a -> list 'a -> list 'a
            {
                Type elemType = Type.getWildcard();
                type = Type.getListType(elemType);
                argumentTypes.add(Type.getListType(elemType));
                argumentTypes.add(Type.getListType(elemType));
                break;
            }

            case MS:
            // int -> 'a -> ms 'a
            {
                Type elemType = Type.getWildcard();
                type = Type.getMsType(elemType);
                argumentTypes.add(Type.getIntType());
                argumentTypes.add(elemType);
                break;
            }

            case MSSUM:
            // ms 'a -> ms 'a -> ms 'a
            {
                Type elemType = Type.getWildcard();
                type = Type.getMsType(elemType);
                argumentTypes.add(type);
                argumentTypes.add(type);
                break;
            }

            case TIME:
            // 'a -> int -> timed 'a
            {
                Type innerType = Type.getWildcard();
                type = Type.getTimedType(innerType);
                argumentTypes.add(innerType);
                argumentTypes.add(Type.getIntType());
                break;
            }

            case ADDTIME:
            // timed 'a -> int -> timed 'a
            {
                Type innerType = Type.getWildcard();
                type = Type.getTimedType(innerType);
                argumentTypes.add(type);
                argumentTypes.add(Type.getIntType());
                break;
            }

            default:
                throw new RuntimeException("IMPOSSIBRU");
        }
    }

    public int getArity() {
        return argumentTypes.size();
    }

    public Type getType() {
        return type;
    }

    public List<Type> getArgumentTypes() {
        return argumentTypes;
    }
}
