package gohigher.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class MaskingPatternLayout extends PatternLayout {

	private static final String MASKED_STRING = "********";

	private Pattern multilinePattern;
	private final List<String> maskPatterns = new ArrayList<>();

	public void addMaskPattern(String maskPattern) {
		maskPatterns.add(maskPattern);
		multilinePattern = Pattern.compile(String.join("|", maskPatterns), Pattern.CASE_INSENSITIVE);
	}

	@Override
	public String doLayout(ILoggingEvent event) {
		return maskMessage(super.doLayout(event));
	}

	private String maskMessage(String message) {
		if (multilinePattern == null) {
			return message;
		}

		StringBuilder stringBuilder = new StringBuilder(message);
		Matcher matcher = multilinePattern.matcher(stringBuilder);
		while (matcher.find()) {
			maskIfMatch(stringBuilder, matcher);
		}

		return stringBuilder.toString();
	}

	private void maskIfMatch(StringBuilder stringBuilder, Matcher matcher) {
		IntStream.rangeClosed(1, matcher.groupCount())
			.filter(it -> !Objects.isNull(matcher.group(it)))
			.forEach(it -> stringBuilder.replace(matcher.start(it), matcher.end(it), MASKED_STRING));
	}
}
