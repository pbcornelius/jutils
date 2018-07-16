package de.pbc.utils;

import java.util.Objects;

/**
 * Simple key-value-pair class.
 * 
 * @author Philipp Cornelius
 * @param <K> key type
 * @param <V> value type
 */
public class KeyValuePairImpl<K, V> implements KeyValuePair<K, V> {
	
	// VARIABLES ------------------------------------------------------ //
	
	private K key;
	
	private V value;
	
	// CONSTRCUTOR ---------------------------------------------------- //
	
	public KeyValuePairImpl() {
		this(null, null);
	}
	
	public KeyValuePairImpl(K key) {
		this(key, null);
	}
	
	public KeyValuePairImpl(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	// PUBLIC --------------------------------------------------------- //
	
	public K getKey() {
		return key;
	}
	
	public KeyValuePair<K, V> setKey(K key) {
		this.key = key;
		return this;
	}
	
	public V getValue() {
		return value;
	}
	
	public KeyValuePair<K, V> setValue(V value) {
		this.value = value;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(key);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KeyValuePairImpl<?, ?>)
			return Objects.equals(key, ((KeyValuePairImpl<?, ?>) obj).getKey());
		else
			return false;
	}
	
	@Override
	public String toString() {
		return getKey() + "=" + getValue();
	}
	
}