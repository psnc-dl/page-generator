package pl.psnc.synat.a12.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BaseLinesFile {

    private File file;


    public BaseLinesFile(String filename) {
        file = new File(filename);
    }


    public BaseLinesFile(File file) {
        this.file = file;
    }


    public void write(Map<Character, Integer> offsets)
            throws IOException {
        FileOutputStream fo = new FileOutputStream(file);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fo));

        try {
            for (Entry<Character, Integer> entry : offsets.entrySet()) {
                writer.write(entry.getKey());
                writer.write('\t');
                writer.write(entry.getValue().toString());
                writer.write('\n');
            }
        } finally {
            writer.close();
        }
    }


    public Map<Character, Integer> read()
            throws IOException {
        Map<Character, Integer> offsets = new HashMap<Character, Integer>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                String[] splits = line.split("\t");
                Character key = splits[0].charAt(0);
                Integer value = Integer.parseInt(splits[1]);
                offsets.put(key, value);
            }
        } finally {
            reader.close();
        }
        return offsets;
    }
}
