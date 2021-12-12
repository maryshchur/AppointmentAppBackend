package org.example.app.repository;

import org.example.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

//@NoRepositoryBean
public interface UserRepository<T extends User> extends JpaRepository<T , Long> {

    T save(T user);

    @Query("select u from #{#entityName} as u where u.email = ?1 ")
    Optional<T> findUserByEmail(String email);

    @Query("select u from #{#entityName} as u where u.id = ?1 ")
    Optional<T> findById(Long id);

    //@Modifying
    @Query("select r.name from Role r join #{#entityName} as u on  u.role = r.id where u.id = ?1")
    String getUserRoleById(Long id);

//    @Query("select u from #{#entityName} as u where u.email = ?1 ")
//    boolean existsUserByEmail(String email);

    // List<T> findAllByRoleId(Long role);
}
