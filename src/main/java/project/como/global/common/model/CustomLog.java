package project.como.global.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
public class CustomLog {
	private String createdAt;
	private String item;
	private String action;
	private String result;
	private String uri;
	private String method;
}
