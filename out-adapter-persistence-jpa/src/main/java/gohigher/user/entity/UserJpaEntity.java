package gohigher.user.entity;

import java.util.ArrayList;
import java.util.List;

import gohigher.position.Position;
import gohigher.position.entity.DesiredPositionJpaEntity;
import gohigher.position.entity.PositionJpaEntity;
import gohigher.user.Role;
import gohigher.user.User;
import gohigher.user.auth.Provider;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private Provider provider;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<DesiredPositionJpaEntity> desiredPositions;

	public UserJpaEntity(String email, Role role, Provider provider) {
		this(null, email, role, provider, new ArrayList<>());
	}

	public static UserJpaEntity from(User user) {
		return new UserJpaEntity(user.getEmail(), user.getRole(), user.getProvider());
	}

	public User toDomain() {
		List<Position> positions = desiredPositions.stream()
			.map(DesiredPositionJpaEntity::getPosition)
			.map(PositionJpaEntity::toDomain)
			.toList();

		return new User(id, email, role, provider, new ArrayList<>(positions));
	}
}
