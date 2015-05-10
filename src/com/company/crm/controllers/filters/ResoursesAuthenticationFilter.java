package com.company.crm.controllers.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "ResoursesAuthenticationFilter",
	urlPatterns = { "/restapi/*" },
	initParams = {@WebInitParam(name = "excludeLogin", value = "/restapi/login") })
public class ResoursesAuthenticationFilter implements Filter {
	private String excludePatternLogin;

    @Override
    public void init(FilterConfig cfg) throws ServletException {
    	this.excludePatternLogin = cfg.getInitParameter("excludeLogin");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String address = httpRequest.getRequestURL().toString();
        boolean isLoginService = address.indexOf(excludePatternLogin) > -1;
        
        if (isLoginService) {
        	chain.doFilter(request, response);
        	return;
        }
        
    	HttpSession session = httpRequest.getSession(false);

        boolean isValidSession = (session != null) && (session.getAttribute("username") != null);
    	
    	if (isValidSession){
        	chain.doFilter(request, response);
        } else {
        	httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    	return;
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}
