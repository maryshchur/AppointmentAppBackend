package org.example.app.repository;

import org.example.app.entities.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrizeRepository extends JpaRepository<Prize,Long> {
    Prize save(Prize prize);

    @Override
    Optional<Prize> findById(Long id);
}
