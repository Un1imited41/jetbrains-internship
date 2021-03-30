package com.github.un1imited41.internship;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.un1imited41.internship.dto.TemplateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class InternshipApplicationTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Transactional
    void testSaveTemplate() throws Exception {
        mvc.perform(post("/template/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(new TemplateDto("testTemplate", "test", null))))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.templateId").value("testTemplate"))
                .andReturn();
    }

    @Test
    void testSendMessage() throws Exception {
        mvc.perform(post("/template/send-message")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"templateId\": \"testTemplate\",\"variables\": [{\"teamName\": \"Analytics Platform\"}]}"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();
    }

}
