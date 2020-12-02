package com.meteoweb.meteoanalyzer.mysqlloaderimpl;

import com.meteoweb.domain.StationInformation;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *  JPA CRUD Operations
 * @author Ramesh Fadatare
 *
 */
public class CRUDOperations {

    public void insertEntity(StationInformation stationInformation) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(stationInformation);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}