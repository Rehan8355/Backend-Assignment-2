
package com.example.JsonDataset.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;

public class QueryResponse {
    private Map<String, List<JsonNode>> groupedRecords;
    private List<JsonNode> sortedRecords;
    private List<JsonNode> records; // when neither groupBy nor sortBy is provided
    private String dataset;
    private String operation; // "groupBy", "sortBy", or "all"

    public static QueryResponse grouped(String dataset, Map<String, List<JsonNode>> map) {
        QueryResponse r = new QueryResponse();
        r.dataset = dataset;
        r.operation = "groupBy";
        r.groupedRecords = map;
        return r;
    }
    public static QueryResponse sorted(String dataset, List<JsonNode> list) {
        QueryResponse r = new QueryResponse();
        r.dataset = dataset;
        r.operation = "sortBy";
        r.sortedRecords = list;
        return r;
    }
    public static QueryResponse all(String dataset, List<JsonNode> list) {
        QueryResponse r = new QueryResponse();
        r.dataset = dataset;
        r.operation = "all";
        r.records = list;
        return r;
    }

    public Map<String, List<JsonNode>> getGroupedRecords() { return groupedRecords; }
    public List<JsonNode> getSortedRecords() { return sortedRecords; }
    public List<JsonNode> getRecords() { return records; }
    public String getDataset() { return dataset; }
    public String getOperation() { return operation; }
}
