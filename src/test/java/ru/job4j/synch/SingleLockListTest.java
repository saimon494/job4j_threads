package ru.job4j.synch;

import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SingleLockListTest {

    @Test
    public void whenAddThenIterate() throws InterruptedException {
        SingleLockList<Integer> list = new SingleLockList<>();
        Thread first = new Thread(() -> list.add(1));
        Thread second = new Thread(() -> list.add(2));
        first.start();
        second.start();
        first.join();
        second.join();
        assertThat(list.get(0), is(1));
        assertThat(list.get(1), is(2));
        Set<Integer> rsl = new TreeSet<>();
        list.iterator().forEachRemaining(rsl::add);
        list.add(3);
        assertThat(rsl, is(Set.of(1, 2)));
    }
}