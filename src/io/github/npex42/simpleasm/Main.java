package io.github.npex42.simpleasm;

import io.github.npex42.simpleasm.api.Assembler;
import io.github.npex42.simpleasm.api.Linker;
import io.github.npex42.simpleasm.utils.*;

import java.util.List;

@SuppressWarnings("ALL")
public class Main {
    public static ArgumentParser argParser;
    public static void main(String[] args) {
        argParser = new ArgumentParser(args);
        System.out.println(StringUtils.StripSurrounding("{0;3}", 1));
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

        Assembler asm = new AssemblerImpl(parser, map);
        Linker linker = new LinkerImpl();

        if(argParser.containsKey("-exp")) asm.EnableSymbolExport(argParser.getString("-exp"));
        long tp1, tp2;
        tp1 = System.currentTimeMillis();
        program = asm.preprocess(program);
        System.out.printf("Preprocessing '%s'...%n", argParser.getString("-i"));
        tp2 = System.currentTimeMillis();
        if(argParser.containsKey("--verbose"))
            System.out.printf("[ASM] Preprocessed %d lines in %2.2f seconds%n", program.size(), ((tp2 - tp1) / 1000f));
        System.out.printf("Assembling '%s'...%n", argParser.getString("-i"));
        tp1 = System.currentTimeMillis();
        List<Section> sections = asm.assemble(program);
        tp2 = System.currentTimeMillis();
        if(argParser.containsKey("--verbose"))
            System.out.printf("[ASM] Assembled %d lines in %2.2f seconds%n", program.size(), ((tp2 - tp1) / 1000f));

        //System.out.println(bytes);
        System.out.printf("Linking '%s'...%n", argParser.getString("-o"));
        BinaryFile bin = linker.link(sections);

        tp1 = System.currentTimeMillis();
          if(argParser.containsKey("-ob")) {
            bin.SaveBytes(argParser.getString("-o"));
            if(argParser.containsKey("--verbose"))
                System.out.println("Written Using BE8");
        } else if(argParser.containsKey("-os")) {
            bin.SaveShorts(argParser.getString("-o"));
            if(argParser.containsKey("--verbose"))
                System.out.println("Written Using BE16");
        } else {
            bin.Save(argParser.getString("-o"));
            if(argParser.containsKey("--verbose"))
                System.out.println("Written Using BE32");
        }
        tp2 = System.currentTimeMillis();
        if(argParser.containsKey("--verbose"))
            System.out.printf("[ASM] Wrote %,d Words in %2.2f seconds%n", bin.getData().size(), ((tp2 - tp1) / 1000f));

    }
}
