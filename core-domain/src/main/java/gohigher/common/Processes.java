package gohigher.common;

import static gohigher.common.ProcessType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
		List<Process> adjustedProcesses = addAdditionalProcessIfNecessary(processes);
		Map<ProcessType, List<Process>> values = groupingByProcessType(adjustedProcesses);

		for (ProcessType processType : values.keySet()) {
			assignNewOrder(values.get(processType));
		}

		return new Processes(values);
	}

	private static List<Process> addAdditionalProcessIfNecessary(List<Process> processes) {
		Optional<Process> toApplyProcess = processes.stream()
			.filter(process -> process.isTypeOf(TO_APPLY))
			.findAny();

		Optional<Process> documentProcess = processes.stream()
			.filter(process -> process.isTypeOf(DOCUMENT))
			.findAny();

		List<Process> changedProcesses = new ArrayList<>(processes);
		if (toApplyProcess.isPresent() && documentProcess.isEmpty()) {
			changedProcesses.add(toApplyProcess.get().copyWithSameScheduleAndTypeOf(DOCUMENT));
		} else if (toApplyProcess.isEmpty() && documentProcess.isPresent()) {
			changedProcesses.add(documentProcess.get().copyWithSameScheduleAndTypeOf(TO_APPLY));
		}

		return changedProcesses;
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
}
