package su.nsk.iis.cpn.ml;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Stenenko
 */
public class IdentifierManager {

    // =========== begin static ===========
    static Map<String, IdentifierType> identifiers = new HashMap<String, IdentifierType>();

    public static void registerIdentifier(String identifier, IdentifierType type) throws IdentifierCollision {
        if (identifier == null) return; // ok?

        if (identifiers.get(identifier) != null) throw new IdentifierCollision(identifier, type, identifiers.get(identifier));
        identifiers.put(identifier, type);
    }

    public static IdentifierType getIdentifierType(String identifier) {
        return identifiers.get(identifier);
    }
    // =========== end static ===========

}
