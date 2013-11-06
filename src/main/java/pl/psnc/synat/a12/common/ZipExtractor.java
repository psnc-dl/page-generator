package pl.psnc.synat.a12.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipExtractor {

    private BufferedOutputStream destination;

    private BufferedInputStream source;

    private final static int BUFFER_SIZE = 2048;

    public void extract(File archive, File dest) {
        try {
            try {
                ZipFile zipfile = new ZipFile(archive);
                @SuppressWarnings("unchecked")
                Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipfile.entries();

                if (entries.hasMoreElements() && !dest.isDirectory()) {
                    dest.mkdir();
                }

                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    source = new BufferedInputStream(zipfile.getInputStream(entry));
                    byte data[] = new byte[BUFFER_SIZE];
                    File destFile = prepareDestination(dest, entry.getName());
                    destFile.getParentFile().mkdirs();

                    if (!entry.isDirectory()) {
                        FileOutputStream fos = new FileOutputStream(destFile);
                        destination = new BufferedOutputStream(fos, BUFFER_SIZE);

                        int count;
                        while ((count = source.read(data, 0, BUFFER_SIZE)) != -1) {
                            destination.write(data, 0, count);
                        }
                    }
                    destination.flush();
                    destination.close();
                    source.close();

                }
            } finally {
                if (destination != null) {
                    destination.flush();
                    destination.close();
                }
                if (source != null) {
                    source.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Problem with extraction archive", e);
        }
    }


    private File prepareDestination(File dest, String fileName)
            throws IOException {
        StringBuilder sbName = new StringBuilder(dest.getAbsolutePath());
        sbName.append(File.separator);
        sbName.append(fileName);

        File file = new File(sbName.toString());
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

}
