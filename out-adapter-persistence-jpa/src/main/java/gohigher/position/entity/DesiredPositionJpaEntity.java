package gohigher.position.entity;

import gohigher.user.entity.UserJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "desiredPosition")
@Entity
public class DesiredPositionJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserJpaEntity user;

	@ManyToOne
	@JoinColumn(name = "position_id")
	private PositionJpaEntity position;

	public DesiredPositionJpaEntity(UserJpaEntity user, PositionJpaEntity position) {
		this.user = user;
		this.position = position;
	}
}
