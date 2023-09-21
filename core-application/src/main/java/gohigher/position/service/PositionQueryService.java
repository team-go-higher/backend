package gohigher.position.service;

import java.util.List;
import java.util.stream.Collectors;

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
		return positionPersistenceQueryPort.findAll()
			.stream()
			.map(PositionResponse::from)
			.toList();
	}
}
