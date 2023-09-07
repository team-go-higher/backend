package gohigher.jwt;

import java.io.Serial;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import gohigher.user.Role;

public class RoleGrantedAuthority implements GrantedAuthority {

	@Serial
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	private static final String ROLE_PREFIX = "ROLE_";

	private final String role;

	public RoleGrantedAuthority(Role role) {
		Assert.hasText(role.toString(), "A granted authority textual representation is required");
		this.role = ROLE_PREFIX + role;
	}

	@Override
	public String getAuthority() {
		return this.role.substring(ROLE_PREFIX.length());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof RoleGrantedAuthority) {
			return this.role.equals(((RoleGrantedAuthority)obj).role);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.role.hashCode();
	}

	@Override
	public String toString() {
		return this.role;
	}
}
