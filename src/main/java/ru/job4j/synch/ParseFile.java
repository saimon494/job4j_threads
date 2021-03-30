package ru.job4j.synch;

import java.io.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ParseFile {

    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    private String filterContent(String line, Predicate<Integer> filter) {
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < line.length(); index++) {
            char c = line.charAt(index);
            if (filter.test((int) c)) {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    private String getContent(Predicate<Integer> filter) throws IOException {
        String content = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            content = reader.lines()
                    .map(str -> filterContent(str, filter))
                    .collect(Collectors.joining(System.lineSeparator()));
        }
        return content;
    }

    public synchronized String getContent() throws IOException {
        return getContent(c -> true);
    }

    public synchronized String getContentWithoutUnicode() throws IOException {
        return getContent(c -> c < 0x80);
    }
}
