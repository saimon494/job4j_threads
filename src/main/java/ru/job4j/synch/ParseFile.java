package ru.job4j.synch;

import java.io.*;
import java.util.function.Predicate;

public final class ParseFile {

    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    private String getContent(Predicate<Integer> pred) {
        StringBuffer buffer = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int data;
            while ((data = reader.read()) > 0) {
                if (pred.test(data)) {
                    buffer.append((char) data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

//    private String getContent(Predicate<Character> pred) throws IOException {
//        String content = "";
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            content = reader.lines()
//                    .flatMapToInt(CharSequence::chars)
//                    .mapToObj(c -> (char) c)
//                    .filter(pred)
//                    .map(String::valueOf)
//                    .collect(Collectors.joining());
//        }
//        return content;
//    }

    public synchronized String getContent() throws IOException {
        return getContent(c -> true);
    }

    public synchronized String getContentWithoutUnicode() throws IOException {
        return getContent(c -> c < 0x80);
    }

    public static void main(String[] args) {
        ParseFile parseFile = new ParseFile(new File("readme.md"));
        String content = "";
        SaveFile saveFile = new SaveFile(content, new File("readme2.md"));
        Thread thread0 = new Thread(
                () -> {
                    try {
                        saveFile.saveContent(parseFile.getContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName());
                });
        Thread thread1 = new Thread(
                () -> {
                    try {
                        saveFile.saveContent(parseFile.getContentWithoutUnicode());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName());
                });
        thread0.start();
        thread1.start();
    }
}
