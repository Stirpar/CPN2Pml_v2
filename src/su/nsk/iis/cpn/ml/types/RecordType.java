package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;
import su.nsk.iis.cpn.ml.IdentifierManager;
import su.nsk.iis.cpn.ml.IdentifierType;

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

    static String buildId(List<Entry> entries) {
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
    // =========== end static ===========

    private List<Entry> entries;
    private Map<String, Integer> namesMap;
    private int listDegree = 0;

    /**
     * Constructs the PRODUCT type with the given name and that have elements of the given types.
     * @param name the type name, can be null but later must be specified with setName method
     * @param entries the list of the record entries
     */
    RecordType(String name, List<Entry> entries) throws IdentifierCollision {
        super(name, buildId(entries));
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
        if (that instanceof RecordType) {
            RecordType pt = (RecordType) that;
            if (entries.size() != pt.entries.size()) return false;
            for (int i = 0; i < entries.size(); ++i) {
                if (!entries.get(i).type.meets(pt.entries.get(i).type)) return false;
            }
            return true;
        }
        else return (that instanceof AnyType);
    }


    @Override
    public Type clarify(Type that) {
        if (that == null) return null;
        if (that instanceof AnyType) return this;
        if (that instanceof RecordType) {
            RecordType thatRecord = (RecordType) that;
            if (entries.size() != thatRecord.entries.size()) return null;

            List<Entry> clarifiedEntries = new LinkedList<Entry>();
            for (int i = 0; i < entries.size(); ++i) {
                Entry entry1 = entries.get(i);
                Entry entry2 = thatRecord.entries.get(i);
                if (! entry1.name.equals(entry2.name)) return null;
                String entryName = entry1.name;
                Type entryType = entry1.type.clarify(entry2.type);
                clarifiedEntries.add( new Entry(entryType, entryName) );
            }
            return getRecordType(clarifiedEntries);
        }
        return this;
    }
}
