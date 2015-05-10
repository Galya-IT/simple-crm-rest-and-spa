package com.company.crm.dto;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.UnavailableException;

import com.company.crm.controllers.database.ClientsHibernateQueryManager;
import com.company.crm.controllers.database.ClientsOrmQueryManager;
import com.company.crm.exceptions.ClientNotFoundException;
import com.company.crm.exceptions.PropertyNotSetException;
import com.company.crm.exceptions.PropertyValidationException;
import com.company.crm.exceptions.RecordNotFoundException;
import com.company.crm.model.Client;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ClientsDtoConverter extends DtoJsonConvertor {
    private ClientsOrmQueryManager persistenceManager;
    
    private static ClientsDtoConverter instance;
    
    private ClientsDtoConverter() throws UnavailableException {
        super();
        
        try {
            persistenceManager = ClientsHibernateQueryManager.getInstance();
        } catch (Exception e) {
            throw new UnavailableException("Initialization of one or more services failed.");
        }
    }

    public static ClientsDtoConverter getInstance() throws UnavailableException {
        if (instance == null) {
            instance = new ClientsDtoConverter();
        }
        return instance;
    }

    @Override
    public JsonObject getByIdAsJson(int id, boolean getRecordMarkedAsDeleted) throws SQLException, PropertyValidationException, ClientNotFoundException {
        JsonObject clientJson = null;

        Client client = persistenceManager.getClientById(id);

        boolean isClientDeleted = client.getIsDeleted();

        if (getRecordMarkedAsDeleted || !isClientDeleted) {
            clientJson = serialize(client);
        }
        
        return clientJson;
    }

    @Override
    public JsonArray getAllAsJson(boolean getRecordsMarkedAsDeleted) throws SQLException {
        JsonArray clientsArray = new JsonArray();

        List<Client> clients = persistenceManager.getAllClients();

        for (Client client : clients) {
            boolean isClientDeleted = client.getIsDeleted();
            
            if (getRecordsMarkedAsDeleted || !isClientDeleted) {
                JsonObject currentClientDto = serialize(client);
                clientsArray.add(currentClientDto);
            }
        }
        return clientsArray;
    }

    /**
     * Client update is done by receiving an html form data and no deserialization is needed. 
     */
    @Override
    public void updateDto(String jsonDtoString) throws SQLException, RecordNotFoundException, PropertyNotSetException,
            PropertyValidationException, ParseException {
        // do nothing.
    }
}
