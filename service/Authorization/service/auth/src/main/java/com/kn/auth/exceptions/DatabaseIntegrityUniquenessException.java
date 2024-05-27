package com.kn.auth.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class DatabaseIntegrityUniquenessException extends DataIntegrityViolationException {
    private String column;

    public DatabaseIntegrityUniquenessException(String msg) {
        super(msg);
    }

    public DatabaseIntegrityUniquenessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DatabaseIntegrityUniquenessException(String msg, Throwable cause, String column) {
        super(msg, cause);
        this.column = column;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}