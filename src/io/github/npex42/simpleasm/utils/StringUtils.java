package io.github.npex42.simpleasm.utils;

import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class StringUtils {
    public static String StripWhiteSpace(String input) {
        StringBuilder output = new StringBuilder();
        for(char c : input.toCharArray()) {
            if(!(""+c).matches("\\s")) {
                output.append(c);
            }
        }
        return output.toString();
    }

    public static String RemoveLast(String text, int count) {
        return text.substring(0, text.length() - count);
    }

    public static String StripSurrounding(String text, int index) {
        return text.substring(index , text.length() - index);
    }

    public static String SafeReplacement(String source, String oldStr, String newStr) {
        String safeSource = Pattern.quote(source);
        String safeOldStr = Pattern.quote(oldStr);
        String safeNewStr = Pattern.quote(newStr);

        return safeSource.replaceAll(safeOldStr, safeNewStr);
    }
}
