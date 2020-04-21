package de.pbc.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * String utils.
 * 
 * @author Philipp Cornelius
 * @version 1.2 (2012-03-26)
 */
public class StringUtils {
	
	public static String decodeNCR(String input, char unknownCh) {
		StringBuffer sb = new StringBuffer();
		int i1 = 0;
		int i2 = 0;
		
		while (i2 < input.length()) {
			i1 = input.indexOf("&#", i2);
			if (i1 == -1) {
				sb.append(input.substring(i2));
				break;
			}
			sb.append(input.substring(i2, i1));
			i2 = input.indexOf(";", i1);
			if (i2 == -1) {
				sb.append(input.substring(i1));
				break;
			}
			
			String tok = input.substring(i1 + 2, i2);
			try {
				int radix = 10;
				if (tok.charAt(0) == 'x' || tok.charAt(0) == 'X') {
					radix = 16;
					tok = tok.substring(1);
				}
				sb.append((char) Integer.parseInt(tok, radix));
			} catch (NumberFormatException exp) {
				sb.append(unknownCh);
			}
			i2++;
		}
		return sb.toString();
	}
	
	public static String decodeURL(String input, String charset) {
		try {
			return URLDecoder.decode(input, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String encodeURL(String input) {
		try {
			return URLEncoder.encode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String removeHtmlTags(String input) {
		return input.replaceAll("\\<[^>]*>", "");
	}
	
	/**
	 * For two <code>Strings</code> <code>A</code> and <code>B</code> the
	 * <code>Match</code> is:<br>
	 * <code>match = match - ( editDistance / ( 2 * titleLength ) ) * 100</code><br>
	 * with <code>match = 100.0</code> and for edit distance see
	 * <code>editDistance (String, String)</code>.
	 * 
	 * @param o1 a <code>String</code>
	 * @param o2 a <code>String</code> which is compared to <code>o1</code>
	 * @return the relative match of the specified <code>String</code>s
	 */
	public static double getMatch(String o1, String o2) {
		double match = 1.0;
		
		double editDistance = editDistance(o1, o2);
		double titleLength = o1.length();
		
		match = ((titleLength - editDistance) / titleLength);
		
		if (match < 0) {
			match = 0.0;
		}
		
		return match;
	}
	
	/**
	 * A helper method for <code>editDistance(String, String)</code>.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return the minimum of the given integer
	 */
	private static int min(int a, int b, int c) {
		
		int mi;
		
		mi = a;
		
		if (b < mi) {
			mi = b;
		}
		if (c < mi) {
			mi = c;
		}
		return mi;
		
	}
	
	/**
	 * The edit distance of the specified <code>String</code>s (see
	 * <a href="http://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein
	 * distance</a>).
	 * 
	 * @param s a <code>String</code>
	 * @param t a <code>String</code> witch is compared to <code>o1</code>
	 * @return the Levenshtein distance
	 */
	public static int editDistance(String s, String t) {
		int d[][]; // matrix
		int n; // length of s
		int m; // length of t
		int i; // iterates through s
		int j; // iterates through t
		char s_i; // ith character of s
		char t_j; // jth character of t
		int cost; // cost
		
		n = s.length();
		m = t.length();
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];
		
		for (i = 0; i <= n; i++) {
			d[i][0] = i;
		}
		
		for (j = 0; j <= m; j++) {
			d[0][j] = j;
		}
		
		for (i = 1; i <= n; i++) {
			
			s_i = s.charAt(i - 1);
			
			for (j = 1; j <= m; j++) {
				
				t_j = t.charAt(j - 1);
				
				if (s_i == t_j) {
					cost = 0;
				} else {
					cost = 1;
				}
				
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
				
			}
			
		}
		
		return d[n][m];
		
	}
	
	/**
	 * Reads an {@code InputStream} into a {@code String}.
	 * 
	 * Same as {@link #toString(InputStream, String, boolean)} with
	 * {@code utfFix = true}.
	 * 
	 * @see #toString(InputStream, String, boolean)
	 */
	public static String toString(InputStream inputStream, String charset) throws IOException {
		return toString(inputStream, charset, true);
	}
	
	/**
	 * Reads an {@code InputStream} into a {@code String}.
	 * 
	 * @param utfFix if {@code true} and {@code charset} is UTF-8, parses the
	 *            {@code inputStream} as {@code ISO-8859-1} if it
	 *            would contain {@code \uFFFD} otherwise
	 * @throws IOException if {@code charset} is UTF-8, {@code utfFix} is
	 *             {@code false}, and
	 *             {@code inputStream} contains {@code \uFFFD}
	 */
	public static String toString(InputStream inputStream, String charset, boolean utfFix) throws IOException {
		try {
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			
			String str = result.toString(charset);
			
			// \uFFFD is the REPLACEMENT CHARACTER which means Unicode couldn't parse a character
			if (charset.toLowerCase().equals("utf-8") && str.contains("\uFFFD")) {
				if (utfFix) {
					str = result.toString("ISO-8859-1");
				} else {
					throw new IOException(
							"The InputStream was parsed as UTF-8 but contains characters that are not UTF-8."
									+ " Set utfFix=true to parse the string as ISO-8859-1.");
				}
			}
			
			return str;
		} finally {
			inputStream.close();
		}
	}
	
}