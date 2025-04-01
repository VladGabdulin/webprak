package com.cmc.sp.webprak.classes;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Users implements InheritedInterface<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_id")
    private Long id;

    @Column(nullable = false, name = "username")
    @lombok.NonNull
    private String name;

    @Column(nullable = false, name = "password_hash")
    @lombok.NonNull
    private String password;

    @Column(name = "role")
    @lombok.NonNull
    private String role;
}
