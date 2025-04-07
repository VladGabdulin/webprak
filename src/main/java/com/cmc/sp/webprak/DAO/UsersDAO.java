package com.cmc.sp.webprak.DAO;

import com.cmc.sp.webprak.classes.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsersDAO extends InheritedDAO<Users, Long> {
    public UsersDAO() {
        super(Users.class);
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }

    public List<Users> getAllUsersByName(String userName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("FROM Users WHERE name LIKE :gotName", Users.class)
                    .setParameter("gotName", likeExpr(userName));
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };

    public List<Users> getAllUsersSameRole(String role) {
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("FROM Users WHERE role LIKE :gotRole", Users.class)
                    .setParameter("gotRole", likeExpr(role));
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
}
