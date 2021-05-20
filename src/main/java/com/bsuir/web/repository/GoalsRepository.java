package com.bsuir.web.repository;

import com.bsuir.web.model.Goals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalsRepository extends JpaRepository<Goals, Long> {
}
