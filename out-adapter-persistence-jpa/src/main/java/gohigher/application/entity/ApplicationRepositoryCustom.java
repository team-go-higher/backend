package gohigher.application.entity;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import gohigher.application.search.ApplicationSortingType;
import gohigher.common.ProcessType;

public interface ApplicationRepositoryCustom {

    Slice<ApplicationJpaEntity> findAllByUserId(Long userId, Pageable pageable, ApplicationSortingType sortingType,
        List<ProcessType> process, List<String> scheduled, String companyName);
}
