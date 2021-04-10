package io.github.npex42.simpleasm;

import io.github.npex42.simpleasm.utils.OpcodeMap;
import io.github.npex42.simpleasm.utils.OperandParser;
import io.github.npex42.simpleasm.utils.Preprocessor;
import io.github.npex42.simpleasm.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Assembler {
    private OperandParser parser;
    private OpcodeMap map;
    private List<Preprocessor> preprocessors = new ArrayList<>();

    private boolean exportSymbols;
    private String exportPath;

    public Assembler(OperandParser parser, OpcodeMap map) {
        this.parser = parser;
        this.map = map;

        preprocessors.add(this::RemoveEmptyLinesAndTrim);
        preprocessors.add(this::Labels);
    }

    public boolean add(Preprocessor preprocessor) {
        return preprocessors.add(preprocessor);
    }

    public List<String> preprocess(List<String> program) {
        for(Preprocessor p : preprocessors) {
            program = p.process(program, parser, map);
        }
        return program;
    }

    private List<String> Labels(List<String> program, OperandParser parser, OpcodeMap map) {
        int offset = 0;
        for(String line : program) {
            if(line.matches("[A-z_-]+:")) {
                parser.SetSymbol(StringUtils.RemoveLast(line, 1), offset);
            } else {
                offset += map.getInstructionSize();
            }
        }

        return program;

    }


    private List<String> RemoveEmptyLinesAndTrim(List<String> program, OperandParser parser, OpcodeMap map) {
        List<String> newProg = new ArrayList<>();
        for(String line : program) {
            if(!line.isBlank()) newProg.add(line.trim());
        }
        return newProg;
    }

    public List<Integer> assemble(List<String> program) {
        List<Integer> bytes = new ArrayList<>();
        int lineNum = 1;
        for(String line : program) {
            if(line.matches("[A-z_-]+:")) { continue; }

            try {
                bytes.add(map.getOpcode(line));
                String operands = line.split("\\s+")[1];
                if(map.getInstructionSize() > -1) {
                    bytes.addAll(parser.parseOperands(operands, map.getInstructionSize()));
                } else {
                    bytes.addAll(parser.parseOperands(operands));
                }

            } catch (OpcodeMap.InvalidInstructionException ex) {
                System.err.printf("[Line %d]: Unknown Instruction '%s'%n", lineNum, line);
                break;
            }
            lineNum++;
        }

        if(exportSymbols) parser.ExportSymbols(exportPath);
        if(bytes.size() % map.getInstructionSize() != 0) System.out.println("Potential Instruction Slip!");
        return bytes;
    }

    public void EnableSymbolExport(String path) {
        exportSymbols = true;
        exportPath = path;


    }



}
