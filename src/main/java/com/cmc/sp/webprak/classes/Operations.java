package com.cmc.sp.webprak.classes;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "operations")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Operations implements InheritedInterface<Long>{
    public enum OperationType {
        in, out
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "operation_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "operation_type")
    @lombok.NonNull
    private OperationType operationType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "partner_id")
    @ToString.Exclude
    @lombok.NonNull
    private Partners partner;

    @Column(nullable = false, name = "operation_date")
    @lombok.NonNull
    private Date operationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @lombok.NonNull
    private Users user;
}