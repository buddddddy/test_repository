package org.mdlp.service.exception;

import com.google.common.base.Joiner;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class IllegalKizStatesException extends ValidationException {

    public IllegalKizStatesException(@NotNull Collection<IllegalKizStateException> causes) {
        super(Joiner.on("; ").join(causes.stream().map(IllegalKizStateException::getMessage).collect(Collectors.toList())));
        causes.forEach(this::addSuppressed);
    }

    public IllegalKizStatesException(@NotNull IllegalKizStateException... causes) {
        this(asList(causes));
    }

    @NotNull
    public List<IllegalKizStateException> getIllegalKizStateExceptions() {
        return Stream.of(getSuppressed())
            .filter(IllegalKizStateException.class::isInstance)
            .map(IllegalKizStateException.class::cast)
            .collect(Collectors.toList());
    }

}
