package gohigher.position.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<PositionJpaEntity, Long> {

	boolean existsAllByIdIn(List<Long> positionIds);
}
