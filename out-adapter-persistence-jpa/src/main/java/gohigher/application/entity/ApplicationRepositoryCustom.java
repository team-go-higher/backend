package gohigher.application.entity;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ApplicationRepositoryCustom {

    Slice<ApplicationJpaEntity> findAllByUserId(Long userId, Pageable pageable, String sort, List<String> process,
        List<String> scheduled, String companyName);
}
