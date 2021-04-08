package ru.job4j.pool;

import ru.job4j.wait.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {

    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(5);

    public ThreadPool() {
        int size = Runtime.getRuntime().availableProcessors();
        System.out.printf("available processors: %d\n", size);
        for (int i = 0; i < size; i++) {
            threads.add(new Thread(
                    () -> {
                        System.out.printf("%s start\n", Thread.currentThread().getName());
                        while (!Thread.currentThread().isInterrupted()) {
                            try {
                                System.out.printf(
                                        "%s working with\n", Thread.currentThread().getName());
                                tasks.poll().run();
                            } catch (InterruptedException e) {
                                System.out.printf(
                                        "%s interrupt\n", Thread.currentThread().getName());
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
            ));
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public void work(Runnable job) throws InterruptedException {
        tasks.offer(job);
    }

    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool();
        threadPool.work(() -> System.out.println("job1"));
        threadPool.work(() -> System.out.println("job2"));
        threadPool.work(() -> System.out.println("job3"));
        threadPool.work(() -> System.out.println("job4"));
        threadPool.work(() -> System.out.println("job5"));
        threadPool.shutdown();
    }
}