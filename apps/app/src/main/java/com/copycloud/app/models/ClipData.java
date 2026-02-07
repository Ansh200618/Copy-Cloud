package com.copycloud.app.models;

public class ClipData {
    private String code;
    private String content;
    private String type;
    private String created_at;

    public ClipData() {
    }

    public ClipData(String code, String content, String type) {
        this.code = code;
        this.content = content;
        this.type = type;
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
}
