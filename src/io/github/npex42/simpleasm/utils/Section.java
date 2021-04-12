package io.github.npex42.simpleasm.utils;

import java.util.List;

public class Section {
    private int start = 0x0000;
    private int length = 0;
    private List<Integer> data;

    public Section(int _start, int _length, List<Integer> _data) {
        while(_data.size() < _length) {
            _data.add(0);
        }
        start = _start;
        data = _data;
        length = _length;
    }

    public List<Integer> getData() {
        return data;
    }
}
