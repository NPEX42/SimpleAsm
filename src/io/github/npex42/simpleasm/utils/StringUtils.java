package io.github.npex42.simpleasm.utils;

import java.util.regex.Pattern;

public class StringUtils {
    public static String StripWhiteSpace(String input) {
        String output = "";
        for(char c : input.toCharArray()) {
            if(!(""+c).matches("\\s")) {
                output += c;
            }
        }
        return output;
    }

    public static String RemoveLast(String text, int count) {
        return text.substring(0, text.length() - count);
    }

    public static String SafeReplacement(String source, String oldStr, String newStr) {
        String safeSource = Pattern.quote(source);
        String safeOldStr = Pattern.quote(source);
        String safeNewStr = Pattern.quote(source);

        return safeSource.replaceAll(safeOldStr, safeNewStr);
    }
}
