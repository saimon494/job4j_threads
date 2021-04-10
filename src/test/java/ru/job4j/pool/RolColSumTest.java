package ru.job4j.pool;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class RolColSumTest {

    private final int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    private final StringBuilder expected = new StringBuilder()
            .append("[Sums{rowSum=6, colSum=12}, ")
            .append("Sums{rowSum=15, colSum=15}, ")
            .append("Sums{rowSum=24, colSum=18}]");

    @Test
    public void whenSum() {
        assertEquals(expected.toString(), Arrays.toString(RolColSum.sum(matrix)));
    }

    @Test
    public void whenAsyncSum() throws ExecutionException, InterruptedException {
        assertEquals(expected.toString(), Arrays.toString(RolColSum.asyncSum(matrix)));
    }
}