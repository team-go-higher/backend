package gohigher.application.port.out.persistence;

import java.util.List;

import gohigher.application.Application;

public interface ApplicationPersistenceQueryPort {

	List<Application> findByIdAndMonth(Long userId, int year, int month);
}
