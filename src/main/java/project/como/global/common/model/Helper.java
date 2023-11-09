package project.como.global.common.model;

import jakarta.servlet.http.HttpServletRequest;

public class Helper {

	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");

		if (ip == null)
			ip = request.getRemoteAddr();

		return ip;
	}
}
