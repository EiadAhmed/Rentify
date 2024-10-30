package com.io.rentify.Ad;

import com.io.rentify.updatedUser.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdService {
    @Autowired
    private AdRepository adRepository;

    public Ad createAd(Ad ad) {
        return adRepository.save(ad);
    }

    public List<Ad> getAdsByUser(User user) {
        return adRepository.findByUser(user);
    }

    public Ad updateAvailability(Long adId, Ad.Availability availability) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"));
        ad.setAvailability(availability);
        return adRepository.save(ad);
    }
}
