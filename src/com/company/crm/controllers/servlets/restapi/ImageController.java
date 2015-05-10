package com.company.crm.controllers.servlets.restapi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet("/restapi/image")
public class ImageController extends HttpServlet {
    private static final String LOGOS_FOLDER = "WEB-INF" + File.separator + "logos";
    private static final String LOGOS_RESOURCES_FOLDER = "/WEB-INF/logos/";

    @Override
    public void init() throws ServletException {
        // do nothing.
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getImage(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getImage(request, response);
    }

    @Override
    public void destroy() {
        // do nothing.
    }

    private void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalArgumentException {
        String imageFileName = request.getParameter("name");

        if (imageFileName == null) {
            throw new IllegalArgumentException("The name of the requested image file is not provided!");
        }

        HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();
        String realContextPath = context.getRealPath(request.getContextPath());
        
        // Fixes the wrong file path
        String parentDirectory = new File(realContextPath).getParent();
        
        String absoluteDiskPath = parentDirectory + File.separator + LOGOS_FOLDER + File.separator + imageFileName;
        
        /**
         * Checks if file actually exists
         */
        File imageFile = new File(absoluteDiskPath);
        
        if (imageFile.exists() && !imageFile.isDirectory()) {
            byte[] buffer = new byte[1024];

            try (InputStream inputStream = context.getResourceAsStream(LOGOS_RESOURCES_FOLDER.concat(imageFileName));
                    
                    OutputStream output = response.getOutputStream();) {
                int length = 0;
                while ((length = inputStream.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }
        }
    }
}
