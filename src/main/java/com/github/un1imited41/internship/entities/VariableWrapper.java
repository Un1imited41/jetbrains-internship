package com.github.un1imited41.internship.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.un1imited41.internship.converters.VariableConverter;
import com.github.un1imited41.internship.deserializers.VariableDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariableWrapper {

    @Id
    @GeneratedValue
    Long id;
    String templateId;

    @Convert(converter = VariableConverter.class)
    @JsonDeserialize(using = VariableDeserializer.class)
    List<Variable> variables;

}
