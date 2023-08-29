package project.como.global.common.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionResponse {
	private String title;
	private Integer status;
	private LocalDateTime timestamp;
	private String message;
}
