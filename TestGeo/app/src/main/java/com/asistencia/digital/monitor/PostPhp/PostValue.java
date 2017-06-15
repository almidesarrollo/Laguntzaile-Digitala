package com.asistencia.digital.monitor.PostPhp;

public class PostValue {

    public PostValue(){}

    public PostValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String value;
}
