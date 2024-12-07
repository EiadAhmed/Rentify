package com.io.rentify.cms;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class StaticPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // Page Title
    @Column(columnDefinition = "TEXT")
    private String content; // Page Content

    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    // Getters and Setters
}
