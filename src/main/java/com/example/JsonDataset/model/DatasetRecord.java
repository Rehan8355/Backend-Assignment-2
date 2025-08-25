package com.example.JsonDataset.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import java.io.IOException;

@Entity
@Table(name = "dataset_records")
public class DatasetRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment Long
    private Long id;

    @Column(nullable = false)
    private String datasetName;

    @Lob
    @Column(columnDefinition = "TEXT") // store JSON as string
    private String record;

    // === Getters & Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDatasetName() { return datasetName; }
    public void setDatasetName(String datasetName) { this.datasetName = datasetName; }

    public String getRecord() { return record; }
    public void setRecord(String record) { this.record = record; }

//    // Convert String -> JSON
    public JsonNode getRecordAsJson() {
        try {
            return new ObjectMapper().readTree(this.record);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON in record", e);
        }
    }

    // Save directly from JsonNode
    public void setRecordFromJson(JsonNode jsonNode) {
        try {
            this.record = new ObjectMapper().writeValueAsString(jsonNode);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JsonNode to String", e);
        }
    }


}
