package gohigher.application.port.out.persistence;

import java.time.LocalDate;
import java.util.List;

import gohigher.application.Application;

public interface ApplicationPersistenceQueryPort {

	List<Application> findByUserIdAndMonth(Long userId, int year, int month);

	List<Application> findByUserIdAndDate(long userId, LocalDate date);
}
