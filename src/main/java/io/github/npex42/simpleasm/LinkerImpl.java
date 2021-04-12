package io.github.npex42.simpleasm;

import io.github.npex42.simpleasm.api.Linker;
import io.github.npex42.simpleasm.utils.BinaryFile;
import io.github.npex42.simpleasm.utils.Section;

import java.util.ArrayList;
import java.util.List;

public class LinkerImpl implements Linker {
    @Override
    public BinaryFile link(List<Section> sections) {
        List<Integer> output = new ArrayList<>();
        for(Section s : sections) {
            output.addAll(s.getData());
        }
        BinaryFile file = new BinaryFile();
        file.setData(output);
        return file;


    }
}
