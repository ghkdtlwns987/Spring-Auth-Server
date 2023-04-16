package com.amd.backend.Domain.User.Entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * JPA Database 요소를 만드는 Entity입니다/
 * @author : 황시준
 * @since : 1.0
 */
@Data
@Entity
@Table(name="amd_users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length=50, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String encryptedPwd;

    @Column(nullable = false)
    private Date createAt;
}
