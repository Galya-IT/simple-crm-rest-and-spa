package com.company.crm.controllers.filters;

import static com.company.crm.controllers.database.PersistenceManager.logError;
import static com.company.crm.controllers.database.PersistenceManager.logInfo;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.company.crm.controllers.database.PersistenceManager;

@WebFilter(filterName = "PersistenceSessionFilter", urlPatterns = { "/*" })
public class PersistenceSessionFilter implements Filter {
    
    @Override
    public void init(FilterConfig cfg) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        
        try {
            chain.doFilter(request, response);
        } catch (Throwable throwable) {
            String throwableStackTraceString = throwable.getStackTrace().toString();
            logError(throwableStackTraceString);
            throw new ServletException("Error ", throwable);
        } finally {
            persistenceManager.closeSession();
        }
    	return;
    }

    @Override
    public void destroy() {
        PersistenceManager.getInstance().destroy();
        logInfo("Persistence manager destroyed.");
    }
}
