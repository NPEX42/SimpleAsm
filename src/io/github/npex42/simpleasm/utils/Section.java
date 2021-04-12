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

    public Section Add(Section other) {
        List<Integer> thisData, otherData;
        thisData = data;
        otherData = other.data;

        for(int i = other.start; i - other.start < other.length; i++) {
            thisData.add(otherData.get(i));
        }

        return new Section(this.start, this.length + other.length, thisData);
    }

    public List<Integer> getData() {
        return data;
    }
}
