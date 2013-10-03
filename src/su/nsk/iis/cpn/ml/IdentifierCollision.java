package su.nsk.iis.cpn.ml;

/**
 * @author Alexander Stenenko
 */
public class IdentifierCollision extends Exception {
    public IdentifierCollision(String identifier, IdentifierType type1, IdentifierType type2) {
        super("Identifier \'" + identifier + "\' redeclaration");
    }
}
