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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

	public static ApplicationProcessJpaEntity of(ApplicationJpaEntity application, Process process, int order) {
		return new ApplicationProcessJpaEntity(null, application, process.type(), order, process.schedule(),
			process.description());
	}
}
