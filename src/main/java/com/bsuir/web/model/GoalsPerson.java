package com.bsuir.web.model;

import javax.persistence.*;

@Entity
@Table (name = "goal_person")
public class GoalsPerson {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long idGoalsPerson;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id")
    private Goals goals;

    @Column(nullable = false)
    private Double rate;

    public Long getIdGoalsPerson() {
        return idGoalsPerson;
    }

    public void setIdGoalsPerson(Long idGoalsPerson) {
        this.idGoalsPerson = idGoalsPerson;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Goals getGoals() {
        return goals;
    }

    public void setGoals(Goals goals) {
        this.goals = goals;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
