package gohigher.position.port.in;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DesiredPositionEmptyValidator.class)
public @interface DesiredPositionBlank {

	String message() default "GLOBAL_011||빈 입력값입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String existedPositionIds();

	String personalPositions();
}
