package com.bit.advancedconcurrency.task1;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

public class QuickSortParallel {
	public static final Random random = new Random();
	public static ForkJoinPool forkJoinPool;
	public static ParallelUtils parallelUtils;

	public QuickSortParallel(int n, int randomSeed) {
		forkJoinPool = new ForkJoinPool(n);
		parallelUtils = new ParallelUtils(forkJoinPool);
		random.setSeed(randomSeed);
	}

	public void qsort(int[] array, int blockSize) {
		try {
			ForkJoinTask<Void> forkJoinTask = forkJoinPool.submit(new QuickSortAction(array, blockSize));
			forkJoinTask.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static class QuickSortAction extends RecursiveAction {
		private final int[] array;
		private final int blockSize;


		public QuickSortAction(int[] array, int blockSize) {
			this.array = array;
			this.blockSize = blockSize;
		}

		@Override
		protected void compute() {
			if (array.length > blockSize) {
				int index = random.nextInt(array.length);
				int[] left = parallelUtils.filter(array, (x) -> x < array[index], blockSize);
				int[] right = parallelUtils.filter(array, (x) -> x > array[index], blockSize);
				ForkJoinTask.invokeAll(
						new QuickSortAction(left, blockSize),
						new QuickSortAction(right, blockSize)
				);
				int l = left.length;
				int r = right.length;
				int delta = array.length - l - r;
				concatenate(array, left, right, delta, array[index]);
			}
			else {
				Arrays.sort(array);
			}
		}

		private void concatenate(int[] array, int[] left, int[] right, int delta, int pivot) {
			System.arraycopy(left, 0, array, 0, left.length);
			for (int i = left.length; i < left.length + delta; ++i) {
				array[i] = pivot;
			}
			System.arraycopy(right, 0, array, left.length + delta, right.length);
		}
	}
}
