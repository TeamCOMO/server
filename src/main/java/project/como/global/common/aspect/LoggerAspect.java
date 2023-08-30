package project.como.global.common.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import project.como.global.common.model.CustomLog;
import project.como.global.common.model.Logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

	private static final String format = "yyyy-MM-dd HH:mm:ss.SSS";

	@Around("@annotation(project.como.global.common.model.Logging) && @annotation(logging)")
	public Object aroundLogger(ProceedingJoinPoint joinPoint, Logging logging) throws Exception {
		CustomLog customLog = new CustomLog();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		customLog.setCreatedAt(LocalDateTime.now().format(formatter));

		HttpServletRequest request =
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		customLog.setUri(request.getRequestURI());

		Object result = null;
		try {
			result = joinPoint.proceed();
		} catch (Throwable t) {
			t.printStackTrace();
			customLog.setResult("fail-" + t.getMessage());
		}

		if (result instanceof ResponseEntity) {
			ResponseEntity responseEntity = (ResponseEntity) result;

			if (responseEntity.getStatusCode() == HttpStatus.OK)
				customLog.setResult("success");
			else {
				HttpStatus status = HttpStatus.valueOf(responseEntity.getStatusCode().value());
				customLog.setResult("fail-" + status.getReasonPhrase());
			}
		}

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		customLog.setMethod(methodSignature.getName());
		customLog.setItem(logging.item());
		customLog.setAction(logging.action());

		log.info(getMessage(customLog));

		return result;
	}

	private String getMessage(CustomLog customLog) throws JsonProcessingException {
		Map<String, String> map = new LinkedHashMap<>();

		map.put("createdAt", customLog.getCreatedAt());
		map.put("item", customLog.getItem());
		map.put("action", customLog.getAction());
		map.put("result", customLog.getResult());
		map.put("uri", customLog.getUri());
		map.put("method", customLog.getMethod());

		return new ObjectMapper().writeValueAsString(map);
	}
}
