package com.example.sales_system.controller.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ErrorResponse {
  private Instant timestamp;
  private String type; // machine-readable error type (URI)
  private String title; // short, human-readable summary
  private int status; // HTTP status
  private String detail; // human-readable explanation
  private String instance; // request path or identifier
  private String traceId; // correlation id
  private Map<String, List<String>> errors; // optional field errors

  public ErrorResponse(
      Instant timestamp,
      String type,
      String title,
      int status,
      String detail,
      String instance,
      String traceId,
      Map<String, List<String>> errors) {
    this.timestamp = timestamp;
    this.type = type;
    this.title = title;
    this.status = status;
    this.detail = detail;
    this.instance = instance;
    this.traceId = traceId;
    this.errors = errors;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public String getType() {
    return type;
  }

  public String getTitle() {
    return title;
  }

  public int getStatus() {
    return status;
  }

  public String getDetail() {
    return detail;
  }

  public String getInstance() {
    return instance;
  }

  public String getTraceId() {
    return traceId;
  }

  public Map<String, List<String>> getErrors() {
    return errors;
  }
}
