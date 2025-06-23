package com.yapai.dreamcatcher.repository;

import com.yapai.dreamcatcher.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
