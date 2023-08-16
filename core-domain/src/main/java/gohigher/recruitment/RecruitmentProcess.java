package gohigher.recruitment;

import java.time.LocalDateTime;

public record RecruitmentProcess(
        ProcessType type,
        String description,
        LocalDateTime schedule
) {
}
