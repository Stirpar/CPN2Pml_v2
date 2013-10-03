package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Tuple type class.
 * @author Alexander Stenenko
 */
public class ProductType extends Type {

    // =========== begin static ===========
    static String buildId(List<Type> types) {
        StringBuilder res = new StringBuilder("P(");
        for (Iterator<Type> it = types.iterator(); it.hasNext(); ) {
            Type type = it.next();
            res.append(type.getId());
            if (it.hasNext()) res.append(',');
        }
        res.append(')');
        return res.toString();
    }
    // =========== end static ===========

    private List<Type> types;
    private int listDegree = 0;

    /**
     * Constructs the PRODUCT type with the given name and that have elements of the given types.
     * @param name the type name, can be null but later must be specified with setName method
     * @param types the list of the types of the elements
     */
    ProductType(String name, List<Type> types) throws IdentifierCollision {
        super(name, buildId(types));
        this.types = types;
        for (Type type : types) {
            if (type.getListDegree() > listDegree) {
                listDegree = type.getListDegree();
            }
        }
    }

    /**
     * Gets the list of the types of the product elements.
     * @return the list of the types, should not be modified
     */
    public List<Type> getTypes() {
        return types;
    }

    @Override
    public boolean isLarge() {
        return true;
    }

    @Override
    public int getValuesCount() {
        throw new RuntimeException("Product type seems to be large");
    }

    @Override
    public int getListDegree() {
        return listDegree;
    }

    @Override
    public boolean meets(Type that) {
        if (that instanceof ProductType) {
            ProductType pt = (ProductType) that;
            if (types.size() != pt.types.size()) return false;
            for (int i = 0; i < types.size(); ++i) {
                if (! types.get(i).meets(pt.types.get(i))) return false;
            }
            return true;
        }
        else return (that instanceof AnyType);
    }


    @Override
    public Type clarify(Type that) {
        if (that == null) return null;
        if (that instanceof AnyType) return this;
        if (that instanceof ProductType) {
            ProductType thatProduct = (ProductType) that;
            if (types.size() != thatProduct.types.size()) return null;

            List<Type> clarifiedTypes = new LinkedList<Type>();
            for (int i = 0; i < types.size(); ++i) {
                clarifiedTypes.add( types.get(i).clarify(thatProduct.types.get(i)) );
            }
            return getProductType(clarifiedTypes);
        }
        return this;
    }
}
