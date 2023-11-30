package project.como.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentCreateResponseDto;
import project.como.domain.comment.dto.CommentDetailDto;
import project.como.domain.comment.dto.CommentResponseDto;
import project.como.domain.comment.model.Comment;
import project.como.domain.comment.service.CommentService;
import project.como.domain.post.model.Post;
import project.como.global.common.exception.DtoNotValidException;

import java.util.List;

import static project.como.domain.post.model.Category.Study;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CommentService commentService;

    static Post post;
    @BeforeAll
    static void init(){
        post = Post.builder()
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
                MockMvcRequestBuilders.post("/api/v1/post/{post_id}/comment", post.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // 403 에러 방지 - Security 제외
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.parentId").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("댓글 작성할 때, 댓글 내용은 1 글자 이상이어야 합니다.")
    @WithMockUser // 401 에러 방지
    void createCommentWithEmptyBody() throws Exception {
        //given
        CommentCreateRequestDto request = CommentCreateRequestDto.builder()
                .body(" ")
                .build();
        BDDMockito.given(commentService.create("user",9999L,request))
                        .willThrow(new DtoNotValidException());

        //when //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/post/{post_id}/comment", post.getId())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()) // 403 에러 방지 - Security 제외
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("INVALID_DTO_PARAMETER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("입력한 정보를 다시 확인해주세요."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorList[0]").value("must not be blank"));
    }
    @GetMapping("/post/{post_id}/comments")
    public ResponseEntity<CommentResponseDto> getComments(@PathVariable("post_id") Long postId){
        CommentResponseDto comments = commentService.getListById(postId);

        return ResponseEntity.ok().body(comments);
    }
    @Test
    @DisplayName("댓글 목록 조회하기")
    @WithMockUser
    void getComments() throws Exception{
        //given
        CommentDetailDto dto1 = createCommentDetailDto("댓글1", 2000L, null, null);
        CommentDetailDto dto2 = createCommentDetailDto("댓글2", 2001L, null, null);
        CommentResponseDto response = CommentResponseDto.builder()
                .comments(List.of(dto1, dto2))
                .build();
        BDDMockito.given(commentService.getListById(post.getId()))
                .willReturn(response);
        //when //then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/post/{post_id}/comments",post.getId())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].id").value(2000L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].body").value("댓글1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[1].id").value(2001L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments[1].body").value("댓글2"));
    }
    private static CommentDetailDto createCommentDetailDto(String body, Long id, Long parentId, List<CommentDetailDto> children) {
        return CommentDetailDto.builder()
                .body(body)
                .id(id)
                .parentId(parentId)
                .children(children)
                .build();
    }
    @Test
    @DisplayName("댓글 수정하기")
    @WithMockUser
    void updateComment() throws Exception{
        //given
        Comment comment = Comment.builder()
                .post(post)
                .body("댓글")
                .id(2000L)
                .build();
        CommentDetailDto request = CommentDetailDto.builder()
                        .body("댓글 수정")
                        .build();

        //when //then
        mockMvc.perform(MockMvcRequestBuilders
                                .patch("/api/v1/comment/{comment_id}",comment.getId())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()) // 403 에러 방지 - Security 제외|-
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("success"));

        BDDMockito.verify(commentService, Mockito.times(1)).modifyById("user", 2000L, request);
    }

    @Test
    @DisplayName("댓글 수정할 때, 수정 내용은 1글자 이상이어야 합니다.")
    @WithMockUser
    void updateCommentWithEmptyBody() throws Exception{
        //given
        Comment comment = Comment.builder()
                .post(post)
                .body("댓글")
                .id(2000L)
                .build();
        CommentDetailDto request = CommentDetailDto.builder()
                .body(" ")
                .build();

         Mockito.doThrow(new DtoNotValidException())
                         .when(commentService)
                                 .modifyById("user",2000L,request);
        //when //then
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/comment/{comment_id}",comment.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // 403 에러 방지 - Security 제외|-
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("INVALID_DTO_PARAMETER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("입력한 정보를 다시 확인해주세요."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorList[0]").value("must not be blank"));

    }

    @Test
    @DisplayName("댓글 삭제하기")
    @WithMockUser
    void deleteComment() throws Exception{
        //given
        Comment comment = Comment.builder()
                .post(post)
                .body("댓글")
                .id(2000L)
                .build();
        //when //then
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/comment/{comment_id}",comment.getId())
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("success"));

        Mockito.verify(commentService, Mockito.times(1)).deleteById("user", comment.getId());
    }
}