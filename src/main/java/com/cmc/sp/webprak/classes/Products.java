package com.cmc.sp.webprak.classes;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Products implements InheritedInterface<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "product_id")
    private Long id;

    @Column(nullable = false, name = "product_name")
    @lombok.NonNull
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "quantity")
    @lombok.NonNull
    private Long quantity;
}