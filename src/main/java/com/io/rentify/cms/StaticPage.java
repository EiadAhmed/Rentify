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

    // Getters and Setters
}
