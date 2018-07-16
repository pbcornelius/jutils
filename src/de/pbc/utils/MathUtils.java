package de.pbc.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MathUtils {
	
	// PUBLIC -------------------------------------------------------- //
	
	public static Double evaluate(double x) {
		return 1 / (1 + Math.exp(-x));
	}
	
	public static Double derivative(double x) {
		return Math.exp(x) / Math.pow(1 + Math.exp(x), 2d);
	}
	
	public static Double derivativeAcc(double x0, double x1) {
		return evaluate(x1) - evaluate(x0);
	}
	
	/**
	 * Objects are treated as unique based on the equals/hashCode methods.
	 */
	public static <T> Collection<Collection<T>> powerSet(Collection<T> originalSet) {
		Collection<Collection<T>> sets = new HashSet<Collection<T>>();
		
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<T>());
			return sets;
		}
		
		List<T> list = new ArrayList<T>(originalSet);
		T head = list.get(0);
		Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
		
		for (Collection<T> set : powerSet(rest)) {
			Collection<T> newSet = new HashSet<T>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		
		return sets;
	}
	
	/**
	 * Each object is treated as unique.
	 */
	public static <T> Collection<Collection<T>> powerSet1(Collection<T> originalSet) {
		Collection<Collection<T>> sets = new ArrayList<Collection<T>>();
		
		if (originalSet.isEmpty()) {
			sets.add(new ArrayList<T>());
			return sets;
		}
		
		List<T> list = new ArrayList<T>(originalSet);
		T head = list.get(0);
		Collection<T> rest = new ArrayList<T>(list.subList(1, list.size()));
		
		for (Collection<T> set : powerSet(rest)) {
			Collection<T> newSet = new HashSet<T>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		
		return sets;
	}
	
	public static double harmonicNumbers(int n) {
		double sum = 0d;
		for (int i = 1; i <= n; i++)
			sum += 1d / i;
		return sum;
	}
	
	public static Long[] genNumbers(Long from, Long to) {
		Long[] out = new Long[(int) (to - from + 1)];
		int i = 0;
		for (; from <= to; from++)
			out[i++] = from;
		return out;
	}
	
	public static Integer[] genNumbers(Integer from, Integer to) {
		Integer[] out = new Integer[(int) (to - from + 1)];
		int i = 0;
		for (; from <= to; from++)
			out[i++] = from;
		return out;
	}
	
	public static double euclideanDistance(int i0, int j0, int i1, int j1) {
		return Math.sqrt(Math.pow(i0 - i1, 2d) + Math.pow(j0 - j1, 2d));
	}
	
}