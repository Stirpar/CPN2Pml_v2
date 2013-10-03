package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;
import su.nsk.iis.cpn.ml.TypeError;

/**
 * Integer type class.
 * @author stirpar
 */
public class IntType extends Type {

    @Override
    public String getId() {
        return "int";
    }

    @Override
    public boolean isLarge() {
        return true;
    }

    @Override
    public int getValuesCount() {
        throw new RuntimeException("Int is large");
    }

    @Override
    public int getListDegree() {
        return 0;
    }

    @Override
    public boolean meets(Type that) {
        if (that instanceof Wildcard) return that.meets(this);
        return (that instanceof IntType);
    }

    @Override
    public void clarify(Type that) throws TypeError {
        if ((that == null) || !meets(that)) throw new TypeError("type error");
    }

    @Override
    public Type get() {
        return this;
    }
}
