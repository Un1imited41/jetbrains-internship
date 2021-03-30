package com.github.un1imited41.internship.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDto {

    String templateId;
    String pattern;
    List<String> recipients;

}
