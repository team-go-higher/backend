package gohigher.common;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Processes {

	public static final Process DEFAULT_PROCESS = new Process(ProcessType.TO_APPLY, "", null, 0);

	private final List<Process> values;

	public static Processes initiallyFrom(List<Process> processes) {
		if (processes.isEmpty()) {
			processes.add(DEFAULT_PROCESS);
		}

		assignNewOrder(processes);

		return new Processes(processes);
	}

	private static void assignNewOrder(List<Process> processes) {
		int nextOrder = 0;
		for (Process process : processes) {
			process.assignOrder(nextOrder);
			nextOrder++;
		}
	}
}
