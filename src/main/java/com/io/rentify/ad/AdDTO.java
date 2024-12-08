package com.io.rentify.ad;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class AdDTO {
    private Long adId;
    private String title;
    private String description;
    private float price;
    private String location;
    private List<String> photos;
    private Ad.Availability availability;
    private String category;
    public enum Availability {
        AVAILABLE, BOOKED
    }

    private Long userId;  // Only include userId

    public Long getAdId() {
        return adId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public Ad.Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Ad.Availability availability) {
        this.availability = availability;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
