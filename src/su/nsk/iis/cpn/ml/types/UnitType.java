package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;
import su.nsk.iis.cpn.ml.TypeError;

/**
 * Class representing the UNIT type.
 * @author stirpar
 */
public class UnitType extends Type {

    @Override
    public String getId() {
        return "unit";
    }

    @Override
    public boolean isLarge() {
        return false;
    }

    @Override
    public int getValuesCount() {
        return 1;
    }

    @Override
    public int getListDegree() {
        return 0;
    }

    @Override
    public boolean meets(Type that) {
        if (that instanceof Wildcard) return that.meets(this);
        return (that instanceof UnitType);
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
