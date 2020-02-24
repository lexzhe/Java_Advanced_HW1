package ru.ifmo.Zhelobkovich.walk;

public class WalkerException extends Exception {
    WalkerException(String message, Throwable e) {
        super(message + ": " + e.getMessage(), e);
    }
}
