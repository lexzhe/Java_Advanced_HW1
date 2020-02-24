package ru.ifmo.Zhelobkovich.walk;

import java.io.*;
import java.nio.file.*;

public class RecursiveWalk {
    public static Path newPath(String filePath, String errorMessage) throws WalkerException {
        try {
            return Paths.get(filePath);
        } catch (InvalidPathException e) {
            throw new WalkerException(errorMessage + ": " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("Invalid number of arguments");
            return;
        }
        if (args[0] == null) {
            System.err.println("Null first arguments");
            return;
        }
        if (args[1] == null) {
            System.err.println("Null second arguments");
            return;
        }
        try {
            Path input = newPath(args[0], "Invalid input path");
            Path output = newPath(args[1], "Invalid output path");
            run(input, output);
        } catch (WalkerException e) {
            System.err.println(e.getMessage());
        }

    }

    private static void run(Path input, Path output) throws WalkerException {
        try (BufferedReader in = Files.newBufferedReader(input)) {
            try (BufferedWriter out = Files.newBufferedWriter(output)) {
                try {
                    HashingFileVisitor hashing = new HashingFileVisitor(out);
                    String currentLine;
                    while ((currentLine = in.readLine()) != null) {
                        try {
                            try {
                                Path path = Paths.get(currentLine);
                                Files.walkFileTree(path, hashing);
                            } catch (InvalidPathException e) {
                                hashing.writeHash(0, currentLine);
                            }
                        } catch (IOException e) {
                            throw new WalkerException("Failing during writing output", e);
                        }
                    }
                } catch (IOException e) {
                    throw new WalkerException("Failure while reading input file", e);
                }
            } catch (final IOException e) {
                throw new WalkerException("Failure in output", e);
            }
        } catch (final IOException e) {
            throw new WalkerException("Failure in input", e);
        }
    }
}

