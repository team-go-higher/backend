package gohigher.application.entity;

import static gohigher.application.entity.QApplicationJpaEntity.applicationJpaEntity;
import static gohigher.application.entity.QApplicationProcessJpaEntity.applicationProcessJpaEntity;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApplicationRepositoryCustomImpl implements ApplicationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<ApplicationJpaEntity> findAllByUserId(Long userId, Pageable pageable, String sort,
        List<String> process, List<String> scheduled, String companyName) {

        List<ApplicationJpaEntity> applications = jpaQueryFactory.selectFrom(applicationJpaEntity)
            .fetchJoin()
            .where(
                applicationJpaEntity.userId.eq(userId),
                applicationJpaEntity.deleted.eq(false)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(applicationProcessJpaEntity.id.asc())
            .fetch();

        boolean hasNext = applications.size() > pageable.getPageSize();
        applications.remove(applications.size() - 1);

        return new SliceImpl<>(applications, pageable, hasNext);
    }
}
