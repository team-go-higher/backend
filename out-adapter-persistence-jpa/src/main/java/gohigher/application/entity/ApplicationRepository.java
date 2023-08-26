package gohigher.application.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<ApplicationJpaEntity, Long> {
}
