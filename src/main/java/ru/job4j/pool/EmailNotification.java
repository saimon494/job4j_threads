package ru.job4j.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {

    private final ExecutorService pool =
            Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors());

    public void emailTo(User user) {
        pool.submit(
                () -> {
                    String subject = String.format(
                            "Notification %s to email %s.\n",
                            user.getUsername(), user.getEmail()
                    );
                    String body = String.format(
                            "Add a new event to %s",
                            user.getUsername()
                    );
                    send(subject, body, user.getEmail());
                }
        );
    }

    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void send(String subject, String body, String email) {
        System.out.printf("%s%s", subject, body);
    }

    public static void main(String[] args) {
        EmailNotification en = new EmailNotification();
        en.emailTo(new User("User1", "user1@mail.ru"));
        en.close();
    }
}
