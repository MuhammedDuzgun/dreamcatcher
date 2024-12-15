package com.yapai.dreamcatcher.repository;

import com.yapai.dreamcatcher.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IUserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
