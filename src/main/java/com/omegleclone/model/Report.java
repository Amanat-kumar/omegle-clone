package com.omegleclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reported_by_id", nullable = false)
    private AppUser reportedBy;

    @ManyToOne
    @JoinColumn(name = "reported_user_id", nullable = false)
    private AppUser reportedUser;

    @Column(nullable = false)
    private String reason;

    @Column(name = "report_time", updatable = false)
    private LocalDateTime reportTime = LocalDateTime.now();
}
