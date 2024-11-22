package de.pbc.utils.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.input.BOMInputStream;

import com.opencsv.exceptions.CsvRuntimeException;

public class CSVReader extends com.opencsv.CSVReader {

	// CONSTRUCTOR --------------------------------------------------- //

	public CSVReader(Reader reader) {
		super(reader);
	}

	/**
	 * Removes byte-order-mark (BOM) typically added by Excel.
	 */
	public CSVReader(String path) throws IOException {
		this(Path.of(path));
	}

	/**
	 * Removes byte-order-mark (BOM) typically added by Excel.
	 */
	public CSVReader(Path path) throws IOException {
		super(new BufferedReader(new InputStreamReader(new BOMInputStream(Files.newInputStream(path)), "UTF-8")));
	}

	// PUBLIC -------------------------------------------------------- //

	/**
	 * Reads CSV into list of maps. The first line contains the map keys.
	 * 
	 * @throws CsvRuntimeException no lines must have been read before
	 */
	public List<Map<String, String>> readAllMaps() throws IOException, CsvRuntimeException {
		if (getRecordsRead() > 0) {
			throw new CsvRuntimeException("no lines must have been read");
		}

		List<Map<String, String>> output = new LinkedList<>();
		String[] headers = readNext();
		Objects.requireNonNull(headers);
		for (String header : headers) {
			Objects.requireNonNull(header);
		}

		String[] line = readNext();
		while (line != null) {
			Map<String, String> map = new HashMap<>(line.length);
			for (int i = 0; i < line.length; i++) {
				map.put(headers[i], line[i]);
			}
			output.add(map);
			line = readNext();
		}
		return output;
	}

}