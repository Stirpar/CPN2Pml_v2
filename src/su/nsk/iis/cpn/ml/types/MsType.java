package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;
import su.nsk.iis.cpn.ml.TypeError;

/**
 * Multiset type class.
 * @author stirpar
 */
public class MsType extends Type {

    private Type elementType;
    //private int capacity;

    /**
     * Constructs the multiset type that have elements of the given type.
     * @param elementType the element type
     */
    MsType(Type elementType) {
        this.elementType = elementType;
    }

    @Override
    public String getId() {
        return "M{" + elementType.getId() + "}";
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
        if (that instanceof Wildcard) return that.meets(this);
        if (that instanceof MsType) {
            return elementType.meets( ((MsType) that).elementType );
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
        if (that instanceof MsType) {
            elementType.clarify( ((MsType)that).elementType );
        }
        else throw new TypeError("type error");
    }

    @Override
    public Type get() {
        return getTimedType(elementType.get());
    }
}
