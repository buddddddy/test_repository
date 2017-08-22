package org.mdlp.data.utils;

import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import wsdl.WsdlType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static wsdl.WsdlUtils.marshal;
import static wsdl.WsdlUtils.unmarshal;

@Converter(autoApply = true)
public class WsdlTypeAttributeConverter implements AttributeConverter<WsdlType, String> {

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(WsdlType attribute) {
        return marshal(attribute);
    }

    @Nullable
    @SneakyThrows
    @Override
    public WsdlType convertToEntityAttribute(@Nullable String dbData) {
        return null != dbData ? unmarshal(WsdlType.class, dbData) : null;
    }

}
