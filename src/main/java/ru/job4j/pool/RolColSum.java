package ru.job4j.pool;

import org.openjdk.jmh.annotations.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)

public class RolColSum {
    public static class Sums {
        private final int rowSum;
        private final int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        @Override
        public String toString() {
            return "Sums{"
                    + "rowSum=" + rowSum
                    + ", colSum=" + colSum
                    + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Sums sums = (Sums) o;
            return rowSum == sums.rowSum
                    && colSum == sums.colSum;
        }

        @Override
        public int hashCode() {
            return Objects.hash(rowSum, colSum);
        }
    }

    private final int[][] matrix = {
            {27, 88, 78, 84, 100, 25, 60, 33, 62, 47},
            {44, 85, 8, 60, 12, 40, 82, 19, 27, 27},
            {26, 3, 60, 11, 6, 52, 32, 37, 78, 63},
            {23, 25, 65, 98, 5, 79, 46, 49, 73, 57},
            {58, 6, 12, 41, 56, 40, 38, 9, 44, 78},
            {19, 19, 57, 26, 15, 87, 29, 72, 61, 58},
            {72, 97, 28, 3, 41, 99, 73, 29, 43, 79},
            {58, 56, 56, 47, 14, 99, 90, 35, 18, 85},
            {82, 12, 19, 87, 28, 48, 39, 11, 96, 6},
            {77, 38, 7, 88, 93, 35, 57, 45, 44, 59}
    };

    private static Sums getSums(int[][] matrix, int index) {
        int rowSum = 0;
        int colSum = 0;
        for (int i = 0; i < matrix.length; i++) {
            rowSum += matrix[index][i];
            colSum += matrix[i][index];
        }
        return new Sums(rowSum, colSum);
    }

    public static Sums[] sum(int[][] matrix) {
        Sums[] rsl = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            rsl[i] = getSums(matrix, i);
        }
        return rsl;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        Sums[] rsl = new Sums[matrix.length];
        Map<Integer, CompletableFuture<Sums>> futures = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            futures.put(i, getTask(matrix, i));
        }
        for (Integer key : futures.keySet()) {
            rsl[key] = futures.get(key).get();
        }
        return rsl;
    }

    private static CompletableFuture<Sums> getTask(int[][] matrix, int index) {
        return CompletableFuture.supplyAsync(() -> getSums(matrix, index));
    }

    @Benchmark
    public void testSum() {
        sum(matrix);
    }

    @Benchmark
    public void testAsyncSum() throws ExecutionException, InterruptedException {
        asyncSum(matrix);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
