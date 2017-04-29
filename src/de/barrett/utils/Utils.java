package de.barrett.utils;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {
	
	// CONSTANTS ----------------------------------------------------- //
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	
	// PUBLIC -------------------------------------------------------- //
	
	public static <T> List<T> addNotNull(List<T> list, T t) {
		if (t != null)
			list.add(t);
		return list;
	}
	
	public static String getClassName(Object o) {
		return getClassName(o.getClass());
	}
	
	public static String getClassName(Class<?> clazz) {
		return clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
	}
	
	public static Double[] genRandomNumbers(Integer count) {
		Double[] randomNumbers = new Double[count];
		for (int i = 0; i < count; i++)
			randomNumbers[i] = Math.random();
		return randomNumbers;
	}
	
	public static <T> T[] arraysConcatenate(T[] a, T[] b) {
		int aLen = a.length;
		int bLen = b.length;
		
		@SuppressWarnings("unchecked")
		T[] C = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(a, 0, C, 0, aLen);
		System.arraycopy(b, 0, C, aLen, bLen);
		
		return C;
	}
	
	public static boolean areNull(Object... objects) {
		for (Object obj : objects)
			if (obj != null)
				return false;
		return true;
	}
	
	public static boolean areNotNull(Object... objects) {
		for (Object obj : objects)
			if (obj == null)
				return false;
		return true;
	}
	
	/**
	 * Throws a {@link NullPointerException} at the first object that is
	 * {@code null} with the message being the {@code null}-object's index.
	 */
	public static void requireNonNull(Object... objects) throws NullPointerException {
		for (int i = 0; i < objects.length; i++)
			if (objects[i] == null)
				throw new NullPointerException(String.valueOf(i));
	}
	
	/**
	 * Throws a {@link NullPointerException} if all objects are null.
	 */
	public static void requireAnyNonNull(Object... objects) throws NullPointerException {
		if (Arrays.stream(objects).parallel().filter((o) -> o != null).count() == 0)
			throw new NullPointerException("all objects are null");
	}
	
	public static String[] toStrArray(Object... objects) {
		String[] output = new String[objects.length];
		for (int i = 0; i < objects.length; i++)
			output[i] = String.valueOf(objects[i]);
		return output;
	}
	
	public static String readFile(String fileLocation, String encoding) throws IOException {
		return StringUtils.toString(new FileInputStream(fileLocation), encoding);
	}
	
	public static void writeFile(String fileLocation, String fileName, String encoding, String fileContent)
			throws IOException {
		Path dir = Files.createDirectories(FileSystems.getDefault().getPath(fileLocation));
		BufferedWriter fileWriter = Files.newBufferedWriter(dir.resolve(fileName), Charset.forName(encoding));
		fileWriter.write(fileContent);
		fileWriter.close();
	}
	
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	public static URL createUrlQuietly(Path path) {
		try {
			return path.toUri().toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static URL createUrlQuietly(String path) {
		try {
			return Paths.get(path).toUri().toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> Stream<List<T>> batches(List<T> source, int length) {
		if (length <= 0)
			throw new IllegalArgumentException("length = " + length);
		
		int size = source.size();
		if (size <= 0)
			return Stream.empty();
		
		int fullChunks = (size - 1) / length;
		return IntStream.range(0, fullChunks + 1).mapToObj(n -> source.subList(n * length,
				n == fullChunks ? size : (n + 1) * length));
	}
	
	public static <E> E randomElement(Collection<? extends E> coll) {
		return randomElement(coll, new Random());
	}
	
	public static <E> E randomElement(Collection<? extends E> coll, Random rand) {
		if (coll.size() == 0) {
			return null;
		}
		
		int index = rand.nextInt(coll.size());
		if (coll instanceof List) {
			return ((List<? extends E>) coll).get(index);
		} else {
			Iterator<? extends E> iter = coll.iterator();
			for (int i = 0; i < index; i++) {
				iter.next();
			}
			return iter.next();
		}
	}
	
}