package forum.hub.api.controller;

import forum.hub.api.domain.course.CourseCategory;
import forum.hub.api.domain.topic.TopicCreateDTO;
import forum.hub.api.domain.topic.TopicService;
import forum.hub.api.domain.topic.TopicViewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TopicControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<TopicCreateDTO> topicCreateDTOJson;

    @Autowired
    private JacksonTester<TopicViewDTO> topicViewDTOJson;

    @MockitoBean
    private TopicService service;

    @Test
    @WithMockUser
    void shouldReturnCreatedTopicWhenDataIsValid() throws Exception {
        var topicCreate = new TopicCreateDTO("Topic Title", "Sample Message", 2L, 2L);
        var topicView = new TopicViewDTO(null, "Topic Title", "Sample Message", "User", "Course", Set.of(CourseCategory.CSHARP), LocalDateTime.now(), false);
        when(service.create(any())).thenReturn(topicView);

        var mvcResponse = mvc.perform(
                post("/topics")
                    .contentType("application/json")
                    .content(topicCreateDTOJson.write(topicCreate).getJson()))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        var expectedResponse = topicViewDTOJson.write(topicView).getJson();
        assertThat(mvcResponse.getContentAsString()).isEqualTo(expectedResponse);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenDataIsInvalid() throws Exception {
        var topicCreate = new TopicCreateDTO(null, null, null, null);

        var mvcResponse = mvc.perform(
                post("/topics")
                        .contentType("application/json")
                        .content(topicCreateDTOJson.write(topicCreate).getJson()))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        var expectedErrors = new String[] {
                "{\"field\":\"title\",\"message\":\"must not be blank\"}",
                "{\"field\":\"message\",\"message\":\"must not be blank\"}",
                "{\"field\":\"authorId\",\"message\":\"must not be null\"}",
                "{\"field\":\"courseId\",\"message\":\"must not be null\"}"
        };

        assertThat(mvcResponse.getContentAsString()).contains(expectedErrors);
    }

}