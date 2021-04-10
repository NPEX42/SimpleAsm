package io.github.npex42.simpleasm;

import io.github.npex42.simpleasm.utils.*;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ArgumentParser argParser = new ArgumentParser(args);

        Class<OutputStream> clazz = argParser.getClass("-output-writer");
        System.out.println(clazz);

        if (!argParser.hasAllKeys("-i", "-o", "-map")) {
            System.err.print(
                    """
                    Usage: asm -i=<input file> -o=<output file> -map=<mapper config> [flags] 
                    """
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

        program = asm.preprocess(program);
        bytes = asm.assemble(program);

        System.out.println(bytes);
        BinaryFile bin = new BinaryFile();
        bin.setData(bytes);

          if(argParser.containsKey("-ob")) {
            bin.SaveBytes(argParser.getString("-o"));
        } else if(argParser.containsKey("-os")) {
            bin.SaveShorts(argParser.getString("-o"));
        } else {
            bin.Save(argParser.getString("-o"));
        }


    }
}
