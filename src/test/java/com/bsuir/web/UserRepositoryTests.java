package com.bsuir.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.bsuir.web.model.Users;
import com.bsuir.web.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repo;

    @Test
    public void testCreateUser() {
        Users user = new Users();
        user.setEmail("hello@gmail.com");
        user.setPassword("hello");
        user.setRole(Long.valueOf(2));


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
}
