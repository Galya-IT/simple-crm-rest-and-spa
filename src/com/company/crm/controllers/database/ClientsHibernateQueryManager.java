package com.company.crm.controllers.database;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.company.crm.model.Client;

public class ClientsHibernateQueryManager implements ClientsOrmQueryManager {
    private static ClientsHibernateQueryManager instance;

    private ClientsHibernateQueryManager() {
        super();
    }

    public static ClientsHibernateQueryManager getInstance() {
        if (ClientsHibernateQueryManager.instance == null) {
            ClientsHibernateQueryManager.instance = new ClientsHibernateQueryManager();
        }
        return instance;
    }

    @Override
    public Client getClientById(int id) {
        Session session = PersistenceManager.getInstance().getSession();
        
        Client client = (Client) session.get(Client.class, id);
        return client;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Client> getAllClients() {
        Session session = PersistenceManager.getInstance().getSession();

        Query querySelectAllClients = session.createQuery("from Client");
        List<Client> clients = querySelectAllClients.list();

        return clients;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Client> getClienstWithName(String name) {
        Session session = PersistenceManager.getInstance().getSession();
        
        Query querySelectAllClients = session.createQuery("from Client where name = :name");
        querySelectAllClients.setParameter("name", name);
        
        List<Client> clientsWithSameName = querySelectAllClients.list();
        return clientsWithSameName;
    }

    @Override
    public boolean deleteClient(int id) {
        boolean isDeletedSuccessful = true;

        Client client = getClientById(id);
        client.setIsDeleted(true);

        TransactionOperation updateOperation = new UpdateOperation<Client>(client);
        
        try {
            PersistenceManager.getInstance().runInTransaction(updateOperation);
        } catch (Throwable e) {
            isDeletedSuccessful = false;
        }

        return isDeletedSuccessful;
    }

    @Override
    public boolean addNewClient(Client client) {
        boolean isNewClientAddedSuccessfully = true;

        TransactionOperation addNewOperation = new AddNewOperation<Client>(client);
        
        try {
            PersistenceManager.getInstance().runInTransaction(addNewOperation);
        } catch (Throwable e) {
            isNewClientAddedSuccessfully = false;
        }

        return isNewClientAddedSuccessfully;
    }

    @Override
    public boolean updateClient(Client updatedClient) {
        boolean isClientUpdatedSuccessfully = true;

        TransactionOperation updateOperation = new UpdateOperation<Client>(updatedClient);
        
        try {
            PersistenceManager.getInstance().runInTransaction(updateOperation);
        } catch (Throwable e) {
            isClientUpdatedSuccessfully = false;
        }

        return isClientUpdatedSuccessfully;
    }
}
