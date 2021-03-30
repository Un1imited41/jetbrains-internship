package com.github.un1imited41.internship.services;

import com.github.un1imited41.internship.entities.Template;
import com.github.un1imited41.internship.entities.VariableWrapper;

public interface TemplateService {

    Template saveTemplate(Template template);

    VariableWrapper saveVariables(VariableWrapper variable);

    String send(VariableWrapper variable);

}
