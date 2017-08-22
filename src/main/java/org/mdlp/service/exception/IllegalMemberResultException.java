package org.mdlp.service.exception;

import org.jetbrains.annotations.NotNull;

public class IllegalMemberResultException extends Exception {

    public IllegalMemberResultException(@NotNull String message) {
        super(message);
    }
}
