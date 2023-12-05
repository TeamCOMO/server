package project.como.domain.interest.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestCreateResponseDto {
    private Long interestId;
    
    @Builder
    public InterestCreateResponseDto(Long id){
        this.interestId = id;
    }
    
    public static InterestCreateResponseDto of(Long id){
        return InterestCreateResponseDto.builder()
                .id(id)
                .build();
    }
}
