package ru.job4j.wait;

public class MultiCountBarrier {

    public static void main(String[] args) {
        CountBarrier countBarrier = new CountBarrier(30);

        Thread counter = new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            for (int i = 0; i <= 30; i++) {
                countBarrier.count();
            }
        });
        Thread thread1 = new Thread(
                () -> {
                    countBarrier.await();
                    System.out.println(Thread.currentThread().getName());
                },
                "thread1 wait"
        );
        Thread thread2 = new Thread(
                () -> {
                    countBarrier.await();
                    System.out.println(Thread.currentThread().getName());
                },
                "thread2 wait"
        );
        counter.start();
        thread1.start();
        thread2.start();
        System.out.println("counter state " + counter.getState());
        System.out.println("thread1 state " + thread1.getState());
        System.out.println("thread2 state " + thread2.getState());
    }
}