package ru.job4j.synch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class SaveFile {

    private final String content;

    private final File file;

    public SaveFile(String content, File file) {
        this.content = content;
        this.file = file;
    }

    public synchronized void saveContent(String content) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(file))) {
            w.write(content);
        }
    }
}
