package com.io.rentify.ad;

import com.io.rentify.updatedUser.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdService {
    @Autowired
    private AdRepository adRepository;

    public Optional<Ad> findById(Long adId) {
        return adRepository.findById(adId);
    }

    public Ad createAd(Ad ad) {
        return adRepository.save(ad);
    }

    public List<Ad> getAdsByUser(User user) {
        return adRepository.findByUser(user);
    }

    public List<Ad> getAllAds() {
        return adRepository.findAll();
    }

    public Ad updateAvailability(Long adId, Ad.Availability availability) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"));
        ad.setAvailability(availability);
        return adRepository.save(ad);
    }

    public Ad updateAd(Long adId, Ad updatedAd, User user) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"));

        if (!ad.getUser().equals(user)) {
            throw new AccessDeniedException("User not authorized to update this ad");
        }

        ad.setTitle(updatedAd.getTitle());
        ad.setDescription(updatedAd.getDescription());
        ad.setPrice(updatedAd.getPrice());
        ad.setLocation(updatedAd.getLocation());
        ad.setCategory(updatedAd.getCategory());
        ad.setPhotos(updatedAd.getPhotos());
        ad.setAvailability(updatedAd.getAvailability());

        return adRepository.save(ad);
    }

    public void deleteAd(Long adId, User user) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"));

        if (!ad.getUser().equals(user)) {
            throw new AccessDeniedException("User not authorized to delete this ad");
        }

        adRepository.delete(ad);
    }


    public List<Ad> searchAds(String title, String location, String category, Float minPrice, Float maxPrice, Ad.Availability availability) {
        if (title != null && location != null) {
            return adRepository.findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(title, location);
        }

        if (category != null && availability != null) {
            return adRepository.findByCategoryContainingIgnoreCaseAndAvailability(category, availability);
        }

        if (minPrice != null && maxPrice != null) {
            return adRepository.findByPriceBetween(minPrice, maxPrice);
        }

        if (title != null) {
            return adRepository.findByTitleContainingIgnoreCase(title);
        }

        if (location != null) {
            return adRepository.findByLocationContainingIgnoreCase(location);
        }

        if (category != null) {
            return adRepository.findByCategoryContainingIgnoreCase(category);
        }

        if (availability != null) {
            return adRepository.findByAvailability(availability);
        }

        return adRepository.findAll(); // Return all if no filter is applied
    }


    public List<Ad> getSimilarAds(Ad ad) {
        return adRepository.findByCategoryContainingIgnoreCase(ad.getCategory());
    }
}
