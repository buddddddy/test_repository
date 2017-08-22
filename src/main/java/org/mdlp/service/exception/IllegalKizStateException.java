package org.mdlp.service.exception;

import org.jetbrains.annotations.NotNull;

public class IllegalKizStateException extends ValidationException {

    public IllegalKizStateException(@NotNull String message) {
        super(message);
    }

}
