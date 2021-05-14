package com.bit.advancedconcurrency.task1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParallelUtils {
	public ForkJoinPool forkJoinPool;

	public ParallelUtils(ForkJoinPool forkJoinPool) {
		this.forkJoinPool = forkJoinPool;
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
					new ParallelUtils.ParallelForAction(l, m, consumer),
					new ParallelUtils.ParallelForAction(m, r, consumer)
			);
		}
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
		parallelFor(0, blocksNum, (x) -> {
			int begin = x * blockSize;
			for (int i = 0; i < blockSize; ++i) {
				if (begin + i >= r)
					return;
				consumer.accept(begin + i);
			}
		});
	}

	public int[] map(int[] arr, Function<Integer, Integer> function, int blockSize) {
		int[] b = new int[arr.length];
		Consumer<Integer> consumer = (x) -> b[x] = function.apply(arr[x]);
		blockedParallelFor(0, arr.length, consumer, blockSize);
		return b;
	}

	public int[] scan(int[] arr, int l, int r, BiFunction<Integer, Integer, Integer> function, int blockSize) {
		if (r - l <= blockSize) {
			return SerialUtils.scan(arr, l, r, function);
		}
		int blocksNum = (r - l) / blockSize + 1;
		int[] sums = new int[blocksNum];
		sums[0] = 0;
		blockedParallelFor(
				0, sums.length - 1,
				(x) -> sums[x + 1] = SerialUtils.reduce(arr,
						x * blockSize,
						(x + 1) * blockSize,
						Integer::sum),
				blockSize
		);
		int[] tmpSums = new int[sums.length - 1];
		System.arraycopy(sums, 1, tmpSums, 0, tmpSums.length);
		int[] scannedSums = scan(tmpSums, 0, tmpSums.length, function, blockSize);
		int[] answer = new int[r - l + 1];
		blockedParallelFor(0, answer.length, (x) -> {
			if (x % blockSize == 0)
				answer[x] = scannedSums[(x / blockSize)];
			else
				answer[x] = function.apply(answer[x - 1], arr[x - 1]);
		}, blockSize);
		return answer;
	}

	public int[] filter(int[] arr, Predicate<Integer> predicate, int blockSize) {
		int[] flags = map(arr, (x) -> predicate.test(x) ? 1 : 0, blockSize);
		int[] sums = scan(flags,0, arr.length, Integer::sum, blockSize);
		int[] answer = new int[sums[sums.length - 1]];
		blockedParallelFor(
				0, arr.length,
				(x) -> {
					if (flags[x] == 1)
						answer[sums[x]] = arr[x];
				},
				blockSize);
		return answer;
	}
}
