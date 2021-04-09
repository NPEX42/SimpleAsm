package io.github.npex42.simpleasm;

import io.github.npex42.simpleasm.utils.OpcodeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        File file = new File("Instructions.cfg");


        OpcodeMap map = OpcodeMap.load(file);

        System.out.println(map.getOpcode("LDA #$00"));
        System.out.println(map.getOpcode("LDA #00"));
        System.out.println(map.getOpcode("LDA $0000"));
        System.out.println(map.getOpcode("LDA $0000,X"));
        System.out.println(map.getOpcode("LDA $0000,Y"));

        System.out.println(map.getOpcode("ADD #10"));
        System.out.println(map.getOpcode("SUB #10"));


        List<Integer> bytes = new ArrayList<>();
        List<String> program = new ArrayList<>();

        program.add("LDA #$FF");
        program.add("ADD #10");
        int lineNum = 1;
        for(String line : program) {
            try {
                bytes.add(map.getOpcode(line));
            } catch (OpcodeMap.InvalidInstructionException ex) {
                System.err.printf("[Line %d]: Unknown Instruction '%s'%n", lineNum, line);
                break;
            }
            lineNum++;
        }

        System.out.println(bytes);
    }
}
