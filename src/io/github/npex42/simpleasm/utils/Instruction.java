package io.github.npex42.simpleasm.utils;

import java.util.ArrayList;
import java.util.List;

public class Instruction {
    public final List<Integer> operands = new ArrayList<>();
    public final String regex;
    public final String outputFormat;
    public final List<String> symbolPatterns = new ArrayList<>();

    public Instruction(String regex, String outputFormat) {
        this.regex = regex;
        this.outputFormat = outputFormat;
    }
}
