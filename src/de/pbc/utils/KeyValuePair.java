package de.pbc.utils;

/**
 * Key-value pair interface.
 * 
 * @author Philipp Cornelius
 * @param <K> key type
 * @param <V> value type
 */
public interface KeyValuePair<K, V> {
	
	// PUBLIC --------------------------------------------------------- //
	
	public K getKey();
	
	public KeyValuePair<K, V> setKey(K key);
	
	public V getValue();
	
	public KeyValuePair<K, V> setValue(V value);
	
	// FACTORY -------------------------------------------------------- //
	
	public static <K, V> KeyValuePair<K, V> create() {
		return new KeyValuePairImpl<K, V>();
	}
	
	public static <K, V> KeyValuePair<K, V> create(K key) {
		return new KeyValuePairImpl<K, V>(key);
	}
	
	public static <K, V> KeyValuePair<K, V> create(K key, V value) {
		return new KeyValuePairImpl<K, V>(key, value);
	}
	
}