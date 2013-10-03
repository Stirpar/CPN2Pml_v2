package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;

/**
 * Class representing the BOOL type.
 * @author stirpar
 */
public class BoolType extends Type {

    // =========== begin static ===========
    static String buildId() {
        return "B";
    }

    private static BoolType instance;
    static {
        try {
            instance = new BoolType();
        } catch (IdentifierCollision identifierCollision) {
            throw new RuntimeException("SUDDENLY bool declarated before instantiation of bool type");
        }
    }
    // =========== end static ===========


    /**
     * Constructs the type.
     */
    BoolType() throws IdentifierCollision {
        super("bool", buildId());
    }

    @Override
    public boolean isLarge() {
        return false;
    }

    @Override
    public int getValuesCount() {
        return 2;
    }

    @Override
    public int getListDegree() {
        return 0;
    }

    @Override
    public boolean meets(Type that) {
        return (that instanceof BoolType) || (that instanceof AnyType);
    }

    @Override
    public Type clarify(Type that) {
        if (that == null || !meets(that)) return null;
        return this;
    }
}
