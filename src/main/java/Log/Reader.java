package Log;

import java.io.BufferedReader;
import java.io.*;

public class Reader {
    private BufferedReader br;

    public Reader(String filename) {
        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine() {
        String content = null;
        try {
            content = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(content);
        return content;
    }

    public void close() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
