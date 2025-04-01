package com.cmc.sp.webprak.DAO;

import com.cmc.sp.webprak.classes.Partners;
import com.cmc.sp.webprak.classes.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class PartnersDAO extends InheritedDAO<Partners, Long> {
    public PartnersDAO(Class<Partners> entityClass) {
        super(entityClass);
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }

    List<Partners> getPartnersByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Partners> query = session.createQuery("FROM partners WHERE name LIKE :gotName", Partners.class)
                    .setParameter("gotName", likeExpr(name));
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
    List<Partners> getPartnersByType(Partners.PartnerType type) {
        try (Session session = sessionFactory.openSession()) {
            Query<Partners> query = session.createQuery("FROM partners WHERE partner_type = :gotType", Partners.class)
                    .setParameter("gotType", String.valueOf(type));
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
}
