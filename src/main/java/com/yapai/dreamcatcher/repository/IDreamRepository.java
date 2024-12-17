package com.yapai.dreamcatcher.repository;

import com.yapai.dreamcatcher.entity.Dream;
import com.yapai.dreamcatcher.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDreamRepository extends JpaRepository<Dream, Long> {
    List<Dream> findByUser(User user);
}
