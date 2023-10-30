package gohigher.common;

import static gohigher.application.ApplicationErrorCode.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import gohigher.global.exception.GoHigherException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Processes {

	private static final int INITIAL_ORDER = 1;

	private final Map<ProcessType, List<Process>> values;

	public static Processes of(List<Process> processes) {
		return new Processes(groupingByProcessType(processes));
	}

	public static Processes initialFrom(Process process) {
		return initialFrom(List.of(process));
	}

	public static Processes initialFrom(List<Process> processes) {
		validateProcessesIsEmpty(processes);
		Map<ProcessType, List<Process>> values = groupingByProcessType(processes);

		for (ProcessType processType : values.keySet()) {
			assignNewOrder(values.get(processType));
		}

		return new Processes(values);
	}

	private static void validateProcessesIsEmpty(List<Process> processes) {
		if (processes.isEmpty()) {
			throw new GoHigherException(APPLICATION_PROCESS_NOT_EXIST);
		}
	}

	private static Map<ProcessType, List<Process>> groupingByProcessType(List<Process> processes) {
		return processes.stream()
			.collect(Collectors.groupingBy(Process::getType));
	}

	private static void assignNewOrder(List<Process> processes) {
		int nextOrder = INITIAL_ORDER;
		for (Process process : processes) {
			process.assignOrder(nextOrder);
			nextOrder++;
		}
	}

	public List<Process> getSortedValues() {
		return Arrays.stream(ProcessType.values())
			.filter(values::containsKey)
			.flatMap(processType -> values.get(processType).stream())
			.collect(Collectors.toList());
	}

	public void updateSchedule(Long processId, LocalDateTime schedule) {
		Process process = getValueById(processId);
		process.updateSchedule(schedule);
	}

	public Process getValueById(Long id) {
		return getSortedValues()
			.stream()
			.filter(process -> process.getId().equals(id))
			.findAny()
			.orElseThrow(() -> new GoHigherException(APPLICATION_PROCESS_NOT_FOUND));
	}
}
