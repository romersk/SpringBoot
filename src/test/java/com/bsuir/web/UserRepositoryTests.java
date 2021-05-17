package com.bsuir.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.bsuir.web.model.Person;
import com.bsuir.web.model.Shoes;
import com.bsuir.web.model.Users;
import com.bsuir.web.repository.PersonRepository;
import com.bsuir.web.repository.ShoesRepository;
import com.bsuir.web.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repo;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ShoesRepository shoesRepository;

    @Test
    public void testCreateUser() {
        Users user = new Users();
        user.setEmail("rome@gmail.com");
        user.setPassword("rome");
        user.setRole(Long.valueOf(1));
        Person person = new Person();
        person.setFirstName("Роман");
        person.setSecondName("Евдоокимов");
        person.setWorkPlace("ОАО Марко");
        user.setPerson(person);


        Users savedUser = repo.save(user);

        Users existUser = entityManager.find(Users.class, savedUser.getId());

        assertThat(user.getEmail()).isEqualTo(existUser.getEmail());

    }

    @Test
    public void testSearch()
    {
        String email = "reet@gmail.com";
        Users users = repo.findByEmail(email);
        System.out.println(users.getId());
        assertThat(users).isNotNull();
    }

    @Test
    public void testUpdate()
    {
        Users user = new Users();
        user.setId(Long.valueOf(7));
        user.setEmail("igor@gmail.com");
        user.setPassword("igoR2004");
        user.setRole(Long.valueOf(3));
        Person person = new Person();
        person.setIdPerson(Long.valueOf(6));
        person.setFirstName("Игорь");
        person.setSecondName("Денисов");
        person.setWorkPlace("ОАО Марко");
        user.setPerson(person);



        //repo.updateUsers(user, user.getId());

        repo.save(user);

        Users existUser = entityManager.find(Users.class, user.getId());

        assertThat(user.getId()).isEqualTo(existUser.getId());
    }

    @Test
    public void testString()
    {
        List<Users> userList = repo.findAll();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < userList.size(); i++) {
            stringBuilder.append(userList.get(i).getId());
            stringBuilder.append(" ");
            stringBuilder.append(userList.get(i).getEmail());
            stringBuilder.append(" ");
            stringBuilder.append(userList.get(i).getPerson().getFirstName());
            stringBuilder.append(" ");
            stringBuilder.append(userList.get(i).getPerson().getSecondName());
            stringBuilder.append(" ");
            stringBuilder.append(userList.get(i).getPerson().getWorkPlace());
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString());
    }

    @Test
    public void testAddShoes()
    {
        Shoes shoes = new Shoes();
        shoes.setIdShoes(Long.valueOf(3));
        shoes.setNameShoes("Полуботинки женские №1067");
        shoes.setCosts(Double.valueOf(45.99));

        Person person = personRepository.findByName("Василий");

        shoes.setPerson(person);

        shoesRepository.save(shoes);

        assertThat(shoes.getIdShoes()).isEqualTo(Long.valueOf(3));

    }
}
