package unification;

import java.util.regex.Pattern;

public class Utils {
    private static final String[] VARIABLES_LETTERS = new String[]{"x", "y", "z"};
    private static final String[] CONSTANTS_LETTERS = new String[]{"a", "b", "c", "d"};

    private static final String VARIABLE_LETTERS_JOINED = String.join("", VARIABLES_LETTERS);
    private static final String CONSTANT_LETTERS_JOINED = String.join("", CONSTANTS_LETTERS);

    private static final String variablePatternString = String.format("[%s]\\d*", Utils.VARIABLE_LETTERS_JOINED);
    private static final String constantPatternString = String.format("[%s]\\d*", Utils.CONSTANT_LETTERS_JOINED);
    private static final Pattern variablePattern = Pattern.compile(variablePatternString);
    private static final Pattern constantPattern = Pattern.compile(constantPatternString);

    public static boolean variableMatches(String input) {
        return variablePattern.matcher(input).matches();
    }

    public static boolean constantMatches(String input) {
        return constantPattern.matcher(input).matches();
    }
}
