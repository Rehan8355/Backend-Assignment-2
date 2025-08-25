package com.example.JsonDataset.controller;

import com.example.JsonDataset.dto.InsertRecordRequest;
import com.example.JsonDataset.exception.BadRequestException;
import com.example.JsonDataset.service.DatasetService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class DatasetController {

    private final DatasetService service;

    public DatasetController(DatasetService service) {
        this.service = service;
    }

    // === Insert a record ===
    @PostMapping(
            path = "/dataset/{datasetName}/record",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Object> insert(
            @PathVariable
            @Pattern(regexp = "^[a-zA-Z0-9_\\-]+$", message = "datasetName must match [a-zA-Z0-9_\\-]+")
            String datasetName,
            @RequestBody InsertRecordRequest request
    ) {
        if (request == null || request.getRecord() == null || !request.getRecord().isObject()) {
            throw new BadRequestException("Body must be { \"record\": { ... } } and record must be a JSON object");
        }

        String recordId = service.insert(datasetName, request.getRecord());

        return Map.of(
                "message", "Record added successfully",
                "dataset", datasetName,
                "recordId", recordId
        );
    }

    // === Query dataset (supports groupBy, sortBy, order) ===
    @GetMapping(
            path = "/dataset/{datasetName}/query",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Object> query(
            @PathVariable
            @Pattern(regexp = "^[a-zA-Z0-9_\\-]+$", message = "datasetName must match [a-zA-Z0-9_\\-]+")
            String datasetName,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        if (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")) {
            throw new BadRequestException("order must be 'asc' or 'desc'");
        }

        if ((groupBy == null || groupBy.isBlank()) && (sortBy == null || sortBy.isBlank())) {
            List<JsonNode> all = service.findAll(datasetName);
            return Map.of("records", all);
        }

        if (groupBy != null && !groupBy.isBlank() && (sortBy == null || sortBy.isBlank())) {
            Map<String, List<JsonNode>> grouped = service.groupBy(datasetName, groupBy);
            return Map.of("groupedRecords", grouped);
        }

        if (groupBy == null || groupBy.isBlank()) {
            List<JsonNode> sorted = service.sortBy(datasetName, sortBy, order);
            return Map.of("sortedRecords", sorted);
        }

        Map<String, List<JsonNode>> groupedSorted = service.groupAndSort(datasetName, groupBy, sortBy, order);
        return Map.of("groupedAndSortedRecords", groupedSorted);
    }
}
