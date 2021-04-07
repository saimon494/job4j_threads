package ru.job4j.cas;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CacheTest {

    @Test
    public void whenAddUpdateThenException() throws InterruptedException {
        AtomicReference<Exception> ex = new AtomicReference<>(new Exception());
        Cache cache = new Cache();
        Base base = new Base(1, 0);
        cache.add(base);
        base.setName("Name");
        assertThat(base.getName(), is("Name"));
        Thread thread0 = new Thread(
                () -> {
                    base.setName("Name0");
                    try {
                        cache.update(base);
                    } catch (Exception e) {
                        ex.set(e);
                    }
                }
        );
        Thread thread1 = new Thread(
                () -> {
                    base.setName("Name1");
                    try {
                        cache.update(base);
                    } catch (Exception e) {
                        ex.set(e);
                    }
                }
        );
        thread0.start();
        thread1.start();
        thread0.join();
        thread1.join();
        assertThat(ex.get().getMessage(), is("Versions are not equal"));
    }
}