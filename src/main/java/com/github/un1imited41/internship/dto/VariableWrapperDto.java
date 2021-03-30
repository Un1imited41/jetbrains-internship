package com.github.un1imited41.internship.dto;


import com.github.un1imited41.internship.entities.Variable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariableWrapperDto {

    Long id;
    TemplateDto template;
    List<Variable> variables;
}
