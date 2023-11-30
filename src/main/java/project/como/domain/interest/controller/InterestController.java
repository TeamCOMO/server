package project.como.domain.interest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.como.domain.interest.dto.InterestCreateRequestDto;
import project.como.domain.interest.dto.InterestResponseDto;
import project.como.domain.interest.service.InterestService;
import project.como.global.auth.model.CurrentUser;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InterestController {
    private final InterestService interestService;
    private final String INTEREST_API_ENDPOINT =   "/api/v1/post/interest";

    //관심 등록
    @PostMapping("/interest")
    public ResponseEntity<String> registryInterest(@CurrentUser String username,
                                                   @RequestBody @Valid InterestCreateRequestDto dto){
        interestService.createInterest(username, dto);
        URI location = URI.create(INTEREST_API_ENDPOINT);
        return ResponseEntity.created(location).build();
    }

    //단일 관심 조회는 필요 없어 보여서 만들지 않음.
    //관심 조회 : 해당 유저의 관심 게시물을 전부 반환(조건 : 해당 유저에 대한 인증)
    @GetMapping("/interest")
    private ResponseEntity<InterestResponseDto> getInterestPosts(@CurrentUser String username,
                                                                 @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
                                                                 ){
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1); //실제로 처리될 때는 시작 번호가 0이므로 클라이언트에서 받은 pageNo에서 1을 빼줌
        InterestResponseDto interests = interestService.findInterests(username, pageNo);
        return ResponseEntity.ok().body(interests);
    }

    //관심 삭제
    @DeleteMapping("/post/interest/{interest_id}")
    public ResponseEntity<String> deleteInterest(@PathVariable(name = "interest_id") Long interestId,
                                                 @CurrentUser String username){
        interestService.deleteInterest(interestId, username);

        return ResponseEntity.ok().body("success");
    }

}
