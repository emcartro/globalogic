package com.globallogic.app.users_app.model;


import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ErrorDetail {
    private Instant timestamp;
    private int codigo;
    private String detail;
    private List<String> messages;

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public List<String> getMessages() { return messages; }
    public void setMessages(List<String> messages) { this.messages = messages; }
}