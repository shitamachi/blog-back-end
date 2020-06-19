package me.guojiang.blogbackend.Exceptions;

public class FileStorageException extends RuntimeException {

    private static final long serialVersionUID = -4744941879363451573L;

    public FileStorageException(String message) {
        super(message);
    }
}
