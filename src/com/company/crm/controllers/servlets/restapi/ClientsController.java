package com.company.crm.controllers.servlets.restapi;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.crm.controllers.database.ClientsHibernateQueryManager;
import com.company.crm.controllers.database.ClientsOrmQueryManager;
import com.company.crm.dto.ClientsDtoConverter;
import com.company.crm.dto.DtoJsonConvertor;
import com.company.crm.exceptions.ClientNotFoundException;
import com.company.crm.exceptions.CrmException;
import com.company.crm.exceptions.ImageAlreadyExistsException;
import com.company.crm.exceptions.InvalidFormDataException;
import com.company.crm.exceptions.PropertyValidationException;
import com.company.crm.model.Client;
import com.company.crm.utils.ClientValidator;
import com.company.crm.utils.FormUploadUtils;
import com.company.crm.utils.RestApiUtils;
import com.company.crm.utils.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet("/restapi/clients")
public class ClientsController extends HttpServlet {
    private DtoJsonConvertor dtoJsonConvertor;

    private ClientsOrmQueryManager persistenceManager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            dtoJsonConvertor = ClientsDtoConverter.getInstance();
            persistenceManager = ClientsHibernateQueryManager.getInstance();
        } catch (Exception e) {
            throw new UnavailableException("Initialization of one or more services failed.");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int responseStatusCode = 0;
        String responseStatusMessage = null;
        
        int id = getParameterId(request);
        boolean allClientsRequest = (id == -1);
        
        boolean GET_DELETED_RECORDS = false;
        
        JsonArray clientsArray = null;
        JsonObject clientData = null;

        try {
            if (allClientsRequest) {
                clientsArray = dtoJsonConvertor.getAllAsJson(GET_DELETED_RECORDS);
            } else {
                clientData = dtoJsonConvertor.getByIdAsJson(id, GET_DELETED_RECORDS);
            }
            
            responseStatusCode = HttpServletResponse.SC_OK;
            responseStatusMessage = "Client data successfully delivered.";
        } catch (PropertyValidationException e) {
            responseStatusMessage = "Please provide correct request.";
        } catch (ClientNotFoundException e) {
            responseStatusMessage = "The client is not found.";
        } catch (SQLException e) {
            responseStatusMessage = "Database problem occured.";
        }

        if (responseStatusCode == 0) {
            responseStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }

        JsonObject jsonResponse = null;

        if (clientsArray != null) {
            jsonResponse = RestApiUtils.buildJsonResponse(responseStatusCode, responseStatusMessage, clientsArray);
        } else if (clientData != null) {
            jsonResponse = RestApiUtils.buildJsonResponse(responseStatusCode, responseStatusMessage, clientData);
        } else {
            jsonResponse = RestApiUtils.buildJsonResponse(responseStatusCode, responseStatusMessage);
        }

        RestApiUtils.sendJsonResponse(request, response, jsonResponse.toString());
    }

    /**
     * This method handles ajax requests for adding a new client.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isUpdateRequest = false;
        addEditClient(request, response, isUpdateRequest);
    }

    /**
     * This method handles ajax requests for deleting a client specified by id.
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int responseStatusCode = 0;
        String responseStatusMessage = null;

        try {
            int id = getParameterId(request);
            persistenceManager.deleteClient(id);

            responseStatusCode = HttpServletResponse.SC_OK;
            responseStatusMessage = "The client is successfully deleted.";
        } catch (Exception e) {
            // TODO: ...
            e.printStackTrace();
            responseStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseStatusMessage = "Something went wrong. Try again later.";
        }

        JsonObject jsonResponse = RestApiUtils.buildJsonResponse(responseStatusCode, responseStatusMessage);
        RestApiUtils.sendJsonResponse(request, response, jsonResponse.toString());
    }

    /**
     * This method handles ajax requests for modifying a client specified by id.
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isUpdateRequest = true;
        this.addEditClient(request, response, isUpdateRequest);
    }

    /**
     * This method provides the common logic for fetching the id parameter from the sent request.
     */
    private int getParameterId(HttpServletRequest request) {
        int id = -1;

        String idAsString = request.getParameter("id");
        if (!StringUtils.isNullOrEmpty(idAsString)) {
            id = Integer.parseInt(idAsString.trim());
        }
        
        return id;
    }

    /**
     * This method provides the common logic for both doPost and doPut.
     */
    private void addEditClient(HttpServletRequest request, HttpServletResponse response, boolean isUpdateRequest)
            throws IOException, ServletException {
        String statusMessage = null;
        int statusCode = 0;

        try {
            Client client = null;

            /**
             * If an update request, checks if the provided id is correct.
             * If an add new client request, checks if the provided name is unique.
             */
            if (isUpdateRequest) {
                int id = -1;

                String idString = FormUploadUtils.getTextFieldValue(request, "id");

                if (!StringUtils.isNullOrEmpty(idString)) {
                    try {
                        id = Integer.parseInt(idString.trim());
                    } catch (NumberFormatException e) {
                        throw new InvalidFormDataException("Client id is missing or has incorrect value.");
                    }
                }

                client = persistenceManager.getClientById(id);
            } else {
                String name = FormUploadUtils.getTextFieldValue(request, "name");
                boolean isNameUnique = ClientValidator.checkIfNameIsUnique(name, persistenceManager);

                if (!isNameUnique) {
                    throw new InvalidFormDataException("Client name is a duplicate of the name of an existing record.");
                }

                client = new Client();
            }

            client = FormUploadUtils.populateClientWithFormData(client, request);

            boolean isOperationSuccessful = (isUpdateRequest)
                    ? persistenceManager.updateClient(client)
                    : persistenceManager.addNewClient(client);

            if (isOperationSuccessful) {
                statusMessage = (isUpdateRequest)
                        ? "The client was modified successfully."
                        : "New client added successfully.";
                statusCode = HttpServletResponse.SC_OK;
            } else {
                throw new CrmException("Something went wrong. The record cannot be saved.");
            }
        } catch (InvalidFormDataException | ImageAlreadyExistsException e) {
            e.printStackTrace();
            statusMessage = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            statusMessage = "Something went wrong. The record cannot be saved.";
        }

        if (statusCode == 0) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }

        JsonObject jsonResponse = RestApiUtils.buildJsonResponse(statusCode, statusMessage);
        RestApiUtils.sendJsonResponse(request, response, jsonResponse.toString());
    }
    
    public void setDtoJsonConvertor(DtoJsonConvertor dtoJsonConvertor) {
        this.dtoJsonConvertor = dtoJsonConvertor;
    }
}