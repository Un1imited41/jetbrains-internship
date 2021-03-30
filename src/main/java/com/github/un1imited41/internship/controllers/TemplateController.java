package com.github.un1imited41.internship.controllers;


import com.github.un1imited41.internship.dto.TemplateDto;
import com.github.un1imited41.internship.entities.Template;
import com.github.un1imited41.internship.entities.VariableWrapper;
import com.github.un1imited41.internship.services.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("template")
@CrossOrigin("*")
public class TemplateController {

    @Autowired
    TemplateService templateService;

    @PostMapping("save")
    public Template save(@RequestBody Template template) {
        return templateService.saveTemplate(template);
    }

    @PostMapping("send-message")
    public void send(@RequestBody VariableWrapper variable) {
        templateService.saveVariables(variable);
    }

    @GetMapping("{id}/get-variables")
    public List<String> getVariables(@PathVariable("id") String templateId) {
        return templateService.getVariables(templateId);
    }

}
