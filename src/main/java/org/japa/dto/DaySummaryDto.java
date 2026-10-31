package org.japa.dto;

import java.util.List;

public class DaySummaryDto {
    private String date; // YYYY-MM-DD
    private int totalChants;
    private int totalMalas;
    private int durationMinutes;
    private List<String> mantrasUsed;

    public DaySummaryDto(String date, int totalChants, int totalMalas, int durationMinutes, List<String> mantrasUsed) {
        this.date = date;
        this.totalChants = totalChants;
        this.totalMalas = totalMalas;
        this.durationMinutes = durationMinutes;
        this.mantrasUsed = mantrasUsed;
    }

    public String getDate() { return date; }
    public int getTotalChants() { return totalChants; }
    public int getTotalMalas() { return totalMalas; }
    public int getDurationMinutes() { return durationMinutes; }
    public List<String> getMantrasUsed() { return mantrasUsed; }
}
