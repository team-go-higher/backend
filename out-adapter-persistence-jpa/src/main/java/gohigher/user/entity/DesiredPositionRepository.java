package gohigher.user.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DesiredPositionRepository extends JpaRepository<DesiredPositionJpaEntity, Long> {
	Optional<DesiredPositionJpaEntity> findByUserIdAndPositionId(Long userId, Long positionId);
}
