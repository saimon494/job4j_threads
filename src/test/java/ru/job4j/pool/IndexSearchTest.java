package ru.job4j.pool;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class IndexSearchTest {

    private final Integer[] array1 = {5, 8, 10, 13, 55, 3, 4};
    private final Integer[] array2 = {2, 1, 16};

    @Test
    public void whenFindAndNot() {
        int index1 = IndexSearch.findIndex(array1, 13);
        int index2 = IndexSearch.findIndex(array1, 55);
        int index3 = IndexSearch.findIndex(array2, 13);
        assertThat(index1, is(3));
        assertThat(index2, is(4));
        assertThat(index3, is(-1));
    }
}