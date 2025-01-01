package forum.hub.api.controller;

import forum.hub.api.domain.course.CourseCategory;
import forum.hub.api.domain.topic.TopicCreateDTO;
import forum.hub.api.domain.topic.TopicDetailDTO;
import forum.hub.api.domain.topic.TopicService;
import forum.hub.api.domain.topic.TopicViewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private JacksonTester<TopicDetailDTO> topicDetailDTOJson;

    @Autowired
    private JacksonTester<TopicViewDTO> topicViewDTOJson;

    @Autowired
    private JacksonTester<Page<TopicViewDTO>> topicViewDTOPageJson;

    @MockitoBean
    private TopicService service;

    @Test
    @WithMockUser
    void create_ShouldReturnCreatedTopicWhenDataIsValid() throws Exception {
        var topicCreate = new TopicCreateDTO("Topic Title", "Sample Message", 2L, 2L);
        var topicView = createTopicView();
        when(service.create(any())).thenReturn(topicView);

        mvc.perform(post("/topics")
                .contentType("application/json")
                .content(topicCreateDTOJson.write(topicCreate).getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(topicViewDTOJson.write(topicView).getJson()));
    }

    @Test
    @WithMockUser
    void create_ShouldReturnBadRequestWhenDataIsInvalid() throws Exception {
        var topicCreate = new TopicCreateDTO(null, null, null, null);

        var mvcResponse = mvc.perform(
                        post("/topics")
                                .contentType("application/json")
                                .content(topicCreateDTOJson.write(topicCreate).getJson()))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        var expectedResponse = """
                [
                  {"field":"title","message":"must not be blank"},
                  {"field":"authorId","message":"must not be null"},
                  {"field":"courseId","message":"must not be null"},
                  {"field":"message","message":"must not be blank"}
                ]
                """.trim();

        assertEquals(expectedResponse, mvcResponse.getContentAsString(), false);
    }

    @Test
    @WithMockUser
    void list_ShouldReturnTopicsPageWhenRequested() throws Exception {
        var page = new PageImpl<>(List.of(createTopicView(), createTopicView()));
        when(service.getTopics(any(), any(), any())).thenReturn(page);

        mvc.perform(get("/topics"))
                .andExpect(status().isOk())
                .andExpect(content().json(topicViewDTOPageJson.write(page).getJson()));
    }

    @Test
    @WithMockUser
    void detail_ShouldReturnTopicDetailWhenRequested() throws Exception {
        var topic = new TopicDetailDTO(null, "Topic Title", "Sample Message", "User", List.of(), "Course", Set.of(CourseCategory.CSHARP), LocalDateTime.now(), false);
        when(service.getTopicDetailById(any())).thenReturn(topic);

        mvc.perform(get("/topics/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(topicDetailDTOJson.write(topic).getJson()));
    }

    private TopicViewDTO createTopicView() {
        return new TopicViewDTO(
                null,
                "Topic Title",
                "Sample Message",
                "User",
                "Course",
                Set.of(CourseCategory.CSHARP),
                LocalDateTime.now(),
                false
        );
    }
}