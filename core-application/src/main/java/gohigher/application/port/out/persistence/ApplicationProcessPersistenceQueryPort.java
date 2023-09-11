package gohigher.application.port.out.persistence;

import gohigher.common.Process;

import java.util.Optional;

public interface ApplicationProcessPersistenceQueryPort {

    Optional<Process> findByIdAndApplicationId(Long id, Long applicationId);
}
