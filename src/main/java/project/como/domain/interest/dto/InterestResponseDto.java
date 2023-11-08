package project.como.domain.interest.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterestResponseDto {
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private List<InterestDetailResponseDto> interests;
}
