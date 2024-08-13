package com.example.recommendation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;

@ComponentScan
@Configuration
public class ConfigService {

    public <K, V, T> T getValue(Map<String, String> data, String key, String defaultValue, Map<String, DeltaDays> lookup, Function<V, T> mapper) {
        V returnValue = (V) data.get(key);

        if (returnValue == null || returnValue.toString().isEmpty()) {
            returnValue = (V) defaultValue;
        }

        if (lookup != null && lookup.containsKey(returnValue)) {
            returnValue = (V) lookup.get(returnValue);
        }

        if (mapper != null) {
            return mapper.apply(returnValue);
        }

        return (T) returnValue;
    }

    public String ftpFilePrefix(String namespace) {
        String[] tokens = namespace.split("\\.");
        tokens[tokens.length - 1] = "ftp";
        return String.join(".", tokens);
    }

    public boolean stringToBool(String string) {
        if (string.equalsIgnoreCase("true")) {
            return true;
        } else if (string.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new IllegalArgumentException("String " + string + " is neither true nor false");
        }
    }

    public Map.Entry<String, Map<String, Object>> configFromDict(Map<String, String> dict) {
        String namespace = dict.get("Namespace");

        Map<String, Object> dagConfig = Map.of(
                "earliest_available_delta_days", 0,
                "lif_encoding", "json",
                "earliest_available_time", getValue(dict, "Available Start Time", "07:00", null, null),
                "latest_available_time", getValue(dict, "Available End Time", "08:00", null, null),
                "require_schema_match", getValue(dict, "Requires Schema Match", "True", null, this::stringToBool),
                "schedule_interval", getValue(dict, "Schedule", "1 7 * * *", null, null),
                "delta_days", getValue(dict, "Delta Days", "DAY_BEFORE", DeltaDays.lookup(), null),
                "ftp_file_wildcard", getValue(dict, "File Naming Pattern", null, null, null),
                "ftp_file_prefix", getValue(dict, "FTP File Prefix", ftpFilePrefix(namespace), null, null),
                "namespace", namespace
        );

        return Map.entry(dict.get("Airflow DAG"), dagConfig);
    }
}
