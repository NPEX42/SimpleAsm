package io.github.npex42.simpleasm.utils;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class OpcodeMap {
    public static final String SEPARATOR = ":";
    private final Map<String, Integer> map = new HashMap<>();

    private int instructionSize = -1;
    public OpcodeMap(List<String> config) {
        int radix = 16;
        for(String line : config) {

            line = line.trim();
            if(line.startsWith("%REPR")) {
                String[] parts = line.split("\\s+");

                switch(parts[1].toLowerCase(Locale.ENGLISH)) {
                    case "bin" -> radix = 2;
                    case "oct" -> radix = 8;
                    case "dec" -> radix = 10;
                    case "hex" -> radix = 16;
                }
                continue;
            }


            if(line.startsWith("%SIZE")) {
                String[] parts = line.split("\\s+");

                if(parts[1].toLowerCase().matches("var")) instructionSize = -1;
                else {
                    instructionSize = Integer.parseInt(parts[1]);
                }
                continue;
            }

            if(line.startsWith("//")) continue;
            String[] parts = line.split(SEPARATOR);
            if(parts.length < 2) continue;
            String instructionRegex = parts[0];
            int opcode = Integer.parseInt(parts[1], radix);
            map.put(instructionRegex, opcode);
        }
    }

    public OpcodeMap(Stream<String> stream) {
        this(stream.toList());
    }

    public static OpcodeMap buildFrom(InputStream stream) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            return new OpcodeMap(reader.lines());
    }

    public static OpcodeMap load(File file)
    throws OpcodeMapReadException {
        try {
            return buildFrom(new FileInputStream(file));
        } catch (Exception ioex) {
            throw new OpcodeMapReadException();
        }
    }

    public int getOpcode(String instruction)
    throws InvalidInstructionException {
        for(String key : map.keySet()) {
            if(Pattern.matches(key, instruction)) {
                return map.get(key);
            }
        }
        throw new InvalidInstructionException();
    }

    public int getInstructionSize() {
        return instructionSize;
    }


    public static class InvalidInstructionException extends RuntimeException {}
    public static class OpcodeMapReadException extends RuntimeException {}
}
