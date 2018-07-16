package de.pbc.utils.csv;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CSVReader extends com.opencsv.CSVReader {
	
	// CONSTANTS ----------------------------------------------------- //
	
	public static String DEFAULT_CHARSET = "UTF-8";
	
	// CONSTRUCTOR --------------------------------------------------- //
	
	public CSVReader(String path) throws IOException {
		super(Files.newBufferedReader(Paths.get(path), Charset.forName(DEFAULT_CHARSET)));
	}
	
	public CSVReader(String path, String charset) throws IOException {
		super(Files.newBufferedReader(Paths.get(path), Charset.forName(charset)));
	}
	
	public CSVReader(Path path) throws IOException {
		super(Files.newBufferedReader(path, Charset.forName(DEFAULT_CHARSET)));
	}
	
	public CSVReader(Path path, String charset) throws IOException {
		super(Files.newBufferedReader(path, Charset.forName(charset)));
	}
	
	public CSVReader(Reader reader) {
		super(reader);
	}
	
	public CSVReader(Reader reader, char separator) {
		super(reader, separator);
	}
	
	public CSVReader(Reader reader, char separator, char quotechar) {
		super(reader, separator, quotechar);
	}
	
	public CSVReader(Reader reader, char separator, char quotechar, int line) {
		super(reader, separator, quotechar, line);
	}
	
	public CSVReader(Reader reader, char separator, char quotechar, char escape) {
		super(reader, separator, quotechar, escape);
	}
	
	public CSVReader(Reader reader, char separator, char quotechar, char escape, int line) {
		super(reader, separator, quotechar, escape, line);
	}
	
	public CSVReader(Reader reader, char separator, char quotechar, char escape, int line, boolean strictQuotes) {
		super(reader, separator, quotechar, escape, line, strictQuotes);
	}
	
	public CSVReader(Reader reader, char separator, char quotechar, char escape, int line, boolean strictQuotes,
			boolean ignoreLeadingWhiteSpace) {
		super(reader, separator, quotechar, escape, line, strictQuotes, ignoreLeadingWhiteSpace);
	}
	
	public CSVReader(Reader reader, char separator, char quotechar, boolean strictQuotes) {
		super(reader, separator, quotechar, strictQuotes);
	}
	
}