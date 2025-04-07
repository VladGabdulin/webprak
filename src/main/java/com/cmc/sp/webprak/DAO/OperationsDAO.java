package com.cmc.sp.webprak.DAO;

import com.cmc.sp.webprak.classes.Operations;
import com.cmc.sp.webprak.classes.Partners;
import com.cmc.sp.webprak.classes.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OperationsDAO extends InheritedDAO<Operations, Long> {
    public OperationsDAO() {
        super(Operations.class);
    }

    public List<Operations> getOperationsByType(Operations.OperationType type) {
        try (Session session = sessionFactory.openSession()) {
            Query<Operations> query = session.createQuery("FROM Operations WHERE operationType = :gotType", Operations.class)
                    .setParameter("gotType", type);
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
    public List<Operations> getOperationsByPartner(Partners partner) {
        try (Session session = sessionFactory.openSession()) {
            Query<Operations> query = session.createQuery("FROM Operations WHERE partner = :gotPartner", Operations.class)
                    .setParameter("gotPartner", partner);
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    }

    public List<Operations> getOperationsByUser(Users user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Operations> query = session.createQuery("FROM Operations WHERE user = :gotUser", Operations.class)
                    .setParameter("gotUser", user);
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
}
