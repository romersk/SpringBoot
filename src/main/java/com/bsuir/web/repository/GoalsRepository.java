package com.bsuir.web.repository;

import com.bsuir.web.model.Goals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GoalsRepository extends JpaRepository<Goals, Long> {

    @Query("SELECT u FROM Goals u WHERE u.nameGoal = ?1")
    Goals findByName(String name);
}
