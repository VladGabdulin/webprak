package com.cmc.sp.webprak.DAO;

import com.cmc.sp.webprak.classes.Products;
import com.cmc.sp.webprak.classes.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductsDAO extends InheritedDAO<Products, Long> {
    public ProductsDAO() {
        super(Products.class);
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }

    public List<Products> getProductsByCategory(String category) {
        try (Session session = sessionFactory.openSession()) {
            Query<Products> query = session.createQuery("FROM Products WHERE category LIKE :gotCategory", Products.class)
                    .setParameter("gotCategory", likeExpr(category));
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
}
