package io.github.npex42.simpleasm.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
    private Map<String, String> map = new HashMap<>();

    public ArgumentParser(String[] args) {
        for(String arg : args) {
            try {
                String key = arg.split("=")[0];
                String value = arg.split("=")[1];

                map.put(key, value);
            } catch (ArrayIndexOutOfBoundsException aex) {map.put(arg, "");}
        }
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public String getString(String key) {
        return map.getOrDefault(key, "0");
    }

    public File getFile(String key) {
        return new File(getString(key));
    }

    public <T> Class<T> getClass(String key) {
        try {
            return (Class<T>) getClass().getClassLoader().loadClass(getString(key));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public boolean hasAllKeys(String... keys) {
        for(String key : keys) {
            if (!containsKey(key)) return false;
        }
        return true;
    }
}
