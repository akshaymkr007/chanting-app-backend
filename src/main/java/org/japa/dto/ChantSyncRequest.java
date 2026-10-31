package org.japa.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ChantSyncRequest {

    private String userId;

    @NotNull
    private List<ChantRecordDto> records;

    public ChantSyncRequest() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<ChantRecordDto> getRecords() { return records; }
    public void setRecords(List<ChantRecordDto> records) { this.records = records; }
}
