package com.bit.advancedconcurrency.task1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class QuickSortTest {
	int randomSeed = 13;
	int r = 10_000_000;
	int blockSize = 100_000;


	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 4})
	public void testParallel(int numThreads) {
		Random random = new Random(randomSeed);
		QuickSortParallel quickSortParallel = new QuickSortParallel(numThreads, randomSeed);
		long startTime, millis;
		int[] array = new int[r];
		for (int i = 0; i < r; ++i)
			array[i] = random.nextInt();
		int[] check = array.clone();
		startTime = System.nanoTime();
		quickSortParallel.qsort(array, blockSize);
		millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
		System.out.println("Total execution time - parallel with " + numThreads + " threads: " + (millis) + "ms");

		Arrays.sort(check);
		assertArrayEquals(check, array);
	}


	@Test
	public void testSerial() {
		Random random = new Random(randomSeed);
		QuickSortSerial quickSortSerial = new QuickSortSerial(randomSeed);
		long startTime, millis;
		int[] array = new int[r];
		for (int i = 0; i < r; ++i)
			array[i] = random.nextInt();
		int[] check = array.clone();
		startTime = System.nanoTime();
		int[] res = quickSortSerial.qsort(array);
		millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
		System.out.println("Total execution time - serial: " + (millis) + "ms");

		Arrays.sort(check);
		assertArrayEquals(check, res);
	}
}