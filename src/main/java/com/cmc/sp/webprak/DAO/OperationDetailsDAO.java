package com.cmc.sp.webprak.DAO;

import com.cmc.sp.webprak.classes.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OperationDetailsDAO extends InheritedDAO<OperationDetails, Long>{
    public OperationDetailsDAO() {
        super(OperationDetails.class);
    }

    public List<OperationDetails> getOperationsDetailsByOperation(Operations operation) {
        try (Session session = sessionFactory.openSession()) {
            Query<OperationDetails> query = session.createQuery("FROM OperationDetails WHERE operation = :gotOperation", OperationDetails.class)
                    .setParameter("gotOperation", operation);
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
    public List<OperationDetails> getOperationsDetailsByProduct(Products product) {
        try (Session session = sessionFactory.openSession()) {
            Query<OperationDetails> query = session.createQuery("FROM OperationDetails WHERE product = :gotProduct", OperationDetails.class)
                    .setParameter("gotProduct", product);
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
}
