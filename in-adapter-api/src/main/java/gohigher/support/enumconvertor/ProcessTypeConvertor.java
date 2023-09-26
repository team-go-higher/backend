package gohigher.support.enumconvertor;

import org.springframework.core.convert.converter.Converter;

import gohigher.application.ApplicationErrorCode;
import gohigher.common.ProcessType;
import gohigher.global.exception.GoHigherException;

public class ProcessTypeConvertor implements Converter<String, ProcessType> {

	@Override
	public ProcessType convert(String value) {
		if (value.isBlank()) {
			throw new GoHigherException(ApplicationErrorCode.APPLICATION_PROCESS_TYPE_NULL);
		}
		return ProcessType.from(value);
	}
}
