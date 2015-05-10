package com.company.crm.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RestApiUtils {

    public static JsonObject buildJsonResponse(int statusCode, String message) {
        JsonObject jsonResponse = new JsonObject();
        
        jsonResponse.addProperty("status", statusCode);
        jsonResponse.addProperty("statusText", message);
        
        return jsonResponse;
    }
    
    /**
     * Both overridden methods accepting 3 parameters are absolutely identical logically
     * but accept different types of data, on which depends the execution of the JsonObject method add.
     */
    public static JsonObject buildJsonResponse(int statusCode, String message, JsonObject data) {
        JsonObject jsonResponse = buildJsonResponse(statusCode, message);
        jsonResponse.add("data", data);
        return jsonResponse;
    }
    
    public static JsonObject buildJsonResponse(int statusCode, String message, JsonArray data) {
        JsonObject jsonResponse = buildJsonResponse(statusCode, message);
        jsonResponse.add("data", data);
        return jsonResponse;
    }

    public static void sendJsonResponse(HttpServletRequest request, HttpServletResponse response, String jsonData)
            throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonData);
    }
}
