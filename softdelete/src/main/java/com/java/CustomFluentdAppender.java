package com.java;

import ch.qos.logback.classic.pattern.CallerDataConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.more.appenders.DataFluentAppender;
import ch.qos.logback.more.appenders.marker.MapMarker;
import org.slf4j.Marker;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomFluentdAppender<E> extends DataFluentAppender<E> {
    private static final String DATA_MESSAGE = "message";
    private static final String DATA_LOGGER = "logger";
    private static final String DATA_THREAD = "thread";
    private static final String DATA_LEVEL = "level";
    private static final String DATA_MARKER = "marker";
    private static final String DATA_CALLER = "caller";
    private static final String DATA_THROWABLE = "throwable";
    private static final String DATA_PID = "pid";
    private static final String DATA_TIMESTAMP = "timestamp";

    private Encoder<E> encoder;
    protected Map<String, String> additionalFields;
    private boolean flattenMapMarker;
    private String markerPrefix = DATA_MARKER;
    private String messageFieldKeyName = DATA_MESSAGE;
    private List<String> ignoredFields;


    @Override
    protected Map<String, Object> createData(E event) {
        Map<String, Object> data = new LinkedHashMap<>();

        if (event instanceof ILoggingEvent) {
            ILoggingEvent loggingEvent = (ILoggingEvent) event;

            // 타임스탬프 추가
            data.put(DATA_TIMESTAMP, formatTimestamp(Instant.now()));  // 예시로 ISO 8601 형식으로 현재 시간 추가
            data.put(DATA_LEVEL, loggingEvent.getLevel().levelStr);
            // PID 추가
            data.put(DATA_PID, getProcessId());
            data.put(DATA_THREAD, loggingEvent.getThreadName());
            data.put(DATA_LOGGER, loggingEvent.getLoggerName());
            data.put(messageFieldKeyName, encoder != null ? encoder.encode(event) : loggingEvent.getFormattedMessage());


            // 기존 로그 데이터 추가


            // 나머지 데이터 처리
            Marker marker = loggingEvent.getMarker();
            if (marker != null) {
                if (marker instanceof MapMarker) {
                    extractMapMarker((MapMarker) marker, data);
                } else {
                    data.put(markerName(), marker.toString());
                    if (marker.hasReferences()) {
                        for (Iterator<Marker> iter = marker.iterator(); iter.hasNext(); ) {
                            Marker nestedMarker = iter.next();
                            if (nestedMarker instanceof MapMarker) {
                                extractMapMarker((MapMarker) nestedMarker, data);
                            }
                        }
                    }
                }
            }

            if (loggingEvent.hasCallerData()) {
                data.put(DATA_CALLER, new CallerDataConverter().convert(loggingEvent));
            }
            if (loggingEvent.getThrowableProxy() != null) {
                data.put(DATA_THROWABLE, ThrowableProxyUtil.asString(loggingEvent.getThrowableProxy()));
            }
            for (Map.Entry<String, String> entry : loggingEvent.getMDCPropertyMap().entrySet()) {
                data.put(entry.getKey(), entry.getValue());
            }
        } else {
            data.put(messageFieldKeyName, encoder != null ? encoder.encode(event) : event.toString());
        }

        if (additionalFields != null) {
            data.putAll(additionalFields);
        }

        if (ignoredFields != null) {
            ignoredFields.stream().forEach(data::remove);
        }

        return data;
    }

    // PID를 가져오는 메서드
    private String getProcessId() {
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        return pid.split("@")[0];  // PID만 추출
    }

    // 시간을 가져오는 메서드
    private String formatTimestamp(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("Asia/Seoul"));  // 예시로 서울 시간대 사용

        return formatter.format(instant);
    }
}