package org.mdlp.data.kiz;

import org.mdlp.data.document.Operation;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Collection;
import java.util.Optional;

import static org.mdlp.data.document.Operation.*;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public enum KizStatus {

    PACKED(1, register_end_packing),
    RELEASED(2, register_control_samples, register_product_emission, move_order_s, receive_order_s, move_order_cancel),
    SOLD(3, retail_sale),;

    public static Optional<KizStatus> findByOperation(Operation operation) {
        for (KizStatus status : KizStatus.values()) {
            if (status.operations.contains(operation)) return Optional.of(status);
        }
        return Optional.empty();
    }

    public static Optional<KizStatus> findById(int id) {
        for (KizStatus status : KizStatus.values()) {
            if (status.id == id) return Optional.of(status);
        }
        return Optional.empty();
    }

    @Getter
    private final int id;

    @NotNull
    @Getter
    private final Collection<Operation> operations;

    KizStatus(int id, Operation... operations) {
        this.id = id;
        this.operations = unmodifiableList(asList(operations));
    }

    @Converter(autoApply = true)
    public static class AttributeConverterImpl implements AttributeConverter<KizStatus, String> {
        @NotNull
        @Override
        public String convertToDatabaseColumn(@NotNull KizStatus attribute) {
            return attribute.name().toLowerCase();
        }

        @Override
        public KizStatus convertToEntityAttribute(@Nullable String dbData) {
            return null != dbData ? KizStatus.valueOf(dbData.toUpperCase()) : null;
        }
    }

}
