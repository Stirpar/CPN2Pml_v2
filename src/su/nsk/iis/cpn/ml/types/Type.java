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
    private static Map<String, Type> typeById = new HashMap<String, Type>();

    static {
        IntType.buildId();
        BoolType.buildId();
        UnitType.buildId();
        AnyType.buildId();
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
        return typeById.get(IntType.buildId());
    }

    public static Type getBoolType() {
        return typeById.get(BoolType.buildId());
    }

    public static Type getUnitType() {
        return typeById.get(UnitType.buildId());
    }

    public static Type getAnyType() {
        return typeById.get(AnyType.buildId());
    }

    public static Type getMsType(Type elementType) {
        try {
            Type res = typeById.get(MsType.buildId(elementType));
            return (res == null) ? new MsType(elementType) : res;
        } catch (IdentifierCollision identifierCollision) {
            throw new RuntimeException("SUDDENLY null identifier is used");
        }
    }

    public static Type getListType(String name, Type elementType) throws IdentifierCollision {
        Type res = typeById.get(ListType.buildId(elementType));
        if (res == null) return new ListType(name, elementType);

        res.registerAlias(name);
        return res;
    }

    public static Type getListType(Type elementType) {
        try {
            return getListType(null, elementType);
        } catch (IdentifierCollision identifierCollision) {
            throw new RuntimeException("SUDDENLY null identifier is used");
        }
    }

    public static Type getProductType(String name, List<Type> types) throws IdentifierCollision {
        Type res = typeById.get(ProductType.buildId(types));
        if (res == null) return new ProductType(name, types);

        res.registerAlias(name);
        return res;
    }

    public static Type getProductType(List<Type> types) {
        try {
            return getProductType(null, types);
        } catch (IdentifierCollision identifierCollision) {
            throw new RuntimeException("SUDDENLY null identifier is used");
        }
    }

    public static Type getRecordType(String name, List<RecordType.Entry> entries) throws IdentifierCollision {
        Type res = typeById.get(RecordType.buildId(entries));
        if (res == null) return new RecordType(name, entries);

        res.registerAlias(name);
        return res;
    }

    public static Type getRecordType(List<RecordType.Entry> entries) {
        try {
            return getRecordType(null, entries);
        } catch (IdentifierCollision identifierCollision) {
            throw new RuntimeException("SUDDENLY null identifier is used");
        }
    }

    public static Type getTimedType(String name, Type innerType) throws IdentifierCollision {
        Type res = typeById.get(TimedType.buildId(innerType));
        if (res == null) return new TimedType(name, innerType);

        res.registerAlias(name);
        return res;
    }

    public static Type getTimedType(Type innerType) {
        try {
            return getTimedType(null, innerType);
        } catch (IdentifierCollision identifierCollision) {
            throw new RuntimeException("SUDDENLY null identifier is used");
        }
    }

    public static Type getEnumType(String name, List<String> elements) throws IdentifierCollision {
        Type res = typeById.get(EnumType.buildId(elements));
        if (res == null) return new EnumType(name, elements);

        res.registerAlias(name);
        return res;
    }

    public static Type getEnumType(List<String> elements) {
        try {
            return getEnumType(null, elements);
        } catch (IdentifierCollision identifierCollision) {
            throw new RuntimeException("SUDDENLY null identifier is used");
        }
    }
    // =========== end static ===========


    private String name;
    private String id;

    /**
     * Constructs the type instance.
     * @param name the name assigned to the type
     * @param id the ID of the type
     */
    Type(String name, String id) throws IdentifierCollision {
        this.name = name;
        this.id = id;
        registerName();
        registerId();
    }

    private void registerName() throws IdentifierCollision {
        registerAlias(name);
    }

    private void registerAlias(String alias) throws IdentifierCollision {
        if (alias == null) return;
        if (name == null) name = alias;
        IdentifierManager.registerIdentifier(alias, IdentifierType.TYPE);
        typeByName.put(alias, this);
    }

    private void registerId() {
        typeById.put(id, this);
    }

    /**
     * Gets the string ID of the type.
     * IDs of two types are different iff the types are different.
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the type.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /* * -----------------if uncommenting this THEN uncomment in MsType class -------------------+/
     * Sets the name of the type.
     * It is possible only if the name was not set before.
     * @return the name
     * /
    public void setName(String name) throws IdentifierCollision {
        if (this.name == null) this.name = name;
        else throw new UnsupportedOperationException("Type named " + name + " (id " + id + ") can't change name.");
        registerName();
    }
    /+ + -----------------if uncommenting this THEN uncomment in MsType class ------------------- */

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


    public abstract Type clarify(Type that);
}
