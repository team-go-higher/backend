package gohigher.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.position.port.out.PositionPersistenceQueryPort;
import gohigher.user.port.out.DesiredPositionPersistenceCommandPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("PositionCommandService 클래스의")
class DesiredPositionCommandServiceTest {

	@Mock
	private PositionPersistenceQueryPort positionPersistenceQueryPort;

	@Mock
	private DesiredPositionPersistenceCommandPort desiredPositionPersistenceCommandPort;

	@InjectMocks
	private DesiredDesiredPositionCommandService desiredPositionCommandService;

	@DisplayName("saveDesiredPositions 메서드는")
	@Nested
	class Describe_saveDesiredPosition {

		@DisplayName("n개의 position의 ID를 받으면")
		@Nested
		class Context_input_position_ids {

			private final Long userId = 1L;
			private final List<Long> positionIds = new ArrayList<>(List.of(1L, 2L));

			@DisplayName("desiredPositions를 저장한다.")
			@Test
			void it_void_after_save_desiredPositions() {
				// given
				when(positionPersistenceQueryPort.existsByIds(anyList())).thenReturn(true);
				doNothing().when(desiredPositionPersistenceCommandPort)
					.saveDesiredPositions(anyLong(), anyLong(), anyList());

				// when & then
				assertThatNoException().isThrownBy(
					() -> desiredPositionCommandService.saveDesiredPositions(userId, positionIds));
			}
		}
	}
}
