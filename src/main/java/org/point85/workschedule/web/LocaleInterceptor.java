package org.point85.workschedule.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.point85.workschedule.WorkSchedule;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LocaleInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// Check explicit ?lang= query parameter first (overrides header)
		String langParam = request.getParameter("lang");
		if (langParam != null && !langParam.isEmpty()) {
			WorkSchedule.setLocale(Locale.forLanguageTag(langParam));
			return true;
		}

		// Fall back to Accept-Language header
		String acceptLang = request.getHeader("Accept-Language");
		if (acceptLang != null && !acceptLang.isEmpty()) {
			Locale locale = Locale.forLanguageTag(acceptLang.split(",")[0].trim());
			WorkSchedule.setLocale(locale);
		}

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		WorkSchedule.clearLocale();
	}
}
