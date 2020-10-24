package com.semicolondevop.suite.model.techStack;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 25/10/2020 - 12:12 AM
 * @project com.semicolondevop.suite.model.techStack in ds-suite
 */
//@Converter(autoApply = true)
//public class TechStackConverter implements AttributeConverter<TechStackType,String> {
//    @Override
//    public String convertToDatabaseColumn(TechStackType techStackType) {
//        if (techStackType == null) {
//            return null;
//        }
//        return techStackType.getCode();
//    }
//
//    @Override
//    public TechStackType convertToEntityAttribute(String code) {
//        if (code == null) {
//            return null;
//        }
//
//        return Stream.of(TechStackType.values())
//                .filter(c -> c.getCode().equals(code))
//                .findFirst()
//                .orElseThrow(IllegalArgumentException::new);
//    }
//}
