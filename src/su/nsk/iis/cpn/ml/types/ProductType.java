package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;
import su.nsk.iis.cpn.ml.TypeError;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Tuple type class.
 * @author Alexander Stenenko
 */
public class ProductType extends Type {

    private List<Type> types;
    private int listDegree = 0;

    /**
     * Constructs the PRODUCT type with the given name and that have elements of the given types.
     * @param types the list of the types of the elements
     */
    ProductType(List<Type> types) {
        this.types = types;
        for (Type type : types) {
            if (type.getListDegree() > listDegree) {
                listDegree = type.getListDegree();
            }
        }
    }

    @Override
    public String getId() {
        StringBuilder res = new StringBuilder("P(");
        for (Iterator<Type> it = types.iterator(); it.hasNext(); ) {
            Type type = it.next();
            res.append(type.getId());
            if (it.hasNext()) res.append(',');
        }
        res.append(')');
        return res.toString();
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
        if (that instanceof Wildcard) return that.meets(this);
        if (that instanceof ProductType) {
            ProductType pt = (ProductType) that;
            if (types.size() != pt.types.size()) return false;
            for (int i = 0; i < types.size(); ++i) {
                if (! types.get(i).meets(pt.types.get(i))) return false;
            }
            return true;
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
        if (that instanceof ProductType) {
            ProductType thatProduct = (ProductType) that;
            if (types.size() != thatProduct.types.size()) throw new TypeError("type error");

            for (int i = 0; i < types.size(); ++i) {
                types.get(i).clarify(thatProduct.types.get(i));
            }
        }
        else throw new TypeError("type error");
    }

    @Override
    public Type get() {
        List<Type> gTypes = new LinkedList<Type>();
        for (int i = 0; i < types.size(); ++i) {
            gTypes.add(types.get(i).get());
        }
        return getProductType(gTypes);
    }
}
