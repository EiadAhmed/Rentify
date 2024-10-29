package com.io.rentify.Ad;


import com.io.rentify.updatedUser.User;
import com.io.rentify.updatedUser.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ads")
public class AdController {

    @Autowired
    private AdService adService;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    //    @PostMapping("/create")
//    public Ad createAd(@RequestBody Ad ad) {
//        return adService.createAd(ad);
//    }


    @PostMapping("/create")
    public Ad createAd(@RequestBody Ad ad, @AuthenticationPrincipal UserDetails userDetails) {
        // Get the user from the repository

        User user = myUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ad.setUser(user); // Set the user for the ad
        return adService.createAd(ad);



    }

//    @GetMapping("/user/{userId}")
//    public List<Ad> getAdsByUser(@PathVariable Long userId) {
//        // Add method to retrieve user and pass to adService
//    }

    @PutMapping("/{adId}/availability")
    public Ad updateAvailability(@PathVariable Long adId, @RequestParam Ad.Availability availability) {
        return adService.updateAvailability(adId, availability);
    }


}