package com.omegleclone.service;

import com.omegleclone.model.Report;
import com.omegleclone.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    // Submit a report about inappropriate behavior
    public Report submitReport(Report report) {
        return reportRepository.save(report);
    }

    // Get a report by ID
    public Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }
}
