package gohigher;

import java.time.LocalDateTime;

public record NoticeProcess(
        ProcessType type,
        LocalDateTime schedule
) {
}
