package ru.job4j.concurrent;

public class ThreadState {

    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.printf("Thread name: %s\n", Thread.currentThread().getName())
        );
        Thread second = new Thread(
                () -> System.out.printf("Thread name: %s\n", Thread.currentThread().getName())
        );
        System.out.printf("Before start:\n%s state: %s, %s state: %s\n",
                first.getName(), first.getState(),
                second.getName(), second.getState());
        first.start();
        second.start();
        System.out.println("After start:");
        while (first.getState() != Thread.State.TERMINATED
        || second.getState() != Thread.State.TERMINATED) {
            System.out.printf("%s state: %s, %s state: %s\n",
                    first.getName(), first.getState(),
                    second.getName(), second.getState());
        }
        System.out.printf("After terminated:\n%s state: %s, %s state: %s\n",
                first.getName(), first.getState(),
                second.getName(), second.getState());
        System.out.println("Completed");
    }
}