package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;
import su.nsk.iis.cpn.ml.TypeError;

/**
 * List type class.
 * @author stirpar
 */
public class ListType extends Type {

    private Type elementType;
    //private int capacity;

    /**
     * Constructs the LIST type with the given name and that have elements of the given type.
     * @param elementType the element type
     */
    ListType(Type elementType) {
        this.elementType = elementType;
    }

    @Override
    public String getId() {
        return "L[" + elementType.getId() + "]";
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
        if (that instanceof Wildcard) return that.meets(this);
        if (that instanceof ListType) {
            return elementType.meets( ((ListType) that).elementType );
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
        if (that instanceof ListType) {
            elementType.clarify( ((ListType)that).elementType );
        }
        else throw new TypeError("type error");
    }

    @Override
    public Type get() {
        return getTimedType(elementType.get());
    }
}
