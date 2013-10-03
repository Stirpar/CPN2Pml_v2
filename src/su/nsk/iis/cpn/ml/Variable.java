package su.nsk.iis.cpn.ml;

import su.nsk.iis.cpn.ml.types.Type;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Class representing a variable of CPN ML language.
 * @author stirpar
 */
public class Variable implements Comparable<Variable> {

    // =========== begin static ===========
    private static Map<String, Variable> variableMap = new HashMap<String, Variable>();

    /**
     * Gets the variable with the given name.
     * @param name the name
     * @return the variable if it was defined or null otherwise
     */
    public static Variable getVariableByName(String name) {
        return variableMap.get(name);
    }

    /**
     * Defines variable with given name and type.
     * @param name the name
     * @param type the type.
     */
    private static void defineVariable(String name, Type type) throws IdentifierCollision {
        IdentifierManager.registerIdentifier(name, IdentifierType.VARIABLE);
        //if (variableMap.get(name) != null) throw new RuntimeException("Variable \'"+ name + "\' redeclaration");
        variableMap.put(name, new Variable(name, type));
    }

    public static void declare(Lexer lexer) throws SyntaxError, IdentifierCollision {
        List<Lexem> nameLexems = lexer.getLexems(",");
        if (! lexer.curLexemEquals(":")) throw new SyntaxError("Missed \':\' in variable declaration");
        Lexem typeNameLexem = lexer.nextLexem();
        String typeName = typeNameLexem.getString();
        Type type = Type.getTypeByName(typeName);

        for (Lexem nameLexem : nameLexems) {
            String name = nameLexem.getString();
            defineVariable(name, type);
        }
    }
    // =========== end static ===========

    private Type type;
    private String name;
    
    /**
     * Constructs the variable with the given name and type.
     * @param name the name
     * @param type the type
     */
    Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }
    
    /**
     * Gets the variable name.
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the variable type.
     * @return the type
     */
    public Type getType() {
        return type;
    }
    
    /**
     * Gets the name of the variable type.
     * @return the name of the type
     */
    public String getTypeName() {
        return type.getName();
    }

    @Override
    public int compareTo(Variable that) {
        return name.compareTo(that.name);
    }
    
}
