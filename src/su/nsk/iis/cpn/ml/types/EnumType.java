package su.nsk.iis.cpn.ml.types;

import su.nsk.iis.cpn.ml.IdentifierCollision;
import su.nsk.iis.cpn.ml.IdentifierManager;
import su.nsk.iis.cpn.ml.IdentifierType;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

/**
 * Enumeration type class.
 * @author stirpar
 */
public class EnumType extends Type {

    // =========== begin static ===========
    private static Map<String, EnumType> typeOfElement;
    
    static {
        typeOfElement = new HashMap<String, EnumType>();
    }

    /**
     * Gets the type of the given element identifier
     * @param element the element identifier
     * @return the type if the identifier is of some type, null otherwise
     */
    public static EnumType getElementType(String element) {
        return typeOfElement.get(element);
    }

    static String buildId(List<String> elements) {
        StringBuilder res = new StringBuilder("E<");
        for (Iterator<String> it = elements.iterator(); it.hasNext(); ) {
            res.append(it.next());
            if (it.hasNext()) res.append(',');
        }
        res.append('>');
        return res.toString();
    }
    // =========== end static ===========


    private List<String> elements;
    private Map<String, Integer> elements_rev;

    /**
     * Constructs the ENUM type with the given name and elements.
     * @param name the name
     * @param elements the list of the elements
     */
    EnumType(String name, List<String> elements) throws IdentifierCollision {
        super(name, buildId(elements));
        this.elements = elements;
        elements_rev = new HashMap<String, Integer>();
        int i = 0;
        for (String elementName : elements) {
            IdentifierManager.registerIdentifier(elementName, IdentifierType.ENUM_ELEMENT); // enum element is the same as constant?
            typeOfElement.put(elementName, this);
            elements_rev.put(elementName, i++);
        }
    }
    
    /**
     * Gets the list of element identifiers.
     * The order of identifiers in the list corresponds their integer values.
     * @return the list of identifiers
     */
    public List<String> getElements() {
        return elements;
    }
    
    /**
     * Gets the mapping from element identifiers into their integer values.
     * @return the map
     */
    public Map<String, Integer> getElements_rev() {
        return elements_rev;
    }

    @Override
    public boolean isLarge() {
        return false;
    }

    @Override
    public int getValuesCount() {
        return elements.size();
    }

    @Override
    public int getListDegree() {
        return 0;
    }

    @Override
    public boolean meets(Type that) {
        if (that instanceof AnyType) return true;
        return (this.equals(that)); // is it right?
    }

    @Override
    public Type clarify(Type that) {
        if (that == null || !meets(that)) return null;
        return this;
    }
}
