package com.copycloud.app.models;

public class HistoryItem {
    private long id;
    private String code;
    private String content;
    private String type;
    private long timestamp;
    private String deviceCode;

    public HistoryItem() {
    }

    public HistoryItem(String code, String content, String type, String deviceCode) {
        this.code = code;
        this.content = content;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
        this.deviceCode = deviceCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getTimeAgo() {
        long diff = System.currentTimeMillis() - timestamp;
        long minutes = diff / (60 * 1000);
        long hours = diff / (60 * 60 * 1000);
        
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + "m ago";
        if (hours < 24) return hours + "h ago";
        return "1d ago";
    }
}
