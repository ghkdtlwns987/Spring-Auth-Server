package com.amd.backend.Domain.User.Repository;

import com.amd.backend.Domain.User.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId);
    UserEntity findByEmail(String username);

    boolean existsByEmail(String username);
    boolean existsByName(String name);
}
