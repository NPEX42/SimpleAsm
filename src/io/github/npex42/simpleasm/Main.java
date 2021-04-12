package io.github.npex42.simpleasm;

import io.github.npex42.simpleasm.utils.*;

import java.util.List;

@SuppressWarnings("ALL")
public class Main {

    public static void main(String[] args) {
        ArgumentParser argParser = new ArgumentParser(args);

        if (!argParser.hasAllKeys("-i", "-o", "-map")) {
            System.err.print(
                    """
                    Usage: asm -i=<input file> -o=<output file> -map=<mapper config> [flags]"""
            );
            System.exit(-1);
        }

        OperandParser parser = new OperandParser();

        String symPath = "";

        OpcodeMap map = OpcodeMap.load(argParser.getFile("-map"));
        List<Integer> bytes;
        List<String> program = TextFile.Load(argParser.getString("-i")).getContents();

        if(argParser.containsKey("-multi")) {

        }

        Assembler asm = new Assembler(parser, map);

        if(argParser.containsKey("-exp")) asm.EnableSymbolExport(argParser.getString("-exp"));
        long tp1, tp2;
        tp1 = System.currentTimeMillis();
        program = asm.preprocess(program);
        tp2 = System.currentTimeMillis();
        System.out.printf("[ASM] Preprocessed %d lines in %2.2f seconds%n", program.size(), ((tp2 - tp1) / 1000f));
        tp1 = System.currentTimeMillis();
        bytes = asm.assemble(program);
        tp2 = System.currentTimeMillis();
        System.out.printf("[ASM] Assembled %d lines in %2.2f seconds%n", program.size(), ((tp2 - tp1) / 1000f));

        //System.out.println(bytes);
        BinaryFile bin = new BinaryFile();
        bin.setData(bytes);

        tp1 = System.currentTimeMillis();
          if(argParser.containsKey("-ob")) {
            bin.SaveBytes(argParser.getString("-o"));
            System.out.println("Written Using BE8");
        } else if(argParser.containsKey("-os")) {
            bin.SaveShorts(argParser.getString("-o"));
              System.out.println("Written Using BE16");
        } else {
            bin.Save(argParser.getString("-o"));
              System.out.println("Written Using BE32");
        }
        tp2 = System.currentTimeMillis();
        System.out.printf("[ASM] Wrote %,d Words in %2.2f seconds%n", bytes.size(), ((tp2 - tp1) / 1000f));

    }
}
