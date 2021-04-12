package io.github.npex42.simpleasm;

import io.github.npex42.simpleasm.api.Assembler;
import io.github.npex42.simpleasm.utils.OpcodeMap;
import io.github.npex42.simpleasm.utils.OperandParser;
import io.github.npex42.simpleasm.api.Preprocessor;
import io.github.npex42.simpleasm.utils.Section;
import io.github.npex42.simpleasm.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class AssemblerImpl implements Assembler {
    private final OperandParser operandParser;
    private final OpcodeMap opcodeMap;
    private final List<Preprocessor> preprocessors = new ArrayList<>();

    private boolean exportSymbols;
    private String exportPath;

    public AssemblerImpl(OperandParser operandParser, OpcodeMap opcodeMap) {
        this.operandParser = operandParser;
        this.opcodeMap = opcodeMap;

        preprocessors.add(this::RemoveEmptyLinesAndTrim);
        preprocessors.add(this::Labels);
    }

    public boolean add(Preprocessor preprocessor) {
        return preprocessors.add(preprocessor);
    }

    public List<String> preprocess(List<String> program) {
        for(Preprocessor p : preprocessors) {
            program = p.process(program, operandParser, opcodeMap);
        }
        return program;
    }

    private List<String> Labels(List<String> program, OperandParser parser, OpcodeMap map) {
        int offset = 0;
        for(String line : program) {
            if(line.startsWith(".ORG")) {
                String[] parts = line.split("\\s+");
                if (parts.length > 1) offset = Integer.parseInt(parts[1], 16) - map.getInstructionSize();
            }
            if(line.matches("[A-z_-]+:")) {
                parser.SetSymbol(StringUtils.RemoveLast(line, 1), offset);
            } else if (line.startsWith(".DB")) {
                String[] parts = line.split("\\s+");
                parser.SetSymbol(parts[1], offset);

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

    public List<Section> assemble(List<String> program) {
        List<Integer> bytes = new ArrayList<>();
        List<Section> sections = new ArrayList<>();
        int sectionStart = 0, sectionEnd = 0;
        int lineNum = 1;
        for(String line : program) {
            if(line.matches("[A-z_-]+:")) { continue; }
            if(line.startsWith(".ORG") || line.startsWith(".END")) {
                //Create A new section from current .ORG Dir
                String[] parts = line.split("\\s+");
                if(parts.length > 1) sectionEnd = Integer.parseInt(parts[1],16);
                Section section = new Section(sectionStart, sectionEnd - sectionStart, bytes);
                sections.add(section);

                bytes = new ArrayList<>();
                sectionStart = sectionEnd;
                continue;
            }

            if(line.startsWith(".DB")) {
                bytes.add(Integer.parseInt(line.split("\\s+")[2]));
                continue;
            }


            try {
                bytes.add(opcodeMap.getOpcode(line));
                String operands = line.split("\\s+")[1];
                if(opcodeMap.getInstructionSize() > -1) {
                    bytes.addAll(operandParser.parseOperands(operands, opcodeMap.getInstructionSize()));
                } else {
                    bytes.addAll(operandParser.parseOperands(operands));
                }

            } catch (OpcodeMap.InvalidInstructionException ex) {
                System.err.printf("[Line %d]: Unknown Instruction '%s'%n", lineNum, line);
                break;
            }
            lineNum++;
            sectionEnd += opcodeMap.getInstructionSize();
        }

        if(exportSymbols) operandParser.ExportSymbols(exportPath);
        if(bytes.size() % opcodeMap.getInstructionSize() != 0) System.out.println("Potential Instruction Slip!");
        return sections;
    }

    public void EnableSymbolExport(String path) {
        exportSymbols = true;
        exportPath = path;
    }



}
