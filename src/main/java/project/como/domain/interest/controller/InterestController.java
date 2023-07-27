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

    //관심 등록
    @PostMapping("/post/interest")
    public ResponseEntity<String> registryInterest(@CurrentUser String username,
                                                   @RequestBody InterestCreateRequestDto dto){
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

}
