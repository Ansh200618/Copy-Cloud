package com.copycloud.app.models;

public class ClipData {
    private String code;
    private String content;
    private String type;
    private String created_at;
    private String target_device;

    public ClipData() {
    }

    public ClipData(String code, String content, String type) {
        this.code = code;
        this.content = content;
        this.type = type;
    }

    public ClipData(String code, String content, String type, String target_device) {
        this.code = code;
        this.content = content;
        this.type = type;
        this.target_device = target_device;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTarget_device() {
        return target_device;
    }

    public void setTarget_device(String target_device) {
        this.target_device = target_device;
    }
}
