package com.company.crm.controllers.servlets.restapi;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.Test;

import com.company.crm.CrmBaseTestClass;
import com.company.crm.dto.DtoJsonConvertor;
import com.company.crm.exceptions.ClientNotFoundException;
import com.company.crm.exceptions.PropertyValidationException;
import com.google.gson.JsonObject;

public class ClientsControllerTest extends CrmBaseTestClass {
    private static final String TESTS_RESOURCES_FOLDER = "JavaTests" + File.separator + "resources";
    
    @Test
    public void getClientWithDoGetCorrectRequest() throws ServletException, IOException, PropertyValidationException,
            ClientNotFoundException, SQLException {
        /**
         * Mocks method getByIdAsJson from ClientsDtoConverter to return fake result.
         */
        int CLIENT_ID = 1;
        boolean GET_DELETED_RECORDS = false;

        JsonObject CLIENT_AS_JSON = new JsonObject();
        CLIENT_AS_JSON.addProperty("id", CLIENT_ID);
        CLIENT_AS_JSON.addProperty("name", "Client 1");
        CLIENT_AS_JSON.addProperty("location", "Location 1");
        CLIENT_AS_JSON.addProperty("notes", "");
        CLIENT_AS_JSON.addProperty("logoFileName", "");
        CLIENT_AS_JSON.addProperty("isDeleted", false);
        
        DtoJsonConvertor fakeDtoConverter = EasyMock.createMock(DtoJsonConvertor.class);
        EasyMock.expect(fakeDtoConverter.getByIdAsJson(CLIENT_ID, GET_DELETED_RECORDS)).andReturn(CLIENT_AS_JSON);
        EasyMock.replay(fakeDtoConverter);

        /**
         * Fakes id parameter in the request stub
         */
        HttpServletRequest fakeRequest = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse fakeResponse = EasyMock.createMock(HttpServletResponse.class);

        String CLIENT_ID_STRING_IN_REQUEST = Integer.toString(CLIENT_ID);

        EasyMock.expect(fakeRequest.getParameter("id")).andReturn(CLIENT_ID_STRING_IN_REQUEST);
        EasyMock.replay(fakeRequest);

        /**
         * Prepares result to be written on file.
         */
        String RESULT_OUTPUT_FILE_PATH = TESTS_RESOURCES_FOLDER + File.separator + "ClientsControllerDoGetJsonResponse.txt";
        
        PrintWriter writer = new PrintWriter(RESULT_OUTPUT_FILE_PATH);

        fakeResponse.setContentType("text/plain");
        fakeResponse.setCharacterEncoding("UTF-8");
        EasyMock.expect(fakeResponse.getWriter()).andReturn(writer);
        
        EasyMock.replay(fakeResponse);
        
        /**
         * Executes method and compares result.
         */
        ClientsController clientsController = new ClientsController();
        clientsController.setDtoJsonConvertor(fakeDtoConverter);
        clientsController.doGet(fakeRequest, fakeResponse);

        EasyMock.verify(fakeDtoConverter);
        EasyMock.verify(fakeRequest);

        writer.flush(); // it may not have been flushed yet...

        JsonObject EXPECTED_RESULT_JSON = new JsonObject();
        EXPECTED_RESULT_JSON.addProperty("status", 200);
        EXPECTED_RESULT_JSON.addProperty("statusText", "Client data successfully delivered.");
        EXPECTED_RESULT_JSON.add("data", CLIENT_AS_JSON);
        
        String EXPECTED_RESULT_STRING = EXPECTED_RESULT_JSON.toString();

        File resultOutputFile = new File(RESULT_OUTPUT_FILE_PATH);
        String actualResultJsonString = FileUtils.readFileToString(resultOutputFile);

        boolean actualTestResult = actualResultJsonString.contains(EXPECTED_RESULT_STRING);
        assertTrue(actualTestResult);
    }
}
