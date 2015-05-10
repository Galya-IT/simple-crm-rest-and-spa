package com.company.crm.controllers.database;

import java.util.List;

import com.company.crm.model.Client;

public interface ClientsOrmQueryManager extends OrmQueryManager {
    Client getClientById(int id);
    
    List<Client> getAllClients();
    
    List<Client> getClienstWithName(String name);
    
    boolean deleteClient(int id);
    
    boolean addNewClient(Client client);
    
    boolean updateClient(Client client); 
}

