package com.example.CampusJobBoard.Repositories;

import com.example.CampusJobBoard.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    Optional<User> findByEmail(String email);
  
    boolean existsByEmail(String email);


}
