package io.github.npex42.simpleasm.api;

import io.github.npex42.simpleasm.utils.Section;

import java.util.List;

@SuppressWarnings("ALL")
public interface Assembler {
    boolean add(Preprocessor preprocessor);
    List<String> preprocess(List<String> program);
    List<Section> assemble(List<String> program);
    void EnableSymbolExport(String path);
}
