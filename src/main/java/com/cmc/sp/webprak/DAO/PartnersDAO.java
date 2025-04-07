package com.cmc.sp.webprak.DAO;

import com.cmc.sp.webprak.classes.Partners;
import com.cmc.sp.webprak.classes.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PartnersDAO extends InheritedDAO<Partners, Long> {
    public PartnersDAO() {
        super(Partners.class);
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }

    public List<Partners> getPartnersByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Partners> query = session.createQuery("FROM Partners WHERE name LIKE :gotName", Partners.class)
                    .setParameter("gotName", likeExpr(name));
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };

    public List<Partners> getPartnersByType(Partners.PartnerType type) {
        try (Session session = sessionFactory.openSession()) {
            Query<Partners> query = session.createQuery("FROM Partners WHERE partnerType = :gotType", Partners.class)
                    .setParameter("gotType", type);
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
}
