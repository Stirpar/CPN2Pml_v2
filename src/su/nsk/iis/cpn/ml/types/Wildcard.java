package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;
import su.nsk.iis.cpn.ml.TypeError;

/**
 * Class representing unknown type.
 * It is used if there is no information about the type of elements of list or multiset.
 * @author Alexander Stenenko
 */
public class Wildcard extends Type {

    // =========== begin static ===========
    static int cnt = 0;
    // =========== end static ===========


    int id;
    Type realType;

    public Wildcard() {
        this.id = ++cnt;
        this.realType = null;
    }

    public void setType(Type type) {
        this.realType = type;
    }

    public Type getRealType() {
        return realType;
    }

    public int getWildcardId() {
        return id;
    }

    @Override
    public String getId() {
        if (realType == null) return "*" + id;
        else return realType.getId();
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
        if (realType == null) return true;
        else return realType.meets(that);
    }

    @Override
    public void clarify(Type that) throws TypeError {
        if (realType == null) realType = that;
        else realType.clarify(that);
    }

    @Override
    public Type get() {
        if (realType == null) throw new RuntimeException("Type is still not clear");//todo replace RT exception
        return realType;
    }
}
