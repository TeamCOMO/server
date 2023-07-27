package project.como.domain.interest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.como.domain.interest.dto.InterestCreateRequestDto;
import project.como.domain.interest.dto.InterestResponseDto;
import project.como.domain.interest.service.InterestService;
import project.como.domain.post.service.PostService;
import project.como.global.auth.model.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InterestController {
    private final InterestService interestService;
    //private final PostService postService;

    //관심 등록
    @PostMapping("/post/interest")
    public ResponseEntity<String> registryInterest(@CurrentUser String username,
                                                   @RequestBody InterestCreateRequestDto dto){
        /**
         * 해당 유저가 이미 관심 게시물을 등록했더라면 중복 등록을 불가한 기능 필요
         */
        System.out.println("시작");
        interestService.createInterest(username, dto);
        return ResponseEntity.ok().body("success");
    }

    //단일 관심 조회는 필요 없어 보여서 만들지 않음. 획일화를 위해 IntersetDto를 통해 IntrestResponseDto를 반환
    //관심 조회 : 해당 유저의 관심 게시물을 전부 반환(조건 : 해당 유저에 대한 인증)
    @GetMapping("/interests")
    private ResponseEntity<InterestResponseDto> getInterests(@CurrentUser String username){
        InterestResponseDto interests = interestService.findInterests(username);

        return ResponseEntity.ok().body(interests);

    }

    //관심 삭제
    @DeleteMapping("/interest/{interest_id}")
    public ResponseEntity<String> deleteInterest(@PathVariable(name = "interest_id") Long interestId,
                                                 @CurrentUser String username){
        interestService.deleteInterest(interestId, username);

        return ResponseEntity.ok().body("success");
    }
    /**
     * 1. 관심 등록을 반환하는 부분 문제점(페이징 쿼리)
     * InterestResponseDto(필드로 Long postId를 가짐)룰 반환한 후, PostController에서 파라미터로 받은 postId를 통해 호출할 지와
     * IntersetController에서 Postservice를 통해 바로 관심 등록된 게시물들을 호출할 지 중 정해야 함. 어느 부분에 책임을 양도할 지 고민.
     * -> 해결 : PostController를 통해 호출
     * 2. postman으로 정상 작동 test
     * 3. 예외 처리 통일하기(Post, User, Comment 등 ComoExeception을 통해 처리)
     * 4. 마지막으로 점검 후, push
     */
    /**
     * 1번은 해결
     *
     */
}
