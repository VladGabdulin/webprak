package com.cmc.sp.webprak.DAO;

import com.cmc.sp.webprak.classes.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UsersDAO extends InheritedDAO<Users, Long> {
    public UsersDAO(Class<Users> entityClass) {
        super(entityClass);
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }

    List<Users> getAllUsersByName(String userName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("FROM users WHERE name LIKE :gotName", Users.class)
                    .setParameter("gotName", likeExpr(userName));
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };

    List<Users> getAllUsersSameRole(String role) {
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("FROM users WHERE role LIKE :gotRole", Users.class)
                    .setParameter("gotRole", likeExpr(role));
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
}
