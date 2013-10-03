package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;

public class TimedType extends Type {

    // =========== begin static ===========
    static String buildId(Type innerType) {
        return "T`" + innerType.getId() + "\'";
    }
    // =========== end static ===========


    private Type innerType;

    TimedType(String name, Type innerType) throws IdentifierCollision {
        super(name, buildId(innerType));
        this.innerType = innerType;
    }

    /** Gets the element type.
     * @return the type
     */
    public Type getInnerType() {
        return innerType;
    }

    @Override
    public boolean isLarge() {
        return true;
    }

    @Override
    public int getValuesCount() {
        throw new RuntimeException("Timed type is large");
    }

    @Override
    public int getListDegree() {
        return innerType.getListDegree();
    }

    @Override
    public boolean meets(Type that) {
        if (that instanceof TimedType) {
            return innerType.meets( ((TimedType) that).innerType );
        }
        else return (that instanceof AnyType);
    }

    @Override
    public Type clarify(Type that) {
        if (that == null) return null;
        if (that instanceof AnyType) return this;
        if (that instanceof TimedType) {
            Type clarifiedInnerType = innerType.clarify( ((TimedType)that).innerType );
            return getTimedType(clarifiedInnerType);
        }
        return this;
    }
}
