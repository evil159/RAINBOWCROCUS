/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

/**
 *
 * @author rol
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        if (!UserSession.isValid(httpRequest.getHeader("sessionKey"))) {
            
            Cookie[] cookies = httpRequest.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("sessionKey")) {
                        if (UserSession.isValid(cookie.getValue())) {
                            chain.doFilter(request, response);
                            return;
                        }
                    }
                }
            }
            
//            httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Test");
//            return;
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        
    }
    
}
