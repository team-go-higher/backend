package gohigher.application.entity.dto.response;

import java.time.LocalDateTime;

import gohigher.common.ProcessType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProcessWithApplicationResponse {

    private long applicationId;
    private String companyName;
    private String position;
    private String specificPosition;
    private long processId;
    private ProcessType type;
    private String description;
    private LocalDateTime schedule;
}
