package com.github.un1imited41.internship.converters;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RecipientsConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> recipients) {
        return recipients.stream().reduce((s1, s2) -> s1 + ", " + s2).get();
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return Arrays.stream(s.split(", ")).collect(Collectors.toList());
    }
}
