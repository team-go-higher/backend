package gohigher.application.search;

import java.util.Arrays;

import gohigher.application.ApplicationErrorCode;
import gohigher.global.exception.GoHigherException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationSortingType {

    CREATED(null),
    SCHEDULED("scheduled"),
    RE_SCHEDULED("re-scheduled"),
    CLOSING("closing"),
    ;

    private final String code;

    public static ApplicationSortingType from(String sort) {
        if (sort == null) {
            return CREATED;
        }
        return Arrays.stream(values())
            .filter(type -> type.code.equals(sort))
            .findFirst()
            .orElseThrow(() -> new GoHigherException(ApplicationErrorCode.INVALID_APPLICATION_SORTING_TYPE));
    }
}
