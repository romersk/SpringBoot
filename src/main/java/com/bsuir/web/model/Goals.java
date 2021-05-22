package com.bsuir.web.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table (name = "goals")
public class Goals {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO)
    private Long idGoal;

    @Column(nullable = false, length = 64)
    private String nameGoal;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "goals", fetch = FetchType.EAGER)
    private Collection<GoalsPerson> goalsPeople;

    public Collection<GoalsPerson> getGoalsPeople() {
        return goalsPeople;
    }

    public void setGoalsPeople(Collection<GoalsPerson> goalsPeople) {
        this.goalsPeople = goalsPeople;
    }

    public Long getIdGoal() {
        return idGoal;
    }

    public void setIdGoal(Long idGoal) {
        this.idGoal = idGoal;
    }

    public String getNameGoal() {
        return nameGoal;
    }

    public void setNameGoal(String nameGoal) {
        this.nameGoal = nameGoal;
    }

}
