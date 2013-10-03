package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;

/**
 * List type class.
 * @author stirpar
 */
public class ListType extends Type {

    // =========== begin static ===========
    static String buildId(Type elementType) {
        return "L[" + elementType.getId() + "]";
    }
    // =========== end static ===========


    private Type elementType;
    //private int capacity;

    /**
     * Constructs the LIST type with the given name and that have elements of the given type.
     * @param name the type name, can be null but later must be specified with setName method
     * @param elementType the element type
     */
    ListType(String name, Type elementType) throws IdentifierCollision {
        super(name, buildId(elementType));
        this.elementType = elementType;
    }
    
    /** Gets the element type.
     * @return the type
     */
    public Type getElementType() {
        return elementType;
    }

    @Override
    public boolean isLarge() {
        return true;
    }

    @Override
    public int getValuesCount() {
        throw new RuntimeException("List is large");
    }

    @Override
    public int getListDegree() {
        return elementType.getListDegree() + 1;
    }

    @Override
    public boolean meets(Type that) {
        if (that instanceof ListType) {
            return elementType.meets( ((ListType) that).elementType );
        }
        else return (that instanceof AnyType);
    }

    @Override
    public Type clarify(Type that) {
        if (that == null) return null;
        if (that instanceof AnyType) return this;
        if (that instanceof ListType) {
            Type clarifiedElementType = elementType.clarify( ((ListType)that).elementType );
            return getListType(clarifiedElementType);
        }
        return this;
    }
}
