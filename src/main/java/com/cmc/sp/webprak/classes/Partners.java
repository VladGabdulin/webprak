package com.cmc.sp.webprak.classes;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "partners")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Partners implements InheritedInterface<Long>{
    public enum PartnerType {
        supplier, consumer, both
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "partner_id")
    private Long id;

    @Column(nullable = false, name = "name")
    @lombok.NonNull
    private String name;

    @Column(nullable = false, name = "contact_info")
    @lombok.NonNull
    private String contactInfo;

    @Column(nullable = false, name = "partner_type")
    @lombok.NonNull
    private PartnerType partnerType;
}