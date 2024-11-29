/*
package com.example.menu_electronics.configuration;


@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {
	@Override
	public void apply(RequestTemplate template) {
		ServletRequestAttributes servletRequestAttributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		var authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");

		log.info("Header: {}", authHeader);
		if (StringUtils.hasText(authHeader)) template.header("Authorization", authHeader);
	}
}
*/
