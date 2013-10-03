package su.nsk.iis.cpn;

import java.io.PrintStream;

/**
 *
 * @author Alexander Stenenko
 */
public class CodePrintStream {

    private PrintStream out;
    private int indentLevel;

    /**
     * Constructs the code printing stream that prints the code to the given printing stream.
     * @param out the printing stream
     */
    public CodePrintStream(PrintStream out) {
        this.out = out;
        indentLevel = 0;
    }

    /**
     * Prints an empty line with tabulations.
     */
    public void println() {
        printTabs(indentLevel);
        out.println();
    }

    /**
     * Prints the string on the line with tabulations.
     * @param s the string.
     */
    public void println(String s) {
        if (s.isEmpty()) {
            println();
            return;
        }
        boolean incrementIndent = false, decrementIndent = false, withoutIndent = false;

        if (s.charAt(s.length() - 1) == '{') incrementIndent = true;
        else if (s.charAt(s.length() - 1) == ':') withoutIndent = true;

        if (s.charAt(0) == '}') decrementIndent = true;
        else if (s.charAt(0) == '#') withoutIndent = true;

        if (s.length() > 1) {
            if (s.charAt(0) == '/' && s.charAt(1) == '/') withoutIndent = true;
            else if (s.charAt(0) == ':' && s.charAt(1) == ':') {
                incrementIndent = true;
                decrementIndent = true;
            }
            else if (s.charAt(0) == 'i' && s.charAt(1) == 'f' || s.charAt(0) == 'd' && s.charAt(1) == 'o') {
                if (s.length() == 2) incrementIndent = true;
                else if (s.charAt(2) == ' ') incrementIndent = true;
            }
            else if (s.charAt(0) == 'f' && s.charAt(1) == 'i' || s.charAt(0) == 'o' && s.charAt(1) == 'd') {
                if (s.length() == 2) decrementIndent = true;
                else if (s.charAt(2) == ';') decrementIndent = true;
            }
            else if (s.length() > 2) {
                if (s.charAt(0) == 'F' && s.charAt(1) == 'O' && s.charAt(2) == 'R') {
                    if (s.length() == 3) incrementIndent = true;
                    else if (s.charAt(3) == ' ') incrementIndent = true;
                }
                else if (s.charAt(0) == 'R' && s.charAt(1) == 'O' && s.charAt(2) == 'F') {
                    if (s.length() == 3) decrementIndent = true;
                    else if (s.charAt(3) == ';') decrementIndent = true;
                }
            }
        }

        if (decrementIndent) --indentLevel;
        int currentIndentLevel = indentLevel;
        if (incrementIndent) ++indentLevel;

        if (! withoutIndent) printTabs(currentIndentLevel);
        out.print(s);
        /*if (DebugLevel.generatePMLCodeWithReferences()) {
            if (!withoutIndent) {
                int codeWidth = 120;
                printSpaces(codeWidth - (s.length() + currentIndentLevel * DebugLevel.spacesInTabulation()));
                out.print(getCodePointComment());
            }
        }*/
        out.println();
    }

    /**
     * Gets the Promela comment with the line number where was called the method which called this method.
     * @return
     */
    private String getCodePointComment() {
        StackTraceElement ste[] = Thread.currentThread().getStackTrace();
        String res = "// ";
        for (int i = 4; i < ste.length; ++i) {
            if (i > 6) break;
            if (ste[i].getMethodName().equals("translate")) break;
            if (i > 4) res += ", ";
            res += ste[i].getLineNumber();
        }
        return res;
    }

    /**
     * Prints n tabulations to the out stream.
     * @param n the number n
     */
    private void printTabs(int n) {
        /*if (DebugLevel.spacesInsteadOfTabulations()) {
            printSpaces(n * DebugLevel.spacesInTabulation());
        }
        else {*/
            printChars('\t', n);
        /*}*/
    }

    /**
     * Prints n spaces to the out stream.
     * @param n the number n
     */
    private void printSpaces(int n) {
        printChars(' ', n);
    }

    /**
     * Prints the given character n times to the out stream.
     * @param c the character
     * @param n the number n
     */
    private void printChars(char c, int n) {
        for (int i = 0; i < n; ++i) out.print(c);
    }
}
