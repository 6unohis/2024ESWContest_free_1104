package com.goldenengineering.coffeebara.user.repository;

import com.goldenengineering.coffeebara.user.model.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {
    boolean existsByIdentifier(String identifier);
    boolean existsByIdentifierAndPassword(String identifier, String password);

    Optional<UserJpaEntity> findByIdentifierAndPassword(String identifier, String password);
}
