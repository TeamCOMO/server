package project.como.domain.interest.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.como.domain.post.dto.PostDetailResponseDto;

import java.util.List;

@Data
@Builder
public class InterestResponseDto {
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private List<InterestDetailResponseDto> interests;
}
