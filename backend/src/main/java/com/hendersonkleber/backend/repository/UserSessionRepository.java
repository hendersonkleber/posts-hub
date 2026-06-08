package com.hendersonkleber.backend.repository;

import com.hendersonkleber.backend.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    Optional<UserSession> findByRefreshTokenHash(String refreshTokenHash);

    @Query("SELECT COUNT(*) > 0 FROM UserSession s WHERE s.refreshTokenHash = :refreshTokenHash AND s.user.id = :userId")
    boolean existsByRefreshTokenHashAndUserId(String refreshTokenHash, UUID userId);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query("UPDATE UserSession s SET s.revokedAt = :revokedAt WHERE s.refreshTokenHash = :refreshTokenHash AND s.revokedAt IS NULL")
    int revoke(String refreshTokenHash, Instant revokedAt);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query("UPDATE UserSession s SET s.revokedAt = :revokedAt WHERE s.user.id = :userId AND s.revokedAt IS NULL")
    int revokeByUserId(UUID userId, Instant revokedAt);

}
