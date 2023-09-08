package gohigher.application.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<ApplicationJpaEntity, Long> {

	@Modifying
	@Query("UPDATE ApplicationJpaEntity a "
		+ "SET a.currentProcessOrder = :order "
		+ "WHERE a.id = :id")
	void updateCurrentProcessOrder(long id, int order);
}
