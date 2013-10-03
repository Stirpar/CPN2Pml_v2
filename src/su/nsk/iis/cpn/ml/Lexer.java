package su.nsk.iis.cpn.ml;

import java.util.LinkedList;
import java.util.List;

/**
 * CPN ML lexer class.
 * @author Alexander Stenenko
 */
public class Lexer {
    private String s;
    private int pos;
    private Lexem lexem;

    /**
     * Constructs the lexer parsing the given string
     * @param s the string
     */
    public Lexer(String s) throws SyntaxError {
        this.s = s;
        pos = 0;
        lexem = getLexem();
    }

    /**
     * Gets the current lexem.
     * @return the current lexem
     */
    public Lexem curLexem() {
        return lexem;
    }

    public boolean curLexemEquals(String supposed) {
        return supposed.equals(lexem.getString());
    }

    /**
     * Gets the next lexem.
     * @return the next lexem
     */
    public Lexem nextLexem() throws SyntaxError {
        lexem = getLexem();
        return lexem;
    }

    public Lexem nextLexem(String expected) throws SyntaxError {
        lexem = getLexem();
        if (! expected.equals(lexem.getString())) {
            throw new SyntaxError("Expected \'" + expected + "\' instead of \'" + lexem.getString() + "\'in expression \'" + s + "\'");
        }
        return lexem;
    }

    public List<Lexem> getLexems(String separatedBy) throws SyntaxError {
        List<Lexem> result = new LinkedList<Lexem>();
        while (lexem.getType() != LexemType.EOF) {
            result.add(lexem);
            nextLexem();
            if (!separatedBy.equals(lexem.getString())) break;
            nextLexem();
        }
        return result;
    }

    /**
     * Parses the string to get the next lexem.
     * @return the parsed lexem
     */
    private Lexem getLexem() throws SyntaxError {
        if (end()) return new Lexem(""); // END lexem

        if (end()) return new Lexem("");
        while (skipComment()) {
            if (end()) return new Lexem("");
        }
        while (isWhitespace()) {
            ++pos;
            if (end()) return new Lexem("");
            while (skipComment()) {
                if (end()) return new Lexem("");
            }
        }
        int p = pos;
        if (isDigit()) {
            do {
                ++pos;
                if (end()) break;
            } while (isDigit());
        }
        else if (isLetter()) {
            do {
                ++pos;
                if (end()) break;
            } while (isLetterOrDigit());
        }
        else if (isSign()) {
            do {
                ++pos;
                if (end()) break;
            } while (isSign());
        }
        else if (isSymbol()) ++pos;
        return new Lexem( s.substring(p, pos) );
    }

    /**
     * Checks if the string ended.
     * @return true if the string ended, false otherwise
     */
    private boolean end() {
        return pos == s.length();
    }

    private boolean  skipComment() throws SyntaxError {
        if (s.charAt(pos) == '(') {
            if (s.length() == pos + 1) return false;
            if (s.charAt(pos + 1) == '*') {
                pos += 2;
                while (true) {
                    if (s.length() == pos + 1) throw new SyntaxError("Comment doesn't end in expression \'" + s + "\'");
                    if (s.charAt(pos) == '*' && s.charAt(pos + 1) == ')') {
                        pos += 2;
                        return true;
                    }
                    ++pos;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the character in the current position is a whitespace character.
     * @return true if current character is a whitespace character, false otherwise
     */
    private boolean isWhitespace() {
        if (pos == s.length()) return true; // ?
        char c = s.charAt(pos);
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    /**
     * Checks if the character in the current position is a digit.
     * @return true if current character is a digit, false otherwise
     */
    private boolean isDigit() {
        char c = s.charAt(pos);
        return c >= '0' && c <= '9';
    }

    /**
     * Checks if the character in the current position is a hexadecimal digit.
     * @return true if current character is a hexadecimal digit, false otherwise
     */
    private boolean isHexDigit() {
        char c = s.charAt(pos);
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
    }

    /**
     * Checks if the character in the current position is a letter.
     * @return true if current character is a letter, false otherwise
     */
    private boolean isLetter() {
        char c = s.charAt(pos);
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_' || c == '\'';
    }

    /**
     * Checks if the character in the current position is a letter or a digit.
     * @return true if current character is a letter or a digit, false otherwise
     */
    private boolean isLetterOrDigit() {
        return isLetter() || isDigit();
    }

    /**
     * Checks if the character in the current position is a sign.
     * @return true if current character is a sign, false otherwise
     */
    private boolean isSign() {
        char c = s.charAt(pos);
        return c == '!' || c == '%' || c == '&' || c == '$' || c == '#' || c == '+' || c == '-' ||
                c == '/' || c == ':' || c == '<' || c == '=' || c == '>' || c == '?' || c == '@' ||
                c == '\\' || c == '~' || c == '`' || c == '^' || c == '|' || c == '*';
    }

    /**
     * Checks if the character in the current position is a special symbol of CPN ML.
     * @return true if current character is a special symbol, false otherwise
     */
    private boolean isSymbol() {
        char c = s.charAt(pos);
        return c == '(' || c == ')' || c == ',' || c == '[' || c == ']' || c == '{' || c == '}';
    }

}
