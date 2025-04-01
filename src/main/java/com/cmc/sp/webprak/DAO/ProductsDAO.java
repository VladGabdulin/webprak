package com.cmc.sp.webprak.DAO;

import com.cmc.sp.webprak.classes.Products;
import com.cmc.sp.webprak.classes.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ProductsDAO extends InheritedDAO<Products, Long> {
    public ProductsDAO(Class<Products> entityClass) {
        super(entityClass);
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }

    List<Products> getProductsByCategory(String category) {
        try (Session session = sessionFactory.openSession()) {
            Query<Products> query = session.createQuery("FROM products WHERE category LIKE :gotCategory", Products.class)
                    .setParameter("gotCategory", likeExpr(category));
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    };
}
