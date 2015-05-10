package com.company.crm.dto;

import java.sql.SQLException;
import java.text.ParseException;

import com.company.crm.exceptions.ClientNotFoundException;
import com.company.crm.exceptions.PropertyNotSetException;
import com.company.crm.exceptions.PropertyValidationException;
import com.company.crm.exceptions.RecordNotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class DtoJsonConvertor {
    
    public abstract JsonObject getByIdAsJson(int id, boolean getRecordMarkedAsDeleted) throws SQLException, PropertyValidationException, ClientNotFoundException;

    public abstract JsonArray getAllAsJson(boolean getRecordsMarkedAsDeleted) throws SQLException;
    
    public abstract void updateDto(String jsonDtoString) throws SQLException, RecordNotFoundException, PropertyNotSetException, PropertyValidationException, ParseException;
    
    protected <T> T deserialize(String jsonString, Class<T> clazz) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, clazz);
    }

    protected <T> JsonObject serialize(T dto) {
        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();
        
        String dtoJsonString = gson.toJson(dto);
        
        JsonParser parser = new JsonParser();
        JsonObject dtoJson = parser.parse(dtoJsonString).getAsJsonObject();
        
        return dtoJson;
    }
}
