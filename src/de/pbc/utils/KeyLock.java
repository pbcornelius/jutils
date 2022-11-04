package de.pbc.utils;

import java.util.HashSet;
import java.util.Set;

/*
 * Maintains key-based locks. Based on this answer: https://stackoverflow.com/a/54657009
 */
public class KeyLock<K> {

	// VARIABLES ----------------------------------------------------- //

	private final Set<K> lockedKeys = new HashSet<>();

	// PUBLIC -------------------------------------------------------- //

	public void lock(K key) throws InterruptedException {
		synchronized (lockedKeys) {
			while (!lockedKeys.add(key)) {
				lockedKeys.wait();
			}
		}
	}

	public void unlock(K key) {
		synchronized (lockedKeys) {
			lockedKeys.remove(key);
			lockedKeys.notify();
		}
	}

}