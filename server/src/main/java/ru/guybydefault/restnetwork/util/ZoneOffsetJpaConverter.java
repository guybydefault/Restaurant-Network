package ru.guybydefault.restnetwork.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.ZoneOffset;

@Converter
public class ZoneOffsetJpaConverter implements AttributeConverter<ZoneOffset, String> {
    @Override
    public String convertToDatabaseColumn(ZoneOffset attribute) {
        return attribute.toString();
    }

    @Override
    public ZoneOffset convertToEntityAttribute(String dbData) {
        return ZoneOffset.of(dbData);
    }
}
