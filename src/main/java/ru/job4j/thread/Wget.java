package ru.job4j.thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {

    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("pom_tmp.xml")) {
            byte[] dataBuffer = new byte[speed];
            int bytesRead;
            long startTime = System.currentTimeMillis();
            long leftTime;
            int count = 0;
            String[] dots = {".", "..", "...", "...."};
            while ((bytesRead = in.read(dataBuffer, 0, speed)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                leftTime = System.currentTimeMillis() - startTime;
                System.out.printf("\rSpeed limit = %d, loading%s",
                        (speed - leftTime), dots[(count++) % 4]);
                if (leftTime < 1000) {
                    Thread.sleep(speed - leftTime);
                }
                startTime = System.currentTimeMillis();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Not enough args");
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}
// args https://github.com/saimon494/job4j_threads/blob/main/pom.xml 1024