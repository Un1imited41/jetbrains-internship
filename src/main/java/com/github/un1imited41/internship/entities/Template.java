package com.github.un1imited41.internship.entities;

import com.github.un1imited41.internship.converters.RecipientsConverter;
import com.github.un1imited41.internship.dto.TemplateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Template {

    @Id
    String templateId;
    String pattern;

    @Convert(converter = RecipientsConverter.class)
    List<String> recipients;

    public Template(TemplateDto dto) {
        this.templateId = Optional.ofNullable(dto.getTemplateId()).orElse(null);
        this.pattern = Optional.ofNullable(dto.getPattern()).orElse(null);
        this.recipients = Optional.ofNullable(dto.getRecipients()).orElse(null);
    }

}
