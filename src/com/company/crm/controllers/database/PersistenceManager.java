package com.company.crm.controllers.database;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Singleton class PersistenceManager manages multithread operations with
 * Hibernate ORM sessions.
 */
public final class PersistenceManager {
    protected final static ThreadLocal<Boolean> openRequested = new ThreadLocal<Boolean>();
    protected final static ThreadLocal<Session> localSession = new ThreadLocal<Session>();

    private static final Logger logger = Logger.getLogger(PersistenceManager.class.getName());
    private static final SessionFactory factory;
    private static final PersistenceManager instance;

    private PersistenceManager() {
    }

    /**
     * Initializes Hibernate applying configurations.
     */
    static {
        Configuration configuration = new Configuration().configure();
        Properties properties = configuration.getProperties();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(properties);
        factory = configuration.buildSessionFactory(builder.build());
        instance = new PersistenceManager();
    }

    public static PersistenceManager getInstance() {
        return instance;
    }

    public static void logWarning(String msg) {
        logger.log(Level.WARNING, msg);
    }

    public static void logError(String msg) {
        logger.log(Level.SEVERE, msg);
    }

    public static void logInfo(String msg) {
        logger.log(Level.INFO, msg);
    }

    /*------------------------------------------------------------*/

    public synchronized Session getSession() {
        Session session = localSession.get();

        if (session == null) {
            logInfo("Opening session...");
            session = factory.openSession();

            logInfo("Session opened.");
            localSession.set(session);
        }

        return session;
    }

    public synchronized void closeSession() {
        Session session = localSession.get();

        if (session == null) {
            logInfo("No session found in thread.");
            return;
        }
        
        if (session.getTransaction().isActive()) {
            logError("Transaction is still active and not closed. Will rollback.");
            session.getTransaction().rollback();
            logInfo("Session rolled back.");
        }

        logInfo("Closing session.");
        session.close();

        logInfo("Session closed.");
        localSession.set(null);
    }

    public void destroy() {
        logInfo("Closing session factory...");
        factory.close();
        logInfo("Session factory closed.");
    }

    /*------------------------------------------------------------*/

    public void runInTransaction(TransactionOperation operation)
            throws Throwable {
        Session session = getSession();
        
        logInfo("Transaction starts.");
        Transaction transaction = session.beginTransaction();
        
        try {
            operation.execute(session);
            
            logInfo("Transaction commit...");
            transaction.commit();
        } catch (Throwable throwable) {
            logInfo("Rolling back transaction...");
            transaction.rollback();
            
            throw throwable;
        }
    }
}
