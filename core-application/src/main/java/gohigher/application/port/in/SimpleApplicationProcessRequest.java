package gohigher.application.port.in;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import gohigher.common.ProcessType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleApplicationProcessRequest {

	private String type;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime schedule;

	public ProcessType getType() {
		return ProcessType.from(type);
	}
}
