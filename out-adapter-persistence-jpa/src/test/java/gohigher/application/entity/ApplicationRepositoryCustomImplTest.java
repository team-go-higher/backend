package gohigher.application.entity;

import static gohigher.application.ApplicationFixture.COUPANG_APPLICATION;
import static gohigher.application.ApplicationFixture.KAKAO_APPLICATION;
import static gohigher.application.ApplicationFixture.LINE_APPLICATION;
import static gohigher.application.ApplicationFixture.NAVER_APPLICATION;
import static gohigher.application.ProcessFixture.DOCUMENT;
import static gohigher.application.ProcessFixture.TEST;
import static gohigher.fixtureConverter.ApplicationFixtureConverter.convertToApplicationEntity;
import static gohigher.fixtureConverter.ApplicationFixtureConverter.convertToApplicationProcessEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import gohigher.JpaQueryTest;
import gohigher.application.Application;
import gohigher.application.search.ApplicationSortingType;
import gohigher.common.Process;
import gohigher.common.ProcessType;

@DisplayName("ApplicationRepositoryCustomImpl 클래스의")
@JpaQueryTest
class ApplicationRepositoryCustomImplTest {

    @Autowired
    private ApplicationRepositoryCustomImpl applicationRepositoryCustom;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationProcessRepository applicationProcessRepository;

    @DisplayName("findAllByUserId 메소드는")
    @Nested
    class Describe_findAllByUserId {

        private final LocalDateTime today = LocalDateTime.now();
        private final LocalDateTime yesterday = today.minusDays(1);
        private final LocalDateTime tomorrow = today.plusDays(1);

        private final Long userId = 0L;
        private final PageRequest pageRequest = PageRequest.of(0, 10);

        private ApplicationJpaEntity naverApplicationEntity;
        private ApplicationJpaEntity kakaoApplicationEntity;
        private ApplicationJpaEntity coupangApplicationEntity;

        @BeforeEach
        void setUp() {
            Application naverApplication = NAVER_APPLICATION.toDomain(TEST.toDomainWithSchedule(yesterday));
            naverApplicationEntity = saveApplicationAndProcesses(userId, naverApplication);

            Application kakoaApplication = KAKAO_APPLICATION.toDomain(TEST.toDomainWithSchedule(tomorrow));
            kakaoApplicationEntity = saveApplicationAndProcesses(userId, kakoaApplication);

            Application coupangApplication = COUPANG_APPLICATION.toDomain(TEST.toDomainWithSchedule(today));
            coupangApplicationEntity = saveApplicationAndProcesses(userId, coupangApplication);
        }

        @DisplayName("조회를 요청할 경우")
        @Nested
        class Context_request {

            @DisplayName("삭제되지 않은 현재 프로세스 전형을 조회한다.")
            @Test
            void it_returns_processes() {
                Application deletedApplication = LINE_APPLICATION.toDomain();
                ApplicationJpaEntity deletedApplicationJpaEntity = saveApplicationAndProcesses(userId, deletedApplication);
                deletedApplicationJpaEntity.delete();

                Slice<ApplicationJpaEntity> applications = applicationRepositoryCustom.findAllByUserId(
                    userId, pageRequest, ApplicationSortingType.CREATED, List.of(), null, null);

                assertAll(
                    () -> assertThat(applications.getContent()).hasSizeGreaterThan(1),
                    () -> assertThat(applications.getContent().get(0).getProcesses()).hasSize(1),
                    () -> assertThat(applications.getContent()).doesNotContain(deletedApplicationJpaEntity)
                );
            }
        }

        @DisplayName("정렬 기준을 생략할 경우")
        @Nested
        class Context_without_scheduled {

            @DisplayName("지원서를 작성한 순서대로 조회한다.")
            @Test
            void it_returns_desc_id() {
                ApplicationSortingType sortingType = ApplicationSortingType.CREATED;

                Slice<ApplicationJpaEntity> applications = applicationRepositoryCustom.findAllByUserId(
                    userId, pageRequest, sortingType, List.of(), null, null);

                assertAll(
                    () -> assertThat(applications.getNumberOfElements()).isEqualTo(3),
                    () -> assertThat(applications.getContent().get(0).getId()).isEqualTo(coupangApplicationEntity.getId()),
                    () -> assertThat(applications.getContent().get(1).getId()).isEqualTo(kakaoApplicationEntity.getId()),
                    () -> assertThat(applications.getContent().get(2).getId()).isEqualTo(naverApplicationEntity.getId())
                );
            }
        }

        @DisplayName("정렬 기준이 '전형순' 일 경우")
        @Nested
        class Context_with_scheduled {

            @DisplayName("전형일 오름차순으로 정렬되어 조회한다.")
            @Test
            void it_returns_asc_scheduled() {
                ApplicationSortingType sortingType = ApplicationSortingType.PROCESS_TYPE;

                Slice<ApplicationJpaEntity> applications = applicationRepositoryCustom.findAllByUserId(
                    userId, pageRequest, sortingType, List.of(), null, null);

                assertAll(
                    () -> assertThat(applications.getNumberOfElements()).isEqualTo(3),
                    () -> assertThat(applications.getContent().get(0).getId()).isEqualTo(naverApplicationEntity.getId()),
                    () -> assertThat(applications.getContent().get(1).getId()).isEqualTo(coupangApplicationEntity.getId()),
                    () -> assertThat(applications.getContent().get(2).getId()).isEqualTo(kakaoApplicationEntity.getId())
                );
            }
        }

        @DisplayName("정렬 기준이 '전형역순' 일 경우")
        @Nested
        class Context_with_re_scheduled {

            @DisplayName("전형일 내림차순으로 정렬되어 조회한다.")
            @Test
            void it_returns_desc_scheduled() {
                ApplicationSortingType sortingType = ApplicationSortingType.REVERSE_PROCESS_TYPE;

                Slice<ApplicationJpaEntity> applications = applicationRepositoryCustom.findAllByUserId(
                    userId, pageRequest, sortingType, List.of(), null, null);

                assertAll(
                    () -> assertThat(applications.getNumberOfElements()).isEqualTo(3),
                    () -> assertThat(applications.getContent().get(0).getId()).isEqualTo(kakaoApplicationEntity.getId()),
                    () -> assertThat(applications.getContent().get(1).getId()).isEqualTo(coupangApplicationEntity.getId()),
                    () -> assertThat(applications.getContent().get(2).getId()).isEqualTo(naverApplicationEntity.getId())
                );
            }
        }

        // @DisplayName("정렬 기준이 '마감임박순' 일 경우")
        // @Nested
        // class Context_with_closing {
        //
        //     @DisplayName("마감일이 가까운 순으로 정렬되어 조회한다.")
        //     @Test
        //     void it_returns_asc_deadline() {
        //         ApplicationSortingType sortingType = ApplicationSortingType.CLOSING;
        //
        //         Slice<ApplicationJpaEntity> applications = applicationRepositoryCustom.findAllByUserId(
        //             userId, pageRequest, sortingType, null, null, null);
        //
        //         assertAll(
        //             () -> assertThat(applications.getNumberOfElements()).isEqualTo(3),
        //             () -> assertThat(applications.getContent().get(0).getCompanyName()).isEqualTo(kakaoApplicationEntity.getCompanyName()),
        //             () -> assertThat(applications.getContent().get(1).getCompanyName()).isEqualTo(coupangApplicationEntity.getCompanyName()),
        //             () -> assertThat(applications.getContent().get(2).getCompanyName()).isEqualTo(naverApplicationEntity.getCompanyName())
        //         );
        //     }
        // }

        @DisplayName("전형별로 보기 필터링을 선택하지 않을 경우")
        @Nested
        class Context_without_process_filter {

            @DisplayName("전체 항목을 조회한다.")
            @Test
            void it_returns_all_applications() {
                List<ProcessType> processTypes = List.of();

                Slice<ApplicationJpaEntity> applications = applicationRepositoryCustom.findAllByUserId(
                    userId, pageRequest, ApplicationSortingType.CREATED, processTypes, null, null);

                List<Long> ids = applications.getContent().stream()
                    .map(ApplicationJpaEntity::getId)
                    .toList();
                assertAll(
                    () -> assertThat(applications.getNumberOfElements()).isEqualTo(3),
                    () -> assertThat(ids).contains(naverApplicationEntity.getId(), kakaoApplicationEntity.getId(), coupangApplicationEntity.getId())
                );
            }
        }

        @DisplayName("전형별로 보기 필터링을 선택할 경우")
        @Nested
        class Context_with_process_filter {

            @DisplayName("선택된 전형 목록만 조회한다.")
            @Test
            void it_returns_selected_applications() {
                List<ProcessType> processTypes = List.of(ProcessType.DOCUMENT);
                Application naverApplication = NAVER_APPLICATION.toDomain(DOCUMENT.toDomain());
                ApplicationJpaEntity applicationJpaEntity = saveApplicationAndProcesses(userId, naverApplication);

                Slice<ApplicationJpaEntity> applications = applicationRepositoryCustom.findAllByUserId(
                    userId, pageRequest, ApplicationSortingType.CREATED, processTypes, null, null);

                List<Long> ids = applications.getContent().stream()
                    .map(ApplicationJpaEntity::getId)
                    .toList();
                assertThat(ids).contains(applicationJpaEntity.getId());
            }
        }

        @DisplayName("종료된 전형 필터링을 선택하지 않을 경우")
        @Nested
        class Context_without_scheduled_filter {

            @DisplayName("전체 항목을 조회한다.")
            @Test
            void it_returns_all_applications() {

            }
        }

        @DisplayName("종료된 전형 필터링을 선택할 경우")
        @Nested
        class Context_with_scheduled_filter {

            @DisplayName("선택된 전형 목록만 조회한다.")
            @Test
            void it_returns_selected_applications() {

            }
        }

        @DisplayName("회사명을 검색할 경우")
        @Nested
        class Context_with_searching_company_name {

            @DisplayName("해당 회사명을 가지는 전형 목록만 조회한다.")
            @Test
            void it_returns_contained_applications_company_name() {
                String companyName = naverApplicationEntity.getCompanyName();

                Slice<ApplicationJpaEntity> applications = applicationRepositoryCustom.findAllByUserId(
                    userId, pageRequest, ApplicationSortingType.CREATED, List.of(), null, companyName);

                List<Long> ids = applications.getContent().stream()
                    .map(ApplicationJpaEntity::getId)
                    .toList();
                assertThat(ids).contains(naverApplicationEntity.getId());
            }
        }
    }

    public ApplicationJpaEntity saveApplicationAndProcesses(Long userId, Application application) {
        ApplicationJpaEntity applicationEntity = applicationRepository.save(
            convertToApplicationEntity(userId, application));

        for (Process process : application.getProcesses()) {
            ApplicationProcessJpaEntity applicationProcessJpaEntity = applicationProcessRepository.save(
                convertToApplicationProcessEntity(applicationEntity, process,
                    application.getCurrentProcess() == process));

            applicationEntity.addProcess(applicationProcessJpaEntity);
        }

        return applicationEntity;
    }
}
