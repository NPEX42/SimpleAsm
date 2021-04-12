package io.github.npex42.simpleasm.utils;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class OpcodeMap {
    public static final String SEPARATOR = ":";
    private int representationMode = REPR_BIN;
    private final Map<String, Instruction> map = new HashMap<>();
    private final Map<String, Integer> symbols = new HashMap<>();

    public static final int REPR_BIN = 0;

    public int getRepresentationMode() {
        return representationMode;
    }

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

            if(line.startsWith("%SYM")) {
                String symbol = line.split("\\s+")[1];
                int value = Integer.parseInt(line.split("\\s+")[2], 2);

                symbols.put(symbol, value);

                continue;
            }

            if(line.startsWith("//")) continue;
            String[] parts = line.split(SEPARATOR);
            if(parts.length < 2) continue;
            String instructionRegex = parts[0];
            Instruction instruction = new Instruction(instructionRegex, parts[1]);
            for(String section : instruction.outputFormat.split("\\|")) {

                try {
                    int opcode = Integer.parseInt(StringUtils.StripWhiteSpace(section), radix);
                    instruction.operands.add(opcode);
                } catch (NumberFormatException nfex) {
                    String symbolPattern = section;
                    instruction.symbolPatterns.add(symbolPattern);
                }

            }
            map.put(instructionRegex, instruction);
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
            throw new OpcodeMapReadException(ioex);
        }
    }

    public List<Integer> getOpcode(String instruction)
    throws InvalidInstructionException {
        for(String key : map.keySet()) {
            if(Pattern.matches(key, instruction)) {
                return map.get(key).operands;
            }
        }
        throw new InvalidInstructionException();
    }

    public Instruction getInstruction(String instruction)
    throws InvalidInstructionException {
            for(String key : map.keySet()) {
                if(Pattern.matches(key, instruction)) {
                    return map.get(key);
                }
            }
            throw new InvalidInstructionException();
        }

    public int getSymbol(String symbol) {
        return symbols.get(symbol);
    }

    public int getInstructionSize() {
        return instructionSize;
    }


    public static class InvalidInstructionException extends RuntimeException { InvalidInstructionException() {} InvalidInstructionException(Throwable t) {super(t);} }
    public static class OpcodeMapReadException extends RuntimeException { OpcodeMapReadException(Throwable t) {super(t);} }
}
