package ru.job4j.cas;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CASCountTest {

    @Test
    public void when2ThreadIncrement() throws InterruptedException {
        CASCount count = new CASCount();

        Thread thread0 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                count.increment();
            }
        });
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                count.increment();
            }
        });
        thread0.start();
        thread1.start();
        thread0.join();
        thread1.join();
        assertThat(count.get(), is(100));
    }
}