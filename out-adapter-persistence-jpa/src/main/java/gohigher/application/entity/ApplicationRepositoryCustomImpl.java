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
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
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
	public Slice<ApplicationJpaEntity> findAllByUserId(Long userId, Pageable pageable,
		ApplicationSortingType sortingType,
		List<ProcessType> process, List<Boolean> completed, String companyName) {

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
				applicationProcessJpaEntity.schedule,
				applicationJpaEntity.isCompleted
			))
			.from(applicationProcessJpaEntity)
			.join(applicationProcessJpaEntity.application, applicationJpaEntity)
			.where(
				eqUserId(userId),
				applicationProcessJpaEntity.application.deleted.eq(false),
				applicationProcessJpaEntity.isCurrent.eq(true),
				inProcessType(process),
				inCompleted(completed),
				containsCompanyName(companyName)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.orderBy(selectOrderSpecifierAboutFindAll(sortingType));

		List<ApplicationJpaEntity> applications = convertToApplicationJpaEntity(query);

		boolean hasNext = applications.size() > pageable.getPageSize();
		if (hasNext) {
			applications.remove(applications.size() - 1);
		}

		return new SliceImpl<>(applications, pageable, hasNext);
	}

	private BooleanExpression inCompleted(List<Boolean> completed) {
		if (completed == null || completed.isEmpty()) {
			return null;
		}
		return applicationJpaEntity.isCompleted.in(completed);
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
		if (process == null || process.isEmpty()) {
			return null;
		}
		return applicationProcessJpaEntity.type.in(process);
	}

	private OrderSpecifier<?> selectOrderSpecifierAboutFindAll(ApplicationSortingType sortingType) {
		return switch (sortingType) {
			case CREATED -> applicationProcessJpaEntity.id.desc();
			case PROCESS_TYPE -> sortByProcessType().asc();
			case REVERSE_PROCESS_TYPE -> sortByProcessType().desc();
			case CLOSING -> applicationProcessJpaEntity.id.asc();
		};
	}

    private NumberExpression<Integer> sortByProcessType() {
        int order = 0;
        return new CaseBuilder()
            .when(applicationProcessJpaEntity.type.eq(ProcessType.TO_APPLY)).then(order++)
            .when(applicationProcessJpaEntity.type.eq(ProcessType.DOCUMENT)).then(order++)
            .when(applicationProcessJpaEntity.type.eq(ProcessType.TEST)).then(order++)
            .when(applicationProcessJpaEntity.type.eq(ProcessType.INTERVIEW)).then(order++)
            .when(applicationProcessJpaEntity.type.eq(ProcessType.COMPLETE)).then(order++)
            .otherwise(order);
    }

	private List<ApplicationJpaEntity> convertToApplicationJpaEntity(JPAQuery<ProcessWithApplicationResponse> query) {
		return query.fetch().stream()
			.map(processResponse ->
				ApplicationJpaEntity.builder()
					.id(processResponse.getApplicationId())
					.companyName(processResponse.getCompanyName())
					.position(processResponse.getPosition())
					.specificPosition(processResponse.getSpecificPosition())
					.isCompleted(processResponse.getIsCompleted())
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
