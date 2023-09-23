package gohigher.position.port.in;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.util.CollectionUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DesiredPositionEmptyValidator implements ConstraintValidator<DesiredPositionBlank, Object> {

	private String message;
	private String existedPositionIds;
	private String personalPositions;

	@Override
	public void initialize(DesiredPositionBlank constraintAnnotation) {
		message = constraintAnnotation.message();
		existedPositionIds = constraintAnnotation.existedPositionIds();
		personalPositions = constraintAnnotation.personalPositions();
	}

	@Override
	public boolean isValid(Object o, ConstraintValidatorContext context) {

		List<Object> existedPositionIdsField = getField(o, this.existedPositionIds);
		List<Long> existedPositionIds = existedPositionIdsField == null ? null : existedPositionIdsField.stream()
			.map(obj -> Long.valueOf(String.valueOf(obj)))
			.toList();

		List<Object> personalPositionsField = getField(o, this.personalPositions);
		List<String> personalPositions = personalPositionsField == null ? null : personalPositionsField.stream()
			.map(String::valueOf)
			.toList();

		return isCorrect(context, existedPositionIds, personalPositions);
	}

	private List<Object> getField(Object object, String fieldName) {
		Class<?> clazz = object.getClass();
		Field dateField;
		try {
			dateField = clazz.getDeclaredField(fieldName);
			dateField.setAccessible(true);
			Object target = dateField.get(object);
			if (!((target instanceof List) || target == null)) {
				throw new ClassCastException("casting exception");
			}

			if (target == null) {
				return null;
			}

			return (List<Object>)target;
		} catch (NoSuchFieldException e) {
			System.err.printf("NoSuchFieldException", e);
		} catch (IllegalAccessException e) {
			System.err.printf("IllegalAccessException", e);
		}
		throw new RuntimeException("Not Found Field");
	}

	private boolean isCorrect(ConstraintValidatorContext context, List<Long> existedPositionIds,
		List<String> personalPositions) {
		if (CollectionUtils.isEmpty(existedPositionIds) && CollectionUtils.isEmpty(personalPositions)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message)
				.addConstraintViolation();
			return false;
		}
		return true;
	}
}
