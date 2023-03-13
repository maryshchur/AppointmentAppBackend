package org.example.app.repository;

import org.example.app.entities.VerificationToken;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

    VerificationToken save(VerificationToken verificationToken);

    Optional<VerificationToken> findByToken(String token);

    void deleteById(Long aLong);

    void deleteAllByExpiryDateIsLessThanEqual(Date now);
    //todo delete custom query and change method name and test it
    @Modifying
    @Query("delete from VerificationToken where expiryDate<=?1")
    void deleteAllByExpiryDate(Timestamp now);

}
