package project.como.domain.interest.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class InterestResponseDto {
    List<InterestCreateRequestDto> interests;
}
