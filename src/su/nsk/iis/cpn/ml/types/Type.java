package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Alexander Stenenko
 */
public abstract class Type implements Comparable<Type> {

    // =========== begin static ===========
    private static Map<String, Type> typeByName = new HashMap<String, Type>();
    private static Map<String, String> nameById = new HashMap<String, String>();

    private static Type intTypeInstance = new IntType();
    private static Type boolTypeInstance = new BoolType();
    private static Type unitTypeInstance = new UnitType();

    static {
        typeByName.put("int", intTypeInstance);
        typeByName.put("bool", boolTypeInstance);
        typeByName.put("unit", unitTypeInstance);

    }

    public static Type getTypeByName(String name) {
        return typeByName.get(name);
    }

    public static void declare(Lexer lexer) throws SyntaxError, IdentifierCollision, Warning {
        Lexem nameLexem = lexer.curLexem();
        lexer.nextLexem("=");
        Lexem defLexem = lexer.nextLexem();
        lexer.nextLexem();

        String name = nameLexem.getString();
        String def = defLexem.getString();
        Type declaredType = null;

        if (typeByName.get(def) != null) declaredType = typeByName.get(def);
        else if (def.equals("list")) {
            Lexem elementTypeLexem = lexer.curLexem();
            lexer.nextLexem();

            String elementTypeName = elementTypeLexem.getString();
            Type elementType = typeByName.get(elementTypeName);
            if (elementType == null) throw new Warning("Undefined list element type \'" + elementTypeName + "\'");
            declaredType = getListType(elementType);
        }
        else if (def.equals("with")) {
            List<String> elements = new LinkedList<String>();
            List<Lexem> elementLexems = lexer.getLexems("|");
            for (Lexem elementLexem : elementLexems) {
                String elementName = elementLexem.getString();
                elements.add(elementName);
            }
            declaredType = getEnumType(elements);
        }
        else if (def.equals("product")) {
            List<Type> types = new LinkedList<Type>();
            List<Lexem> elementTypeLexems = lexer.getLexems("*");
            for (Lexem elementTypeLexem : elementTypeLexems) {
                String elementTypeName = elementTypeLexem.getString();
                Type elementType = typeByName.get(elementTypeName);
                if (elementType == null) throw new Warning("Undefined product field type \'" + elementTypeName + "\'");

                types.add(elementType);
            }
            declaredType = getProductType(types);
        }
        else if (def.equals("record")) {
            List<RecordType.Entry> entries = new LinkedList<RecordType.Entry>();

            while (lexer.curLexem().getType() != LexemType.EOF) {
                Lexem entryNameLexem = lexer.curLexem();
                lexer.nextLexem(":");
                Lexem entryTypeNameLexem = lexer.nextLexem();

                String entryName = entryNameLexem.getString();
                String entryTypeName = entryTypeNameLexem.getString();
                Type entryType = typeByName.get(entryTypeName);
                if (entryType == null) throw new Warning("Undefined record field type \'" + entryTypeName + "\'");

                RecordType.Entry entry = new RecordType.Entry(entryType, entryName);
                entries.add(entry);

                lexer.nextLexem();
                if (! lexer.curLexemEquals("*")) break;
                lexer.nextLexem();
            }
            declaredType = getRecordType(entries);
        }

        if (lexer.curLexemEquals("timed")) {
            lexer.nextLexem();
            declaredType = getTimedType(declaredType);
        }

        if (declaredType == null) throw new Warning("Type \'" + name + "\' declaration was ignored due to unsupported type construction");
        if (lexer.curLexem().getType() != LexemType.EOF) throw new SyntaxError(""); // todo make lexer generate SyntaxError exceptions
System.out.println(declaredType.getId());
        declaredType.registerAlias(name);
    }

    public static Type getIntType() {
        return intTypeInstance;
    }

    public static Type getBoolType() {
        return boolTypeInstance;
    }

    public static Type getUnitType() {
        return unitTypeInstance;
    }

    public static Type getWildcard() {
        return new Wildcard();
    }

    public static Type getMsType(Type elementType) {
        return new MsType(elementType);
    }

    public static Type getListType(Type elementType) {
        return new ListType(elementType);
    }

    public static Type getProductType(List<Type> types) {
        return new ProductType(types);
    }

    public static Type getRecordType(List<RecordType.Entry> entries) throws IdentifierCollision {
        return new RecordType(entries);
    }

    public static Type getTimedType(Type innerType) {
        return new TimedType(innerType);
    }

    public static Type getEnumType(List<String> elements) throws IdentifierCollision {
        return new EnumType(elements);
    }
    // =========== end static ===========


    private void registerAlias(String alias) throws IdentifierCollision {
        if (alias == null) return;
        String id = getId();
        if (nameById.get(id) == null) {
            nameById.put(id, alias);
        }
        IdentifierManager.registerIdentifier(alias, IdentifierType.TYPE);
        typeByName.put(alias, this);
    }

    /**
     * Gets the string ID of the type.
     * IDs of two types are different iff the types are different.
     * @return the id
     */
    public abstract String getId();

    public String getName() {
        String id = getId();
        String name = nameById.get(id);
        return (name == null) ? "_id__" + id : name;
    }

    @Override
    public int compareTo(Type that) {
        return getId().compareTo(that.getId());
    }

    /**
     * Checks if the type domain has large number of values.
     * @return true if type domain has large number of values, false otherwise
     */
    public abstract boolean isLarge();

    /**
     * If the type domain has not large number of values, THEN gets that number.
     * @return the number of values of the type domain
     */
    public abstract int getValuesCount();

    /**
     * Gets the list degree of the type.
     * List degree of the type is maximum number of nested lists in this type.
     * I.e. type "list (list int)" has list degree = 2,
     *           "list (product (list (list int)) * (list (list (list int))))" has list degree = 4,
     *           "int" has list degree = 0.
     * @return the list degree
     */
    public abstract int getListDegree();

    /**
     * Checks if the type meets the given type.
     * I.e. value of the given type can pass as value of this type.
     * @param that the type
     * @return true if the types meet, false otherwise
     */
    public abstract boolean meets(Type that);

    public abstract void clarify(Type that) throws TypeError;

    public abstract Type get();
}
