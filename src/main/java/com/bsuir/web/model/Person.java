package com.bsuir.web.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table (name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPerson;

    @Column(nullable = false, length = 64)
    private String firstName;

    @Column(nullable = false, length = 64)
    private String secondName;

    @Column(nullable = false, length = 64)
    private String workPlace;

    @OneToOne(mappedBy = "person")
    private Users users;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private Collection<Shoes> shoesCollection;

    public Long getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Long idPerson) {
        this.idPerson = idPerson;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Collection<Shoes> getShoesCollection() {
        return shoesCollection;
    }

    public void setShoesCollection(Collection<Shoes> shoesCollection) {
        this.shoesCollection = shoesCollection;
    }
}
