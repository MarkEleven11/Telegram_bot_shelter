package com.example.shelter_bot.service;
import com.example.shelter_bot.entity.ReportData;
import com.example.shelter_bot.exceptions.ReportDataNotFoundException;
import com.example.shelter_bot.repository.ReportDataRepository;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
/**
 * Класс сервис отчетов
 **/
@Service
@Transactional
public class ReportDataService {
    private final ReportDataRepository repository;

    public ReportDataService(ReportDataRepository reportRepository) {
        this.repository = reportRepository;
    }

    public void uploadReportData(Long chatId,String name, byte[] pictureFile,
                                 String ration, String health, String behaviour,
                                 Date lastMessage) throws IOException {
        ReportData report = new ReportData();
        report.setChatId(chatId);
        report.setName(name);
        report.setData(pictureFile);
        report.setRation(ration);
        report.setHealth(health);
        report.setBehaviour(behaviour);
        report.setLastMessage(lastMessage);
        this.repository.save(report);
    }

    public ReportData findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(ReportDataNotFoundException::new);
    }

    public ReportData findByChatId(Long chatId) {
        return this.repository.findByChatId(chatId);
    }

    public ReportData save(ReportData report) {
        return this.repository.save(report);
    }

    public void remove(Long id) {
        this.repository.deleteById(id);
    }

    public List<ReportData> getAll() {
        return this.repository.findAll();
    }

}