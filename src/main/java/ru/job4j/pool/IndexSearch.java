package ru.job4j.pool;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class IndexSearch<T> extends RecursiveTask<Integer> {

    private final T[] array;
    private final T elem;
    private final int startIndex;
    private final int endIndex;

    public IndexSearch(T[] array, T elem, int startIndex, int endIndex) {
        this.array = array;
        this.elem = elem;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    protected Integer compute() {
        if (endIndex - startIndex < 10) {
            return findIndex();
        }
        int midIndex = startIndex + (endIndex - startIndex) / 2;
        IndexSearch<T> leftPart = new IndexSearch<>(array, elem, startIndex, midIndex);
        IndexSearch<T> rightPart = new IndexSearch<>(array, elem, midIndex + 1, endIndex);
        leftPart.fork();
        rightPart.fork();
        int leftIndex = leftPart.join();
        int rightIndex = rightPart.join();
        return leftIndex != -1 ? leftIndex : rightIndex;
    }

    private int findIndex() {
        return Arrays.asList(array).indexOf(elem);
    }

    public static <T> int findIndex(T[] array, T elem) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new IndexSearch<>(array, elem, 0, array.length - 1));
    }
}
