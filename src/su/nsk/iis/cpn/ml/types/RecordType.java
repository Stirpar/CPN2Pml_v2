package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;
import su.nsk.iis.cpn.ml.IdentifierManager;
import su.nsk.iis.cpn.ml.IdentifierType;
import su.nsk.iis.cpn.ml.TypeError;

import java.util.*;

/**
 * Tuple type class.
 * @author Alexander Stenenko
 */
public class RecordType extends Type {

    // =========== begin static ===========
    static class Entry {
        public final String name;
        public final Type type;

        Entry(Type type, String name) {
            this.name = name;
            this.type = type;
        }
    }
    // =========== end static ===========

    private List<Entry> entries;
    private Map<String, Integer> namesMap;
    private int listDegree = 0;

    /**
     * Constructs the PRODUCT type with the given name and that have elements of the given types.
     * @param entries the list of the record entries
     */
    RecordType(List<Entry> entries) throws IdentifierCollision {
        namesMap = new HashMap<String, Integer>();
        this.entries = entries;
        int i = 0;
        for (Entry entry : entries) {
            IdentifierManager.registerIdentifier(entry.name, IdentifierType.RECORD_FIELD);
            namesMap.put(entry.name, ++i);
            if (entry.type.getListDegree() > listDegree) {
                listDegree = entry.type.getListDegree();
            }
        }
    }

    @Override
    public String getId() {
        StringBuilder res = new StringBuilder("R(");
        for (Iterator<Entry> it = entries.iterator(); it.hasNext(); ) {
            Entry entry = it.next();
            res.append(entry.name);
            res.append(':');
            res.append(entry.type.getId());
            if (it.hasNext()) res.append(',');
        }
        res.append(')');
        return res.toString();
    }

    /**
     * Gets the list of the types of the product elements.
     * @return the list of the types, should not be modified
     */
    public List<Entry> getEntries() {
        return entries;
    }

    public int getFieldNumberByName(String name) {
        return namesMap.get(name); // TODO check
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
        if (that instanceof RecordType) {
            RecordType pt = (RecordType) that;
            if (entries.size() != pt.entries.size()) return false;
            for (int i = 0; i < entries.size(); ++i) {
                if (!entries.get(i).type.meets(pt.entries.get(i).type)) return false;
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
        if (that instanceof RecordType) {
            RecordType thatProduct = (RecordType) that;
            if (entries.size() != thatProduct.entries.size()) throw new TypeError("type error");

            for (int i = 0; i < entries.size(); ++i) {
                entries.get(i).type.clarify(thatProduct.entries.get(i).type);
            }
        }
        else throw new TypeError("type error");
    }

    @Override
    public Type get() {
        List<Entry> gEntries = new LinkedList<Entry>();
        for (int i = 0; i < entries.size(); ++i) {
            Entry entry1 = entries.get(i);
            String entryName = entry1.name;
            Type entryType = entry1.type;
            gEntries.add( new Entry(entryType.get(), entryName) );
        }
        try {
        return getRecordType(gEntries);
        } catch (IdentifierCollision e) {
            throw new RuntimeException();//todo fix
        }
    }
}
