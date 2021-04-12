package io.github.npex42.simpleasm.api;

import io.github.npex42.simpleasm.utils.Preprocessor;
import java.util.List;

@SuppressWarnings("ALL")
public interface Assembler {
    boolean add(Preprocessor preprocessor);
    List<String> preprocess(List<String> program);
    List<Integer> assemble(List<String> program);
    void EnableSymbolExport(String path);
}
