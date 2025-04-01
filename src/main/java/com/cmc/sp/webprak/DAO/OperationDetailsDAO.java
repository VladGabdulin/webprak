package com.cmc.sp.webprak.DAO;

import com.cmc.sp.webprak.classes.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class OperationDetailsDAO extends InheritedDAO<OperationDetails, Long>{
    public OperationDetailsDAO(Class<OperationDetails> entityClass) {
        super(entityClass);
    }

    List<OperationDetails> getOperationsDetailsByOperation(Operations operation) {
        try (Session session = sessionFactory.openSession()) {
            Query<OperationDetails> query = session.createQuery("FROM operation_details WHERE operation_id = :gotOperation", OperationDetails.class)
                    .setParameter("gotOperation", operation.getId());
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
    List<OperationDetails> getOperationsDetailsByProduct(Products product) {
        try (Session session = sessionFactory.openSession()) {
            Query<OperationDetails> query = session.createQuery("FROM operation_details WHERE product = :gotProduct", OperationDetails.class)
                    .setParameter("gotProduct", product.getId());
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
}
