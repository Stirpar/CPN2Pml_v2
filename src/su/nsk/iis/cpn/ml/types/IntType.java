package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;

/**
 * Integer type class.
 * @author stirpar
 */
public class IntType extends Type {

    // =========== begin static ===========
    static String buildId() {
        return "I";
    }

    private static final IntType instance;
    static {
        try {
            instance = new IntType();
        } catch (IdentifierCollision identifierCollision) {
            throw new RuntimeException("SUDDENLY int declarated before instantiation of int type");
        }
    }
    // =========== end static ===========

    /**
     * Constructs the type.
     */
    IntType() throws IdentifierCollision {
        super("int", buildId());
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
        return (that instanceof IntType) || (that instanceof AnyType);
    }

    @Override
    public Type clarify(Type that) {
        if (that == null || !meets(that)) return null;
        return this;
    }
}
