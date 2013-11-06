package pl.psnc.synat.a12.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.common.io.Files;

public class ZipOutputFile {

	private static final long serialVersionUID = -1262110296426104218L;

	private ZipOutputStream output;

	public ZipOutputFile(String filename) {
		init(filename);
	}

	public void addFile(File file) throws IOException {
		ZipEntry entry = new ZipEntry(file.getName());
		output.putNextEntry(entry);
		output.write(Files.toByteArray(file));
		output.closeEntry();
	}
	
	public void close() {
		try {
		output.close();
		} catch (IOException e) {
			throw new RuntimeException("Problem with closinf archive of glyphs", e);
		}
	}

	private void init(String filename) {
		try {
			output = new ZipOutputStream(new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Can't find give file ", e);
		}
	}
	
}
