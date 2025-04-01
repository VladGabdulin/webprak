package com.cmc.sp.webprak.DAO;

import com.cmc.sp.webprak.classes.Operations;
import com.cmc.sp.webprak.classes.Partners;
import com.cmc.sp.webprak.classes.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class OperationsDAO extends InheritedDAO<Operations, Long> {
    public OperationsDAO(Class<Operations> entityClass) {
        super(entityClass);
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }

    List<Operations> getOperationsByType(Operations.OperationType type) {
        try (Session session = sessionFactory.openSession()) {
            Query<Operations> query = session.createQuery("FROM operations WHERE operation_type = :gotType", Operations.class)
                    .setParameter("gotType", String.valueOf(type));
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
    List<Operations> getOperationsByPartner(Partners partner) {
        try (Session session = sessionFactory.openSession()) {
            Query<Operations> query = session.createQuery("FROM operations WHERE partner_id = :gotPartner", Operations.class)
                    .setParameter("gotPartner", partner.getId());
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    }

    List<Operations> getOperationsByUser(Users user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Operations> query = session.createQuery("FROM operations WHERE user_id = :gotUser", Operations.class)
                    .setParameter("gotUser", user.getId());
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
}
