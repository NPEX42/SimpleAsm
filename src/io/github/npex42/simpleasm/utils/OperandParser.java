package io.github.npex42.simpleasm.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class OperandParser {
    private final HashMap<String, Integer> symbols = new HashMap<>();


    public void ExportSymbols(String path) throws RuntimeException {
        List<String> output = new ArrayList<>();
        for(String key : symbols.keySet()) {
            output.add(key+":"+symbols.get(key));
        }
        TextFile file = new TextFile();
        file.setContents(output);
        file.Save(path);
    }

    public void SetSymbol(String symbol, Integer value) {
        symbols.put(symbol, value);
    }

    public List<Integer> parseOperands(String operands) {
        List<Integer> output = new ArrayList<>();
        for(String operand : operands.split(",")) {
            try { output.add(parseInt(operand)); } catch (UnsafeOperand ex) {}
        }

        return output;
    }

    public List<Integer> parseOperands(String operands, int minLength) {
        List<Integer> output = new ArrayList<>();
        for(String operand : operands.split(",")) {
            try { output.add(parseInt(operand)); } catch (UnsafeOperand ex) {}
        }

        for(int i = output.size(); i < minLength - 1; i++) output.add(0);

        return output;
    }

    public int parseInt(String text) {
        //1) Locate the first Character that matches [A-Fa-f0-9]
        int index = 0;
        for(char c : text.toCharArray()) {
            if(Pattern.matches("[A-z0-9_-]", ""+c)) break;
            index++;
        }
        char type = '\0';
        if(index > 0) type = text.charAt(index - 1);

        return parseInt(text.substring(index), type);
    }

    private int parseInt(String text, char type) {
        switch(type) {
            case '#' -> { return Integer.parseInt(text, 10); }
            case '$' -> { return Integer.parseInt(text, 16); }
            case '%' -> { return Integer.parseInt(text, 2); }

        }
        if(symbols.containsKey(text)) {
            return symbols.get(text);
        } else {
            throw new UnsafeOperand();
        }

    }

    public static class UnsafeOperand extends RuntimeException {}

}
