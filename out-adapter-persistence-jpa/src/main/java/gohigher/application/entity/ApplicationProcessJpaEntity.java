package gohigher.application.entity;

import java.time.LocalDateTime;

import gohigher.common.Process;
import gohigher.common.ProcessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "application_process")
@Entity
public class ApplicationProcessJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", nullable = false)
	private ApplicationJpaEntity application;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private ProcessType type;

	@Column(name = "orders")
	private int order;
	private LocalDateTime schedule;
	private String description;

	private boolean isCurrent;

	public static ApplicationProcessJpaEntity of(ApplicationJpaEntity application, Process process, boolean isCurrent) {
		return new ApplicationProcessJpaEntity(process.getId(), application, process.getType(), process.getOrder(),
			process.getSchedule(), process.getDescription(), isCurrent);
	}

	public void assignApplication(ApplicationJpaEntity application) {
		this.application = application;
	}

	public void changeToCurrentProcess() {
		isCurrent = true;
	}

	public void changeToNonCurrentProcess() {
		isCurrent = false;
	}

	public Process toDomain() {
		return new Process(id, type, description, schedule, order);
	}

	public void update(Process process) {
		this.type = process.getType();
		this.order = process.getOrder();
		this.schedule = process.getSchedule();
		this.description = process.getDescription();
	}
}
