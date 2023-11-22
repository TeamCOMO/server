package project.como.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentCreateResponseDto;
import project.como.domain.comment.service.CommentService;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;

import static project.como.domain.post.model.Category.Study;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CommentService commentService;
    @BeforeAll
    static void init(){
        Post post = Post.builder()
                .id(9999L)
                .title("test용 게시물")
                .body("test용 본문")
                .category(Study)
                .build();
    }
    @Test
    @DisplayName("댓글 작성하기")
    @WithMockUser // 401 에러 방지
    void createComment() throws Exception {
        //given
        Post post = Post.builder()
                .id(9999L)
                .title("test용 게시물")
                .body("test용 본문")
                .category(Study)
                .build();
        Long postId = post.getId();
        CommentCreateRequestDto request = CommentCreateRequestDto.builder()
                .body("댓글입니다!")
                .build();
        BDDMockito.given(commentService.create("user",9999L,request))
                        .willReturn(
                                CommentCreateResponseDto.builder()
                                        .parentId(request.getParentId())
                                        .id(2162L)
                                        .build()
                        );

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/post/9999/comment", postId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // 403 에러 방지 - Security 제외
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.parentId").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()); // test 상에서만 responseDto가 null로 나옴. 해당 문제 추후 해결
    }

}