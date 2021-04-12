package io.github.npex42.simpleasm.api;

import io.github.npex42.simpleasm.utils.BinaryFile;
import io.github.npex42.simpleasm.utils.Section;

import java.util.List;

public interface Linker {
    public BinaryFile link(List<Section> sections);
}
