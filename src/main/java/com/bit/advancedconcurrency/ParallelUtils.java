package com.bit.advancedconcurrency;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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
		int blocksNum = (r - l) / blockSize + 1;
		int remainder = (r - l) % blockSize;
		parallelFor(0, blocksNum, (x) -> {
			for (int i = 0; i < (x != blocksNum - 1 ? blockSize : remainder); ++i) {
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

	public Integer[] scan(Integer[] arr, int l, int r, BiFunction<Integer, Integer, Integer> function, int blockSize) {
		if (r - l <= blockSize) {
			return scanSerial(arr, l, r, function);
		}
		int blocksNum = (r - l) / blockSize + 1;
		Integer[] sums = new Integer[blocksNum];
		sums[0] = 0;
		blockedParallelFor(0, blocksNum - 1, (x) -> {
			sums[x + 1] = reduceSerial(arr,
					x * blockSize,
					(x + 1) * blockSize,
					Integer::sum);
		}, blockSize);
		Integer[] scannedSums = scan(sums, 1, blocksNum, function, blockSize);
		System.out.println(Arrays.toString(scannedSums));
		Integer[] answer = new Integer[r - l + 2];
		blockedParallelFor(l - 1, r + 1, (x) -> {
			System.out.println(x);
			if (x % blockSize == 0)
				answer[x] = scannedSums[x/blockSize];
			else
				answer[x] = function.apply(answer[x - 1], arr[x - 1]);
		}, blockSize);
		return answer;
	}

	public Integer[] filter(Integer[] arr, int l, int r, Predicate<Integer> predicate) {
		Integer[] flags = map(arr, l, r, (x) -> {

		});
		return flags;
	}

	public Integer[] scanSerial(Integer[] arr, int l, int r, BiFunction<Integer, Integer, Integer> biFunction) {
		Integer[] b = new Integer[r - l + 1];
		b[0] = arr[l - 1];
		for (int i = l; i < r; ++i) {
			b[i - l + 1] = biFunction.apply(arr[i], b[i - l]);
		}
		return b;
	}

	public Integer reduceSerial(Integer[] arr, int l, int r, BiFunction<Integer, Integer, Integer> biFunction) {
		int res = 0;
		for (int i = l; i < r; ++i) {
			res = biFunction.apply(arr[i], res);
		}
		return res;
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
//		Integer[] b = parallelUtils.scan(arr, 1, r, Integer::sum, 3);
		Integer[] b = parallelUtils.filter(arr, 0, r,);
		System.out.println(Arrays.toString(arr));
		System.out.println(Arrays.toString(b));
	}
}
