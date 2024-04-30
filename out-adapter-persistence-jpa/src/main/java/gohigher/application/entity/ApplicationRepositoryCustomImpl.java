package gohigher.application.entity;

import static gohigher.application.entity.QApplicationJpaEntity.applicationJpaEntity;
import static gohigher.application.entity.QApplicationProcessJpaEntity.applicationProcessJpaEntity;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import gohigher.application.entity.dto.response.ProcessWithApplicationResponse;
import gohigher.application.search.ApplicationSortingType;
import gohigher.common.ProcessType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApplicationRepositoryCustomImpl implements ApplicationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<ApplicationJpaEntity> findAllByUserId(Long userId, Pageable pageable, ApplicationSortingType sortingType,
        List<ProcessType> process, List<String> scheduled, String companyName) {

        JPAQuery<ProcessWithApplicationResponse> query = jpaQueryFactory
            .select(Projections.bean(
                ProcessWithApplicationResponse.class,
                applicationJpaEntity.id.as("applicationId"),
                applicationJpaEntity.companyName,
                applicationJpaEntity.position,
                applicationJpaEntity.specificPosition,
                applicationProcessJpaEntity.id.as("processId"),
                applicationProcessJpaEntity.type,
                applicationProcessJpaEntity.description,
                applicationProcessJpaEntity.schedule
            ))
            .from(applicationProcessJpaEntity)
            .join(applicationProcessJpaEntity.application, applicationJpaEntity)
            .where(
                eqUserId(userId),
                applicationProcessJpaEntity.application.deleted.eq(false),
                inProcessType(process),
                containsCompanyName(companyName)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(selectOrderSpecifierAboutFindAll(sortingType))
        ;

        List<ApplicationJpaEntity> applications = convertToApplicationJpaEntity(query);

        boolean hasNext = applications.size() > pageable.getPageSize();
        if (hasNext) {
            applications.remove(applications.size() - 1);
        }

        return new SliceImpl<>(applications, pageable, hasNext);
    }

    private BooleanExpression containsCompanyName(String companyName) {
        if (companyName == null) {
            companyName = "";
        }
        return applicationProcessJpaEntity.application.companyName.contains(companyName);
    }

    private BooleanExpression eqUserId(Long userId) {
        if (userId == null) {
            return applicationProcessJpaEntity.application.userId.isNull();
        }
        return applicationProcessJpaEntity.application.userId.eq(userId);
    }

    private BooleanExpression inProcessType(List<ProcessType> process) {
        if (process.isEmpty()) {
            return null;
        }
        return applicationProcessJpaEntity.type.in(process);
    }

    private OrderSpecifier<?> selectOrderSpecifierAboutFindAll(ApplicationSortingType sortingType) {
        return switch (sortingType) {
            case CREATED -> applicationProcessJpaEntity.id.desc();
            case PROCESS_TYPE -> applicationProcessJpaEntity.schedule.asc();
            case REVERSE_PROCESS_TYPE -> applicationProcessJpaEntity.schedule.desc();
            case CLOSING -> applicationProcessJpaEntity.id.asc();
        };
    }

    private List<ApplicationJpaEntity> convertToApplicationJpaEntity(JPAQuery<ProcessWithApplicationResponse> query) {
        return query.fetch().stream()
            .map(processResponse ->
                ApplicationJpaEntity.builder()
                    .id(processResponse.getApplicationId())
                    .companyName(processResponse.getCompanyName())
                    .position(processResponse.getPosition())
                    .specificPosition(processResponse.getSpecificPosition())
                    .processes(
                        List.of(
                            ApplicationProcessJpaEntity.builder()
                                .id(processResponse.getProcessId())
                                .type(processResponse.getType())
                                .description(processResponse.getDescription())
                                .schedule(processResponse.getSchedule())
                                .build())
                    )
                    .build())
            .collect(Collectors.toList());
    }
}
