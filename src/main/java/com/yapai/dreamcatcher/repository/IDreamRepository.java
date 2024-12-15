package com.yapai.dreamcatcher.repository;

import com.yapai.dreamcatcher.entity.Dream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDreamRepository extends JpaRepository<Dream, Long> {
}
