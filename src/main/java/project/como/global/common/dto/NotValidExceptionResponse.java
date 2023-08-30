package project.como.global.common.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class NotValidExceptionResponse extends ExceptionResponse {
	private final List<String> err;
}
