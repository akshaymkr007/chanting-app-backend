package org.japa.repository;

import org.japa.entity.ChantRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChantRecordRepository extends JpaRepository<ChantRecordEntity, String> {

    List<ChantRecordEntity> findByUserIdOrderByRecordDateDesc(String userId);

    List<ChantRecordEntity> findByUserIdAndRecordDateGreaterThanEqualOrderByRecordDateAsc(String userId, LocalDate fromDate);

    Optional<ChantRecordEntity> findByUserIdAndRecordDateAndMantraId(String userId, LocalDate recordDate, String mantraId);

    List<ChantRecordEntity> findByUserIdAndRecordDate(String userId, LocalDate recordDate);

    @Query("SELECT coalesce(sum(c.count), 0) FROM ChantRecordEntity c WHERE c.userId = :userId")
    long getTotalChantsByUserId(@Param("userId") String userId);

    @Query("SELECT DISTINCT c.recordDate FROM ChantRecordEntity c WHERE c.userId = :userId AND c.count > 0 ORDER BY c.recordDate ASC")
    List<LocalDate> findDistinctChantingDatesByUserId(@Param("userId") String userId);
}
