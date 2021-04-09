package io.github.npex42.simpleasm;

import io.github.npex42.simpleasm.utils.BinaryFile;
import io.github.npex42.simpleasm.utils.OpcodeMap;
import io.github.npex42.simpleasm.utils.OperandParser;
import io.github.npex42.simpleasm.utils.TextFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<String> argList = Arrays.asList(args);



        File file = new File("Instructions.cfg");
        OperandParser parser = new OperandParser();

        for(String arg : argList) {
            if(arg.matches("[A-z_-]+=[0-9]+")) {
                String sym = arg.split("=")[0];
                int val = Integer.parseInt(arg.split("=")[1]);

                parser.SetSymbol(sym, val);
            }
        }
        String inputPath = "";
        if(argList.contains("-i")) inputPath = argList.get(argList.indexOf("-i") + 1);
        String outputPath = "";
        if(argList.contains("-o")) outputPath = argList.get(argList.indexOf("-o") + 1);

        String symPath = "";
        if(argList.contains("--export-symbols")) symPath = argList.get(argList.indexOf("--export-symbols") + 1);

        OpcodeMap map = OpcodeMap.load(file);
        List<Integer> bytes;
        List<String> program = TextFile.Load("Test.asm").getContents();
        Assembler asm = new Assembler(parser, map);
        program = asm.preprocess(program);
        bytes = asm.assemble(program);
        if(argList.contains("--export-symbols")) parser.ExportSymbols(symPath);

        System.out.println(bytes);
        BinaryFile bin = new BinaryFile();
        bin.setData(bytes);
        bin.SaveBytes(outputPath);

    }
}
