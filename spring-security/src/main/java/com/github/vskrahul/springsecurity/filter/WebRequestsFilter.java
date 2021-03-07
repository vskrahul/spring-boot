package com.github.vskrahul.springsecurity.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import com.github.vskrahul.springsecurity.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebRequestsFilter implements Filter {
	private final int sessionExpiredErrorCode = 408; // HttpStatus.REQUEST_TIMEOUT;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		final String requestURI = httpServletRequest.getRequestURI();
		
		MDC.put("sessionKey", httpServletRequest.getSession().getId());
		CsrfToken csrfToken = (CsrfToken)httpServletRequest.getAttribute(CsrfToken.class.getName());
		
		log.info("[requestURI={}] [host={}] [requestHeaders={}] [requestAttributes={}] [X-XSRF-TOKEN={}]"
						,requestURI
						,httpServletRequest.getRemoteHost()
						,headers(httpServletRequest)
						,attributes(httpServletRequest)
						,csrfToken != null ? csrfToken.getToken() : null);
		
		final SecurityContext context = SecurityContextHolder.getContext();
		if (null == context.getAuthentication() || !context.getAuthentication().isAuthenticated()) {
			httpServletResponse.setStatus(sessionExpiredErrorCode);
			httpServletResponse.sendError(sessionExpiredErrorCode, "Session timed out");
		} else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}
	
	private String headers(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		Enumeration<String> headers = request.getHeaderNames();
		while(headers.hasMoreElements()) {
			String headerName = headers.nextElement();
			String headerValue = request.getHeader(headerName);
			map.put(headerName, headerValue);
		}
		return JsonUtil.toJsonString(map);
	}
	
	private String attributes(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		Enumeration<String> attributes = request.getAttributeNames();
		while(attributes.hasMoreElements()) {
			String attributeName = attributes.nextElement();
			String attributeValue = null;
			if(Objects.nonNull(request.getAttribute(attributeName)))
				attributeValue = request.getAttribute(attributeName).toString();
			map.put(attributeName, attributeValue);
		}
		return JsonUtil.toJsonString(map);
	}

	@Override
	public void destroy() {
	}

}
