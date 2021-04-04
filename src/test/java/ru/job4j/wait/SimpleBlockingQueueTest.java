package ru.job4j.wait;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {

    @Test
    public void whenOfferThenPoll() throws InterruptedException {

        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();

        Thread producer = new Thread(() -> {
            for (int i = 0; i <= 10; i++) {
                queue.offer(i);
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i <= 5; i++) {
                try {
                    queue.poll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        consumer.start();
        producer.start();
        consumer.join();
        assertThat(queue.getSize(), is(5));
        assertEquals(6, queue.poll().intValue());
    }
}