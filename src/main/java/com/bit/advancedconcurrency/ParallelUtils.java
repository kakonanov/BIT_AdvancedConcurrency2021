package com.bit.advancedconcurrency;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ParallelUtils {
	public static ForkJoinPool forkJoinPool;

	public ParallelUtils(int n) {
		forkJoinPool = new ForkJoinPool(n);
	}

	public void parallelFor(int l, int r, Consumer<Integer> consumer) {
		try {
			ForkJoinTask<Void> forkJoinTask = forkJoinPool.submit(new ParallelForAction(l, r, consumer));
			forkJoinTask.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void blockedParallelFor(int l, int r, Consumer<Integer> consumer, int blockSize) {
		int max = (r - l) / blockSize + 1;
		int remainder = (r - l) % blockSize;
		parallelFor(0, max, (x) -> {
			for (int i = 0; i < (x != max - 1 ? blockSize : remainder); ++i) {
				consumer.accept(x*blockSize + i);
			}
		});
	}

	public Integer[] map(Integer[] arr, int l, int r, Function<Integer, Integer> function, int blockSize) {
		Integer[] b = new Integer[r];
		Consumer<Integer> consumer = (x) -> b[x] = function.apply(arr[x]);
		blockedParallelFor(l, r, consumer, blockSize);
		return b;
	}

	public Integer[] scan(Integer[] arr, int l, int r, Consumer<Integer> consumer, int blockSize) {
		if (r - l <= blockSize) {
			return scanSerial(arr, l, r, consumer);
		}
		return arr;
	}

	public Integer[] scanSerial(Integer[] arr, int l, int r, Consumer<Integer> consumer) {
		for (int i = l; i < r; ++i) {

		}
	}

	public static class ParallelForAction extends RecursiveAction {
		private final int l;
		private final int r;
		private final Consumer<Integer> consumer;

		public ParallelForAction(int l, int r, Consumer<Integer> consumer) {
			this.l = l;
			this.r = r;
			this.consumer = consumer;
		}

		@Override
		protected void compute() {
			if (l == r - 1) {
				consumer.accept(l);
				return;
			}
			int m  = (l + r)/2;
			ForkJoinTask.invokeAll(
					new ParallelForAction(l, m, consumer),
					new ParallelForAction(m, r, consumer)
			);
		}
	}

	public static void main(String[] args) {
		ParallelUtils parallelUtils = new ParallelUtils(4);
		Integer[] arr = {1, 2, 3, 4, 5, 6, 7};
		int r = 7;
//		parallelUtils.parallelFor(0, r, (x) -> ++arr[x]);
//		parallelUtils.blockedParallelFor(0, r, (x) -> arr[x + 1] += arr[x], 3);
//		Integer[] b = parallelUtils.map(arr, 0, r, (x) -> x + 1, 3);
		parallelUtils.scan(arr, 0, r, (x) -> ++arr[x], 3);
		System.out.println(Arrays.toString(arr));
//		System.out.println(Arrays.toString(b));
	}
}
