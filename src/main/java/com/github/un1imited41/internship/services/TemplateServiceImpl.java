package com.github.un1imited41.internship.services;

import com.github.un1imited41.internship.dto.MessageWrapper;
import com.github.un1imited41.internship.dto.VariableWrapperDto;
import com.github.un1imited41.internship.entities.Template;
import com.github.un1imited41.internship.entities.Variable;
import com.github.un1imited41.internship.entities.VariableWrapper;
import com.github.un1imited41.internship.readers.VariableReader;
import com.github.un1imited41.internship.repositories.TemplateRepository;
import com.github.un1imited41.internship.repositories.VariableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    TemplateRepository templateRepository;

    @Autowired
    VariableRepository variableRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    VariableReader variableReader;

    @Autowired
    ApplicationEventPublisher publisher;

    @Override
    public Template saveTemplate(Template template) {
        return templateRepository.save(template);
    }

    @Override
    @Transactional
    public VariableWrapper saveVariables(VariableWrapper variable) {
        VariableWrapper variableWrapper = variableRepository.save(variable);
        publisher.publishEvent(variable);
        return variableWrapper;
    }

    @Override
    @TransactionalEventListener
    public String send(VariableWrapper variable) {
        return send(variableReader.getVariableById(variable.getId()));
    }

    public String send(VariableWrapperDto variableWrapper) {
        String message = variableWrapper.getTemplate().getPattern();
        for (Variable variable : variableWrapper.getVariables()) {
            message = message.replaceAll(String
                    .format("\\$%s\\$", variable.getName()), variable.getValue());
        }
        MessageWrapper messageDto = new MessageWrapper(message);
        variableWrapper.getTemplate().getRecipients().forEach(recipient -> {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(recipient, messageDto, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return message;
    }

    @Override
    public List<String> getVariables(String templateId) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Шаблон не найден"));
        Matcher matcher = Pattern.compile("\\$\\w+\\$").matcher(template.getPattern());
        return matcher.results().map(e -> e.group().replace("$", "")).collect(Collectors.toList());
    }

    @Scheduled(cron = "${cron.expression}")
    public void sendAllMessages() {
        variableReader.getAllVariables().forEach(batch -> batch.forEach(this::send));
    }
}
