package com.io.rentify.cms;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

public class StaticPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // Page Title
    @Column(columnDefinition = "TEXT")
    private String content; // Page Content

    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    // Getters and Setters
}
