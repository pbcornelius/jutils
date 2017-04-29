package de.barrett.utils.csv;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVWriter extends au.com.bytecode.opencsv.CSVWriter {

	// CONSTANTS ----------------------------------------------------- //
	
	public static String DEFAULT_CHARSET = "UTF-8";
	
	// CONSTRUCTOR --------------------------------------------------- //
	
	public CSVWriter(String path) throws IOException {
		super(Files.newBufferedWriter(
						Paths.get(path), 
						Charset.forName(DEFAULT_CHARSET)));
	}
	
	public CSVWriter(String path, String charset) throws IOException {
		super(Files.newBufferedWriter(
						Paths.get(path), 
						Charset.forName(charset)));
	}
	
	public CSVWriter(String path, String charset, boolean append) throws IOException {
		super(Files.newBufferedWriter(
						Paths.get(path), 
						Charset.forName(charset),
						StandardOpenOption.CREATE, append && Files.exists(Paths.get(path)) ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE));
	}
	
	public CSVWriter(Path path) throws IOException {
		super(Files.newBufferedWriter(
						path, 
						Charset.forName(DEFAULT_CHARSET)));
	}
	
	public CSVWriter(Path path, String charset) throws IOException {
		super(Files.newBufferedWriter(
						path, 
						Charset.forName(charset)));
	}
	
	public CSVWriter(Writer writer) {
		super(writer);
	}
	
	public CSVWriter(Writer writer, char separator) {
		super(writer, separator);
	}
	
	public CSVWriter(Writer writer, char separator, char quotechar) {
		super(writer, separator, quotechar);
	}
	
	public CSVWriter(Writer writer, char separator, char quotechar, String lineEnd) {
		super(writer, separator, quotechar, lineEnd);
	}
	
	public CSVWriter(Writer writer, char separator, char quotechar, char escapechar) {
		super(writer, separator, quotechar, escapechar);
	}
	
	public CSVWriter(Writer writer, char separator, char quotechar, char escapechar, String lineEnd) {
		super(writer, separator, quotechar, escapechar, lineEnd);
	}

	// PUBLIC -------------------------------------------------------- //
	
	public void writeNext(Object... objects) {
		super.writeNext(Arrays.stream(objects)
				.<ArrayList<Object>>collect(
						ArrayList<Object>::new,
						(l, e) -> {
							if (e instanceof Object[])
								l.addAll(Arrays.asList((Object[]) e));
							else
								l.add(e);
						},
						(l1, l2) -> l1.addAll(l2))
				.stream()
						.map(String::valueOf)
						.toArray(String[]::new));
	}
	
	public synchronized void writeNextSync(Object... objects) {
		super.writeNext(Arrays.stream(objects)
				.<ArrayList<Object>>collect(
						ArrayList<Object>::new,
						(l, e) -> {
							if (e instanceof Object[])
								l.addAll(Arrays.asList((Object[]) e));
							else
								l.add(e);
						},
						(l1, l2) -> l1.addAll(l2))
				.stream()
						.map(String::valueOf)
						.toArray(String[]::new));
	}
	
}