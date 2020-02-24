package ru.ifmo.Zhelobkovich.walk;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class HashingFileVisitor extends SimpleFileVisitor<Path> {
    private BufferedWriter out;

    HashingFileVisitor(BufferedWriter out) {
        this.out = out;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        int fileHash;
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(file))) {
            fileHash = hash(bufferedInputStream);
        } catch (IOException e) {
            fileHash = 0;
        }
        return writeHash(fileHash, file.toString());
    }

    private int hash(BufferedInputStream bufferedInputStream) throws IOException {
        byte[] buffer = new byte[8192];
        int h = 0x811c9dc5;
        int bufferReadSize;
        while ((bufferReadSize = bufferedInputStream.read(buffer)) >= 0) {
            for (int i = 0; i < bufferReadSize; i++) {
                h = (h * 0x01000193) ^ (buffer[i] & 0xff);
            }
        }
        return h;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return writeHash(0, file.toString());
    }

    public FileVisitResult writeHash(int fileHash, String path) throws IOException {
        out.write(String.format("%08x %s%n", fileHash, path));
        return FileVisitResult.CONTINUE;
    }
}
