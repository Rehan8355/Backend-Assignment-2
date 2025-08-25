
package com.example.JsonDataset.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;

public class InsertRecordRequest {
    @NotNull(message = "record is required")
    private JsonNode record;

    public JsonNode getRecord() { return record; }
    public void setRecord(JsonNode record) { this.record = record; }
}
