package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;

/**
 * Class representing the UNIT type.
 * @author stirpar
 */
public class UnitType extends Type {

    // =========== begin static ===========
    static String buildId() {
        return "U";
    }

    private static final UnitType instance;
    static {
        try {
            instance = new UnitType();
        } catch (IdentifierCollision identifierCollision) {
            throw new RuntimeException("SUDDENLY unit declarated before instantiation of unit type");
        }
    }
    // =========== end static ===========


    /**
     * Constructs the type.
     */
    UnitType() throws IdentifierCollision {
        super("unit", buildId());
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
        return (that instanceof UnitType) || (that instanceof AnyType);
    }

    @Override
    public Type clarify(Type that) {
        if (that == null || !meets(that)) return null;
        return this;
    }
}
