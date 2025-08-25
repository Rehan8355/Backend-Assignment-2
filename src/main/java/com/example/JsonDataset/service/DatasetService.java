package com.example.JsonDataset.service;

import com.example.JsonDataset.exception.BadRequestException;
import com.example.JsonDataset.model.DatasetRecord;
import com.example.JsonDataset.repository.ReportRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.JsonDataset.exception.NotFoundException;
import java.util.*;
@Service
public class DatasetService {

    private final ReportRepository datasetRepository;
    private final ObjectMapper objectMapper;

    public DatasetService(ReportRepository datasetRepository, ObjectMapper objectMapper) {
        this.datasetRepository = datasetRepository;
        this.objectMapper = objectMapper;
    }

    // === INSERT RECORD ===
    @Transactional
    public String insert(String datasetName, JsonNode record) {
        if (record == null || !record.isObject()) {
            throw new BadRequestException("Record must be a valid JSON object");
        }
        DatasetRecord entity = new DatasetRecord();
        entity.setDatasetName(datasetName);
        entity.setRecord(record.toString()); // store JSON as string
        datasetRepository.save(entity);
        return entity.getId().toString();
    }

    // === FIND ALL ===
    public List<JsonNode> findAll(String datasetName) {
        List<DatasetRecord> records = datasetRepository.findByDatasetName(datasetName);
        if (records.isEmpty()) {
            throw new NotFoundException("Dataset not found: " + datasetName);
        }
        return records.stream()
                .map(DatasetRecord::getRecordAsJson)
                .toList();
    }

    // === GROUP BY ===
    public Map<String, List<JsonNode>> groupBy(String datasetName, String field) {
        List<JsonNode> records = findAll(datasetName);
        return records.stream()
                .collect(Collectors.groupingBy(r -> {
                    JsonNode value = r.get(field);
                    return value != null ? value.asText() : "null";
                }));
    }

    // === SORT BY ===
    public List<JsonNode> sortBy(String datasetName, String field, String order) {
        List<JsonNode> records = findAll(datasetName);

        Comparator<JsonNode> comparator = Comparator.comparing(
                r -> Optional.ofNullable(r.get(field)).map(JsonNode::asText).orElse("")
        );

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return records.stream()
                .sorted(comparator)
                .toList();
    }

    // === GROUP + SORT ===
    public Map<String, List<JsonNode>> groupAndSort(String datasetName, String groupBy, String sortBy, String order) {
        Map<String, List<JsonNode>> grouped = groupBy(datasetName, groupBy);

        return grouped.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .sorted(getComparator(sortBy, order))
                                .toList()
                ));
    }

    private Comparator<JsonNode> getComparator(String field, String order) {
        Comparator<JsonNode> comparator = Comparator.comparing(
                r -> Optional.ofNullable(r.get(field)).map(JsonNode::asText).orElse("")
        );

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}





