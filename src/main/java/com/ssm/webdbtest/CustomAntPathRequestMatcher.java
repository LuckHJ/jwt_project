package com.ssm.webdbtest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;
public class CustomAntPathRequestMatcher implements RequestMatcher {
    private final String pattern;

    public CustomAntPathRequestMatcher(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        /*// 实现你的匹配逻辑
        String path = request.getServletPath();
        return path.matches(pattern);*/
        // 这里可以使用任何你喜欢的方式来实现路径匹配逻辑，
        // 比如使用正则表达式或其他方式。
        return request.getServletPath().matches(".*" + pattern + ".*");
    }

    @Override
    public MatchResult matcher(HttpServletRequest request) {
        return RequestMatcher.super.matcher(request);
    }
}
