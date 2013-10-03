package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;

/**
 * Multiset type class.
 * @author stirpar
 */
public class MsType extends Type {

    // =========== begin static ===========
    static String buildId(Type elementType) {
        return "M{" + elementType.getId() + "}";
    }
    // =========== end static ===========


    private Type elementType;
    //private int capacity;

    /**
     * Constructs the multiset type that have elements of the given type.
     * @param elementType the element type
     */
    MsType(Type elementType) throws IdentifierCollision {
        super(null, buildId(elementType));
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
        throw new UnsupportedOperationException("Don't ask me!");
    }

    @Override
    public int getValuesCount() {
        throw new UnsupportedOperationException("Don't ask me!");
    }

    @Override
    public int getListDegree() {
        return elementType.getListDegree();
    }

    @Override
    public boolean meets(Type that) {
        if (that instanceof MsType) {
            return elementType.meets( ((MsType) that).elementType );
        }
        else return (that instanceof AnyType);
    }

    @Override
    public Type clarify(Type that) {
        if (that == null) return null;
        if (that instanceof AnyType) return this;
        if (that instanceof MsType) {
            Type clarifiedElementType = elementType.clarify( ((MsType)that).elementType );
            return getMsType(clarifiedElementType);
        }
        return this;
    }
}
