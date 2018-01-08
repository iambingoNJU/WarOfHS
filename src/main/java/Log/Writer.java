package Log;

import java.io.*;

public class Writer {
    private FileWriter fileWriter;

    public Writer(String filename) {
        try {
            fileWriter = new FileWriter(filename, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String content) {
        try {
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
