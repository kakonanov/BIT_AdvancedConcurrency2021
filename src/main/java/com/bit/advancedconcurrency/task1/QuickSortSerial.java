package com.bit.advancedconcurrency.task1;

import java.util.Arrays;
import java.util.Random;

public class QuickSortSerial {
	private static final Random random = new Random();

	QuickSortSerial(int randomSeed) {
		random.setSeed(randomSeed);
	}

	public int[] qsort(int[] array) {
		if (array.length > 100_000) {
			int index = random.nextInt(array.length);
			int[] left = SerialUtils.filter(array, (x) -> x < array[index]);
			int[] right = SerialUtils.filter(array, (x) -> x > array[index]);
			int l = left.length;
			int r = right.length;
			int delta = array.length - l - r;
			left = qsort(left);
			right = qsort(right);
			return concatenate(left, right, delta, array[index]);
		}
		else {
			Arrays.sort(array);
			return array;
		}
	}

	private int[] concatenate(int[] left, int[] right, int delta, int pivot) {
		int[] answer = new int[left.length + right.length + delta];
		System.arraycopy(left, 0, answer, 0, left.length);
		for (int i = left.length; i < left.length + delta; ++i) {
			answer[i] = pivot;
		}
		System.arraycopy(right, 0, answer, left.length + delta, right.length);
		return answer;
	}
}
