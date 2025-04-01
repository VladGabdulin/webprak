package com.cmc.sp.webprak.classes;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "operation_details")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class OperationDetails implements InheritedInterface<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "operation_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operation_id")
    @ToString.Exclude
    @lombok.NonNull
    private Operations operation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    @lombok.NonNull
    private Products product;

    @Column(name = "quantity")
    @lombok.NonNull
    private Long quantity;
}