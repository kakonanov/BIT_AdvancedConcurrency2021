package com.bit.advancedconcurrency.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class SerialUtils {

	public static int[] filter(int[] arr, Predicate<Integer> predicate) {
		List<Integer> list = new ArrayList<>();
		for (int j : arr) {
			if (predicate.test(j)) {
				list.add(j);
			}
		}
		return list.stream().mapToInt(x-> x).toArray();
	}

	public static int[] scan(int[] arr, int l, int r, BiFunction<Integer, Integer, Integer> biFunction) {
		int[] b = new int[r - l + 1];
		b[0] = 0;
		for (int i = l; i < r; ++i) {
			b[i - l + 1] = biFunction.apply(b[i - l], arr[i]);
		}
		return b;
	}

	public static int reduce(int[] arr, int l, int r, BiFunction<Integer, Integer, Integer> biFunction) {
		int res = 0;
		for (int i = l; i < r; ++i) {
			res = biFunction.apply(arr[i], res);
		}
		return res;
	}

	public static int[] map(int[] arr, Function<Integer, Integer> function) {
		int[] b = new int[arr.length];
		Consumer<Integer> consumer = (x) -> b[x] = function.apply(arr[x]);
		for (int i = 0; i < arr.length; ++i) {
			consumer.accept(i);
		}
		return b;
	}
}
