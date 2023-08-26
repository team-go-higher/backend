package gohigher.domain;

import gohigher.user.Provider;
import gohigher.user.Role;
import gohigher.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class UserJpaEntity {

	@Id
	@GeneratedValue
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

	public static UserJpaEntity from(final User user) {
		return new UserJpaEntity(user.getEmail(), user.getRole(), user.getProvider());
	}

	public User convert() {
		return new User(id, email, role, provider);
	}
}
