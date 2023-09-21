package gohigher.position;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PositionFixture {

	DEVELOPER("개발자"),
	DESIGNER("디자이너"),
	PROJECT_MANAGER("PN"),
	;

	private final String value;

	public Position toDomain() {
		return new Position(value);
	}
}
