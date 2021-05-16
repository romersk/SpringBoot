package com.bsuir.web.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table (name = "shoes")
public class Shoes {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long idShoes;

    @Column(nullable = false, length = 45)
    private String nameShoes;

    @Column(nullable = false)
    private Double costs;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    public Long getIdShoes() {
        return idShoes;
    }

    public void setIdShoes(Long idShoes) {
        this.idShoes = idShoes;
    }

    public String getNameShoes() {
        return nameShoes;
    }

    public void setNameShoes(String nameShoes) {
        this.nameShoes = nameShoes;
    }

    public Double getCosts() {
        return costs;
    }

    public void setCosts(Double costs) {
        this.costs = costs;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
