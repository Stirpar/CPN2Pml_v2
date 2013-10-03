package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;
import su.nsk.iis.cpn.ml.TypeError;

public class TimedType extends Type {

    private Type innerType;

    TimedType(Type innerType) {
        this.innerType = innerType;
    }

    @Override
    public String getId() {
        return "T`" + innerType.getId() + "\'";
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
        if (that instanceof Wildcard) return that.meets(this);
        if (that instanceof TimedType) {
            return innerType.meets( ((TimedType) that).innerType );
        }
        else return false;
    }

    @Override
    public void clarify(Type that) throws TypeError {
        if (that == null) throw new TypeError("type error");
        if (that instanceof Wildcard) {
            if (((Wildcard) that).getRealType() == null) return;
            else that = ((Wildcard) that).getRealType();
        }
        if (that instanceof TimedType) {
            innerType.clarify( ((TimedType)that).innerType );
        }
        else throw new TypeError("type error");
    }

    @Override
    public Type get() {
        return getTimedType(innerType.get());
    }
}
