package com.bsuir.web.repository;

import com.bsuir.web.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.email = ?1")
    Users findByEmail(String username);
}
