package io.github.npex42.simpleasm.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFile {
    private List<Integer> data = new ArrayList<>();

    private OutputStream outputStream;

    public static BinaryFile Load(String path) throws RuntimeException {
        try {
            BinaryFile file = new BinaryFile();
            List<Integer> buffer = new ArrayList<>();
            DataInputStream stream = new DataInputStream(new FileInputStream(path));
            File f = new File(path);
            for(long i = 0; i < f.length() / Integer.BYTES; i++) {
                buffer.add(stream.readInt());
            }
            file.setData(buffer);
            return file;
        } catch (IOException ioex) {
            throw new RuntimeException();
        }
    }

    public static BinaryFile LoadBytes(String path) throws RuntimeException {
        try {
            BinaryFile file = new BinaryFile();
            List<Integer> buffer = new ArrayList<>();
            DataInputStream stream = new DataInputStream(new FileInputStream(path));
            File f = new File(path);
            for(long i = 0; i < f.length(); i++) {
                buffer.add(stream.readUnsignedByte());
            }
            file.setData(buffer);
            return file;
        } catch (IOException ioex) {
            throw new RuntimeException();
        }
    }

    public static BinaryFile LoadShorts(String path) throws RuntimeException {
        try {
            BinaryFile file = new BinaryFile();
            List<Integer> buffer = new ArrayList<>();
            DataInputStream stream = new DataInputStream(new FileInputStream(path));
            File f = new File(path);
            for(long i = 0; i < f.length() / Short.BYTES; i++) {
                buffer.add(stream.readUnsignedByte());
            }
            file.setData(buffer);
            return file;
        } catch (IOException ioex) {
            throw new RuntimeException();
        }
    }

    public void Save(String path) throws RuntimeException  {
        try {
            File f = new File(path);
            f.createNewFile();
            setOutputStream(f);
            DataOutputStream stream = new DataOutputStream(outputStream);
            for(int i : data) {
                stream.writeInt(i);
            }
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void SaveBytes(String path) throws RuntimeException  {
        try {
            File f = new File(path);
            f.createNewFile();
            setOutputStream(f);
            DataOutputStream stream = new DataOutputStream(outputStream);
            for(int i : data) {
                if(i < 256) stream.writeByte(i);
                if(i > 256 && i < 65536) stream.writeShort(i);
                if(i > 65536 && i < 4294967296L) stream.writeInt(i);
            }
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void SaveShorts(String path) throws RuntimeException  {
        try {
            File f = new File(path);
            f.createNewFile();
            setOutputStream(f);
            DataOutputStream stream = new DataOutputStream(outputStream);
            for(int i : data) {
                if(i < 256) stream.writeShort(i);
                if(i > 256 && i < 65536) stream.writeShort(i);
                if(i > 65536 && i < 4294967296L) stream.writeInt(i);
            }
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }



    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setOutputStream(File file) throws FileNotFoundException {
        this.outputStream = new FileOutputStream(file);
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
