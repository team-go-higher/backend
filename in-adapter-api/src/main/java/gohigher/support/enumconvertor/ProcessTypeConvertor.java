package gohigher.support.enumconvertor;

import org.springframework.core.convert.converter.Converter;

import gohigher.common.ProcessType;

public class ProcessTypeConvertor implements Converter<String, ProcessType> {

	@Override
	public ProcessType convert(String value) {
			return ProcessType.from(value);
	}
}
