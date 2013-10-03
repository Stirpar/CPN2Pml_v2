package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;

/**
 * Class representing unknown type.
 * It is used if there is no information about the type of elements of list or multiset.
 * @author Alexander Stenenko
 */
public class AnyType extends Type {

    // =========== begin static ===========
    static String buildId() {
        return "?";
    }

    private static final AnyType instance;
    static {
        try {
            instance = new AnyType();
        } catch (IdentifierCollision identifierCollision) {
            throw new RuntimeException("SUDDENLY ? (any) declarated before instantiation of any type");
        }
    }
    // =========== end static ===========


    /**
     * Constructs the type.
     */
    AnyType() throws IdentifierCollision {
        super(null, buildId());
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
        return 0;
    }

    @Override
    public boolean meets(Type that) {
        return true;
    }

    @Override
    public Type clarify(Type that) {
        return that;
    }
}
