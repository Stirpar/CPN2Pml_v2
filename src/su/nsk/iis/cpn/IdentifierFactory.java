package su.nsk.iis.cpn;

/**
 * Class representing a factory producing unique identifiers.
 * Identifiers are described as the following regular expression: [a-zA-Z_][a-zA-Z_0-9]*
 * @author Alexander Stenenko
 */
class IdentifierFactory {
    
    private String prefix;
    private int cnt = 0;
    
    /**
     * Construct the factory producing identifiers starting with the given prefix.
     * @param prefix the prefix
     */
    public IdentifierFactory(String prefix) {
        // check if the prefix is valid
        char c = prefix.charAt(0);
        if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != '_') {
            throw new RuntimeException("Identifier prefix \'" + prefix + "\' is not allowed");
        }
        for (int i = 1; i < prefix.length(); ++i) {
            c = prefix.charAt(i);
            if ((c < '0' || c > '9') && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != '_') {
                throw new RuntimeException("Identifier prefix \'" + prefix + "\' is not allowed");
            }
        }
        this.prefix = prefix;
    }
    
    /**
     * Produces an unique identifier.
     * @return the produced identifier
     */
    public String produceIdentifier() {
        return prefix + ++cnt;
    }
    
    /**
     * Produces an unique identifier based on the given string.
     * @param string the string
     * @return the produced identifier
     */
    public String produceIdentifier(String string) {
        return prefix + ++cnt + "_" + escape(string);
    }
    
    /**
     * Makes the given string safe to be used in identifiers.
     * @param string the string
     * @return the result string
     */
    private String escape(String string) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') { // if c safe is character
                builder.append(c);
            }
            else {
                builder.append('_');
            }
        }
        return builder.toString();
    }    
}
