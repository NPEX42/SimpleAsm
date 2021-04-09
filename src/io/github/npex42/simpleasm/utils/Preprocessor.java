package io.github.npex42.simpleasm.utils;

import java.util.List;

public interface Preprocessor {
    List<String> process(List<String> program, OperandParser parser, OpcodeMap map);
}
