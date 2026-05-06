// src\main\java\com\projet\avance\repository\UserRepository.java
package com.projet.avance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projet.avance.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}