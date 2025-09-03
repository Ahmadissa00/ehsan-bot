package com.e7san.bot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class E7sanUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @Column(nullable = false, unique = true, length = 50) // username لازم يكون فريد
    private String username;

    @Column(nullable = false, length = 15) // رقم الهاتف
    private String phoneNumber;

    @Column(nullable = false, unique = true) // الإيميل لازم يكون فريد
    private String email;

    @Column(nullable = false, length = 20) // role: admin/user...
    private String role;
}
