package com.chengshiun.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserLoginFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(UserLoginFilter.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();

        if (url.equals("/userLogin")) {
            //取得 user 身份
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userAgent = request.getHeader("User-Agent");

            // 使用 SimpleDateFormat 来指定时间格式
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            String now = dateFormat.format(new Date());

            log.info("會員: {} 於 {}, 嘗試從 {} 進行登入", authentication.getName(), now, userAgent);
        }
        //把參數在往下傳給下一個 Filter
        filterChain.doFilter(request, response);
    }
}
