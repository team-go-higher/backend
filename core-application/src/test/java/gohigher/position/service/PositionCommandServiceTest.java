package gohigher.position.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.global.exception.GoHigherException;
import gohigher.position.Position;
import gohigher.position.port.out.PositionPersistenceCommandPort;
import gohigher.position.port.out.PositionPersistenceQueryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("PositionCommandService 클래스의")
class PositionCommandServiceTest {

	@Mock
	private PositionPersistenceCommandPort positionPersistenceCommandPort;

	@Mock
	private PositionPersistenceQueryPort positionPersistenceQueryPort;

	@InjectMocks
	private PositionCommandService positionCommandService;

	@DisplayName("saveAll 메서드는")
	@Nested
	class Describe_saveAll {

		@DisplayName("n개의 새로운 position의 value를 입력받을 때,")
		@Nested
		class Context_request_new_positions {

			Long userId = 1L;
			List<String> positions = List.of("대통령", "국회의원");

			@DisplayName("position들을 저장하고 n개의 id를 반환한다.")
			@Test
			void it_returns_ids_size_of_positions() {
				// given
				given(positionPersistenceQueryPort.existsByValuesAndMadeByAdmin(positions)).willReturn(false);
				List<Position> personalPositions = positions.stream()
					.map(Position::new)
					.toList();
				given(positionPersistenceCommandPort.saveAll(personalPositions)).willReturn(List.of(1L, 2L));

				// when
				List<Long> personalPositionIds = positionCommandService.savePersonalPositions(userId, positions);

				// then
				assertThat(personalPositionIds).hasSize(positions.size());
			}

			@DisplayName("관리자가 저장해둔 position에 있는게 있다면 예외를 발생한다.")
			@Test
			void it_returns_error_if_existed_position_already() {
				// given
				given(positionPersistenceQueryPort.existsByValuesAndMadeByAdmin(positions)).willReturn(true);

				// when & then
				assertThatThrownBy(() -> positionCommandService.savePersonalPositions(userId, positions))
					.isInstanceOf(GoHigherException.class).hasMessage("목록에 존재하는 포지션입니다.");
			}
		}
	}
}
