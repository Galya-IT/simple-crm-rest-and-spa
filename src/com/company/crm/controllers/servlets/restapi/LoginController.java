package com.company.crm.controllers.servlets.restapi;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet("/restapi/login")
public class LoginController extends HttpServlet {
	private static final String USER = "admin";
    private static final String PASSWORD = "qwerty";
    private static final String ADMIN_PAGE = "/CRM/#!/";
    private static final String ERROR_LOGIN_PAGE = "/CRM/#!/login/msg/credentials";

    /**
     * Returns if user has a valid http session.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String[]> parameterMap = request.getParameterMap();

        if (parameterMap.containsKey("checkSession")) {
            HttpSession session = request.getSession(false);
        	boolean isValidSession = (session != null) && (session.getAttribute("username") != null);
        	
        	if (isValidSession){
    	    	response.setStatus(HttpServletResponse.SC_OK);
    	    } else {
            	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    	    }
        }
    }

    /**
     * Checks username and password from form sent and creates a new session if correct.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userFromInput = request.getParameter("user").trim();
        String passFromInput = request.getParameter("pass").trim();

        // TODO: implement database table with users and password and mysql request checking if input values are valid
        
        if (userFromInput.equals(USER) && passFromInput.equals(PASSWORD)) {
            response.setContentType("text/html");
            
            HttpSession session = request.getSession();
            session.setAttribute("username", userFromInput);
            
            response.sendRedirect(ADMIN_PAGE);
        } else {
            response.sendRedirect(ERROR_LOGIN_PAGE);
        }
    }
}
