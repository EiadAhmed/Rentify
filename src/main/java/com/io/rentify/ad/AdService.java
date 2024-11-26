package com.io.rentify.ad;

import com.io.rentify.updatedUser.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Page<Ad> getAllAds(Pageable pageable) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // Admins see all ads
            return adRepository.findAll(pageable);
        } else {
            // Regular users see only approved ads
            return adRepository.findByApprovalStatus(Ad.ApprovalStatus.APPROVED, pageable);
        }
    }

    public Page<Ad> getAllApprovedAds(Pageable pageable) {
        return adRepository.findByApprovalStatus(Ad.ApprovalStatus.APPROVED, pageable);
    }

    public List<Ad> searchApprovedAds(String title, String location, String category, Float minPrice, Float maxPrice, Ad.Availability availability) {
        List<Ad> ads = adRepository.findAll(); // Adjust logic based on search filters.

        return ads.stream()
                .filter(ad -> ad.getApprovalStatus() == Ad.ApprovalStatus.APPROVED)
                .collect(Collectors.toList());
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

    public Ad approveAd(Long adId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        ad.setApprovalStatus(Ad.ApprovalStatus.APPROVED);
        return adRepository.save(ad);
    }

    public Ad rejectAd(Long adId, String feedback) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        ad.setApprovalStatus(Ad.ApprovalStatus.REJECTED);
        ad.setFeedback(feedback);

        return adRepository.save(ad);
    }

    public List<Ad> getAdsByApprovalStatus(Ad.ApprovalStatus approvalStatus) {
        return adRepository.findByApprovalStatus(approvalStatus);
    }


}
