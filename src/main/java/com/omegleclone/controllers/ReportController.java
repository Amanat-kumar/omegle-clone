package com.omegleclone.controllers;

import com.omegleclone.model.Report;
import com.omegleclone.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Submit a report about inappropriate behavior
    @PostMapping("/submit")
    public Report submitReport(@RequestBody Report report) {
        return reportService.submitReport(report);
    }

    // Get a report by ID
    @GetMapping("/{id}")
    public Report getReport(@PathVariable Long id) {
        return reportService.getReportById(id);
    }
}
