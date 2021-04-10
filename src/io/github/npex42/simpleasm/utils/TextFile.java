package io.github.npex42.simpleasm.utils;

import java.io.*;
import java.util.List;
import java.util.stream.Stream;

public class TextFile {
    private List<String> contents;

    private TextFile(Stream<String> stream) {
        contents = stream.toList();
    }

    public TextFile() {}

    public static TextFile Load(String path) throws RuntimeException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            return new TextFile(reader.lines());
        } catch (IOException ioex) {
            throw new RuntimeException(ioex);
        }
    }

    public void Save(String path) throws RuntimeException {
        try {
            BufferedWriter writer = new BufferedWriter((new FileWriter(path)));
            for(String line : getContents()) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        String output = "TextFile {\n";
        for(String line : getContents()) {
            output += '\t' + line + "\n";
        }
        output += "};";
        return output;
    }
}
