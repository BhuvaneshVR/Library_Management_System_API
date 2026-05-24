package com.company.library.converter;

import com.company.library.enums.MemberStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MemberStatusConverter implements AttributeConverter<MemberStatus, String> {

    @Override
    public String convertToDatabaseColumn(MemberStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public MemberStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return MemberStatus.valueOf(dbData.trim().toUpperCase());
    }
}
