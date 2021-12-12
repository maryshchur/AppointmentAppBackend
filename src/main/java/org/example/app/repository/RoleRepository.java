package org.example.app.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository {
//    @Modifying
//    @Query("select r.name from users r join #{#entityName} as u on  u.role_id = r.id where u.id = ?1")
//    String getUserRoleById(Long id);
}
