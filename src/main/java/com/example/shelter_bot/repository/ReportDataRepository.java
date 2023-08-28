package com.example.shelter_bot.repository;

import com.example.shelter_bot.entity.ReportData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDataRepository extends JpaRepository<ReportData, Long> {
    ReportData findByChatId(Long id);
}