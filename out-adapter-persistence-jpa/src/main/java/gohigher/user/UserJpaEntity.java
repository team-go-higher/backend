package gohigher.user;

import gohigher.user.auth.Provider;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
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

	public UserJpaEntity(String email, Role role, Provider provider) {
		this.email = email;
		this.role = role;
		this.provider = provider;
	}

	public static UserJpaEntity from(User user) {
		return new UserJpaEntity(user.getEmail(), user.getRole(), user.getProvider());
	}

	public User convert() {
		return new User(id, email, role, provider);
	}
}
