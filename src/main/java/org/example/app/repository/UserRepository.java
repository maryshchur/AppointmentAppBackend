package org.example.app.repository;

import org.example.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    Optional<User> findUserByEmail(String email);

    Optional<User> findById(Long id);

    boolean existsUserByEmail(String email);

    List<User> findAllByRoleId(Long role);
}
