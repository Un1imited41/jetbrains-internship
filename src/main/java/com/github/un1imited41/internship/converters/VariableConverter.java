package com.github.un1imited41.internship.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.un1imited41.internship.entities.Variable;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
@Component
public class VariableConverter implements AttributeConverter<List<Variable>, String> {

    @Autowired
    ObjectMapper mapper;

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(List<Variable> variables) {
        return mapper.writeValueAsString(variables);
    }

    @SneakyThrows
    @Override
    public List<Variable> convertToEntityAttribute(String s) {
        return mapper.readValue(s, new TypeReference<List<Variable>>() {});
    }
}
