package gohigher.common;

import static gohigher.common.ProcessType.*;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Processes {

	public static final Process DEFAULT_PROCESS = new Process(ProcessType.TO_APPLY, "", null, 1);

	private final List<Process> values;

	public static Processes initialFrom(List<Process> processes) {
		if (processes.isEmpty()) {
			processes.add(DEFAULT_PROCESS);
		}

		assignNewOrder(processes);

		return new Processes(processes);
	}

	public static Processes initialFrom(Process process) {
		List<Process> processes = addAdditionalProcessIfNecessary(process);
		assignNewOrder(processes);
		return new Processes(processes);
	}

	private static List<Process> addAdditionalProcessIfNecessary(Process process) {
		if (process.isTypeOf(TO_APPLY)) {
			return List.of(process, process.copyWithSameScheduleAndTypeOf(DOCUMENT));
		} else if (process.isTypeOf(DOCUMENT)) {
			return List.of(process.copyWithSameScheduleAndTypeOf(TO_APPLY), process);
		}
		return List.of(process);
	}

	private static void assignNewOrder(List<Process> processes) {
		int nextOrder = 1;
		for (Process process : processes) {
			process.assignOrder(nextOrder);
			nextOrder++;
		}
	}
}
