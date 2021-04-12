package io.github.npex42.simpleasm.api;

import io.github.npex42.simpleasm.utils.OpcodeMap;
import io.github.npex42.simpleasm.utils.OperandParser;

import java.util.List;

public interface Preprocessor {
    List<String> process(List<String> program, OperandParser parser, OpcodeMap map);
}
