
package com.example.JsonDataset.repository;

import com.example.JsonDataset.model.DatasetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<DatasetRecord, Long> {
    List<DatasetRecord> findByDatasetName(String datasetName);
}
