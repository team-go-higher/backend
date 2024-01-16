package gohigher.user.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("UPDATE RefreshTokenJpaEntity r SET r.value = :value WHERE r.userId = :userId")
	void updateValueByUserId(Long userId, String value);

	Optional<RefreshTokenJpaEntity> findByUserId(Long userId);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("DELETE FROM RefreshTokenJpaEntity r WHERE r.userId = :userId")
	void deleteByUserId(Long userId);
}
