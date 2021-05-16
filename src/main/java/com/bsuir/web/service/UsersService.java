package com.bsuir.web.service;

import com.bsuir.web.model.Users;
import com.bsuir.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    public List<Users> listAll()
    {
        return userRepository.findAll();
    }

    public void save(Users users)
    {
        userRepository.save(users);
    }

    public Users get(Long id)
    {
        return userRepository.findById(id).get();
    }

    public void delete(Long id)
    {
        userRepository.deleteById(id);
    }
}
