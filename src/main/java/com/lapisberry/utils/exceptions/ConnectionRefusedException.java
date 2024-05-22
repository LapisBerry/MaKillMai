package com.lapisberry.utils.exceptions;

import java.io.Serial;

public class ConnectionRefusedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5965444325760963522L;

    public ConnectionRefusedException(String message) {
        super(message);
    }
}
