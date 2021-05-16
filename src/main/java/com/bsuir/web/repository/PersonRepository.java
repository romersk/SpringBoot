package com.bsuir.web.repository;

import com.bsuir.web.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("SELECT u FROM Person u WHERE u.firstName = ?1")
    Person findByName(String name);
}