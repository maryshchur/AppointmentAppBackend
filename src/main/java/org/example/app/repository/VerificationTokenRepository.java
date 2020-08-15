package org.example.app.repository;

import org.example.app.entities.VerificationToken;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
   VerificationToken save(VerificationToken verificationToken);

     Optional<VerificationToken> findByToken(String token);

    void deleteById(Long aLong);
}
