package org.japa.dto;

public class ChantSyncResponse {
    private String status;
    private int syncedRecordsCount;
    private long timestamp;

    public ChantSyncResponse(String status, int syncedRecordsCount, long timestamp) {
        this.status = status;
        this.syncedRecordsCount = syncedRecordsCount;
        this.timestamp = timestamp;
    }

    public String getStatus() { return status; }
    public int getSyncedRecordsCount() { return syncedRecordsCount; }
    public long getTimestamp() { return timestamp; }
}
