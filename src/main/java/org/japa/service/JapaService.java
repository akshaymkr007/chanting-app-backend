package org.japa.service;

import org.japa.dto.*;
import org.japa.entity.ChantRecordEntity;
import org.japa.repository.ChantRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JapaService {

    private final ChantRecordRepository chantRecordRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public JapaService(ChantRecordRepository chantRecordRepository) {
        this.chantRecordRepository = chantRecordRepository;
    }

    @Transactional
    public int batchSync(String userId, List<ChantRecordDto> records) {
        if (records == null || records.isEmpty()) return 0;
        int count = 0;

        for (ChantRecordDto dto : records) {
            String recordUserId = (userId != null && !userId.isBlank()) ? userId : dto.getUserId();
            if (recordUserId == null || recordUserId.isBlank()) {
                recordUserId = "usr_guest";
            }

            LocalDate date = LocalDate.parse(dto.getDate(), DATE_FORMATTER);
            Optional<ChantRecordEntity> existing = chantRecordRepository.findByUserIdAndRecordDateAndMantraId(
                    recordUserId, date, dto.getMantraId());

            ChantRecordEntity entity;
            if (existing.isPresent()) {
                entity = existing.get();
                // Take maximum of local count vs DB count to prevent data loss on multiple devices
                if (dto.getCount() > entity.getCount()) {
                    entity.setCount(dto.getCount());
                    entity.setDurationSeconds(dto.getDurationSeconds());
                }
                if (dto.getEncryptedNote() != null) {
                    entity.setEncryptedNote(dto.getEncryptedNote());
                }
                entity.setTimestampMs(dto.getTimestamp() != null ? dto.getTimestamp() : System.currentTimeMillis());
            } else {
                entity = new ChantRecordEntity();
                entity.setId((dto.getId() != null && !dto.getId().isBlank()) ? dto.getId() : "rec_" + UUID.randomUUID().toString());
                entity.setUserId(recordUserId);
                entity.setRecordDate(date);
                entity.setMantraId(dto.getMantraId());
                entity.setMantraName(dto.getMantraName() != null ? dto.getMantraName() : "Sacred Mantra");
                entity.setCount(dto.getCount());
                entity.setDurationSeconds(dto.getDurationSeconds());
                entity.setEncryptedNote(dto.getEncryptedNote());
                entity.setTimestampMs(dto.getTimestamp() != null ? dto.getTimestamp() : System.currentTimeMillis());
            }

            chantRecordRepository.save(entity);
            count++;
        }

        return count;
    }

    @Transactional
    public ChantRecordDto increment(ChantIncrementRequest request) {
        String userId = (request.getUserId() != null && !request.getUserId().isBlank()) ? request.getUserId() : "usr_guest";
        LocalDate date = (request.getDate() != null && !request.getDate().isBlank())
                ? LocalDate.parse(request.getDate(), DATE_FORMATTER)
                : LocalDate.now();

        Optional<ChantRecordEntity> existing = chantRecordRepository.findByUserIdAndRecordDateAndMantraId(
                userId, date, request.getMantraId());

        ChantRecordEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            entity.setCount(entity.getCount() + request.getCount());
            entity.setDurationSeconds(entity.getDurationSeconds() + request.getDurationSeconds());
            entity.setTimestampMs(System.currentTimeMillis());
            if (request.getEncryptedNote() != null) {
                entity.setEncryptedNote(request.getEncryptedNote());
            }
        } else {
            entity = new ChantRecordEntity();
            entity.setId("rec_" + UUID.randomUUID().toString().replace("-", ""));
            entity.setUserId(userId);
            entity.setRecordDate(date);
            entity.setMantraId(request.getMantraId());
            entity.setMantraName((request.getMantraName() != null) ? request.getMantraName() : "Sacred Mantra");
            entity.setCount(request.getCount());
            entity.setDurationSeconds(request.getDurationSeconds());
            entity.setEncryptedNote(request.getEncryptedNote());
            entity.setTimestampMs(System.currentTimeMillis());
        }

        ChantRecordEntity saved = chantRecordRepository.save(entity);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ChantRecordDto> getHistory(String userId, int days) {
        String uid = (userId != null && !userId.isBlank()) ? userId : "usr_guest";
        LocalDate fromDate = LocalDate.now().minusDays(days);
        List<ChantRecordEntity> entities = chantRecordRepository.findByUserIdAndRecordDateGreaterThanEqualOrderByRecordDateAsc(uid, fromDate);
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DaySummaryDto> getDailySummaries(String userId, int days) {
        String uid = (userId != null && !userId.isBlank()) ? userId : "usr_guest";
        LocalDate fromDate = LocalDate.now().minusDays(days - 1);
        List<ChantRecordEntity> records = chantRecordRepository.findByUserIdAndRecordDateGreaterThanEqualOrderByRecordDateAsc(uid, fromDate);

        Map<LocalDate, List<ChantRecordEntity>> grouped = records.stream()
                .collect(Collectors.groupingBy(ChantRecordEntity::getRecordDate));

        List<DaySummaryDto> summaries = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            List<ChantRecordEntity> dayRecords = grouped.getOrDefault(d, Collections.emptyList());

            int totalChants = dayRecords.stream().mapToInt(ChantRecordEntity::getCount).sum();
            int totalMalas = totalChants / 108;
            int totalSeconds = dayRecords.stream().mapToInt(ChantRecordEntity::getDurationSeconds).sum();
            List<String> mantras = dayRecords.stream().map(ChantRecordEntity::getMantraName).distinct().collect(Collectors.toList());

            summaries.add(new DaySummaryDto(
                    d.format(DATE_FORMATTER),
                    totalChants,
                    totalMalas,
                    Math.round((float) totalSeconds / 60),
                    mantras
            ));
        }

        return summaries;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> calculateStreakAndTotals(String userId) {
        String uid = (userId != null && !userId.isBlank()) ? userId : "usr_guest";
        List<LocalDate> dates = chantRecordRepository.findDistinctChantingDatesByUserId(uid);
        long totalChants = chantRecordRepository.getTotalChantsByUserId(uid);

        int currentStreak = 0;
        int longestStreak = 0;

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        Set<LocalDate> dateSet = new HashSet<>(dates);

        if (dateSet.contains(today) || dateSet.contains(yesterday)) {
            LocalDate checkDate = dateSet.contains(today) ? today : yesterday;
            while (dateSet.contains(checkDate)) {
                currentStreak++;
                checkDate = checkDate.minusDays(1);
            }
        }

        // Longest streak calculation
        int tempStreak = 0;
        LocalDate prev = null;
        for (LocalDate d : dates) {
            if (prev == null) {
                tempStreak = 1;
            } else if (d.equals(prev.plusDays(1))) {
                tempStreak++;
            } else if (!d.equals(prev)) {
                tempStreak = 1;
            }
            if (tempStreak > longestStreak) {
                longestStreak = tempStreak;
            }
            prev = d;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("currentStreak", currentStreak);
        response.put("longestStreak", Math.max(longestStreak, currentStreak));
        response.put("totalLifetimeChants", totalChants);
        response.put("totalLifetimeMalas", totalChants / 108);
        return response;
    }

    private ChantRecordDto toDto(ChantRecordEntity entity) {
        ChantRecordDto dto = new ChantRecordDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setDate(entity.getRecordDate().format(DATE_FORMATTER));
        dto.setMantraId(entity.getMantraId());
        dto.setMantraName(entity.getMantraName());
        dto.setCount(entity.getCount());
        dto.setMalas(entity.getMalas());
        dto.setDurationSeconds(entity.getDurationSeconds());
        dto.setEncryptedNote(entity.getEncryptedNote());
        dto.setTimestamp(entity.getTimestampMs());
        return dto;
    }
}
