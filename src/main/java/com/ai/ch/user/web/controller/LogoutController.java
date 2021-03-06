package com.ai.ch.user.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登出controller
 * @author Zh
 *
 */
@Controller
public class LogoutController {
	private static final Logger LOG = LoggerFactory.getLogger(LogoutController.class);

	/**
	 * 单点登出
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ssologout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		try {
			session.invalidate();
			session = null;
			LOG.error("+++++++++++++++++logout is success");
		} catch (Exception e) {
			LOG.error("用户登出失败", e); 
		}

	}

}
