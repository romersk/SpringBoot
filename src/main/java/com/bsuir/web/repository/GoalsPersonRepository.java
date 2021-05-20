package com.bsuir.web.repository;

import com.bsuir.web.model.GoalsPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoalsPersonRepository  extends JpaRepository<GoalsPerson, Long> {

    @Query("SELECT u FROM GoalsPerson u WHERE u.person.idPerson = ?1")
    List<GoalsPerson> findByPersonId(Long id);

    @Query("SELECT u FROM GoalsPerson u WHERE u.goals.idGoal = ?1")
    GoalsPerson findByGoalsId(Long id);
}
