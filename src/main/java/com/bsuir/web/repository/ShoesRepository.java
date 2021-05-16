package com.bsuir.web.repository;

import com.bsuir.web.model.Shoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoesRepository extends JpaRepository<Shoes, Long> {

    @Query("SELECT u FROM Shoes u WHERE u.nameShoes = ?1")
    Shoes findByName(String name);
}
