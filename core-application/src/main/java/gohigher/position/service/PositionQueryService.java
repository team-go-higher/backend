package gohigher.position.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.position.port.in.PositionQueryPort;
import gohigher.position.port.in.PositionResponse;
import gohigher.position.port.out.PositionPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PositionQueryService implements PositionQueryPort {

	private final PositionPersistenceQueryPort positionPersistenceQueryPort;

	@Override
	public List<PositionResponse> findPositions() {
		return positionPersistenceQueryPort.findAllMadeByAdmin()
			.stream()
			.map(PositionResponse::from)
			.toList();
	}
}
