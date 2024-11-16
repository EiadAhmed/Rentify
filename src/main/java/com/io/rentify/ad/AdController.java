package com.io.rentify.ad;


import com.io.rentify.chat.ChatMessage;
import com.io.rentify.chat.ChatMessageService;
import com.io.rentify.updatedUser.User;
import com.io.rentify.updatedUser.MyUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import java.util.List;

@RestController
@RequestMapping("/ads")
public class AdController {

    @Autowired
    private AdService adService;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ChatMessageService chatMessageService;


    //    @PostMapping("/create")
//    public Ad createAd(@RequestBody Ad ad) {
//        return adService.createAd(ad);
//    }


    @PostMapping
    public Ad createAd(@Valid @RequestBody Ad ad, @AuthenticationPrincipal UserDetails userDetails) {
        // Get the user from the repository

        User user = myUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ad.setUser(user); // Set the user for the ad
        return adService.createAd(ad);



    }
    @GetMapping
    public ResponseEntity<Page<Ad>> getAllAds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

//        // Parsing sort parameters
//        String sortField = sort[0];
//        String sortDirection = sort.length > 1 ? sort[1] : "asc";
//        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size);

        Page<Ad> ads = adService.getAllAds(pageable);
        return ResponseEntity.ok(ads);
    }

    private User user;

    private void checkUser(@PathVariable Long adId, @AuthenticationPrincipal UserDetails userDetails) {
        user = myUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Ad ad = adService.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad not found"));
        if (!ad.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this ad");
        }
    }

    @PutMapping("/{adId}")
    public ResponseEntity<Ad> updateAd(@PathVariable Long adId,@Valid @RequestBody Ad updatedAd, @AuthenticationPrincipal UserDetails userDetails) {
        checkUser(adId, userDetails);

        return ResponseEntity.ok(adService.updateAd(adId, updatedAd, user));
    }
    @DeleteMapping("/{adId}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long adId, @AuthenticationPrincipal UserDetails userDetails) {
        checkUser(adId, userDetails);
        adService.deleteAd(adId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{adId}")
    public ResponseEntity<Ad> getAdById(@PathVariable Long adId) {
        Ad ad = adService.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"));
        return ResponseEntity.ok(ad);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ad>> getAdsByUserId(@PathVariable Long userId) {
        User user = myUserRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Ad> ads = adService.getAdsByUser(user);
        return ResponseEntity.ok(ads);
    }
//    @GetMapping("/user/{userId}")
//    public List<Ad> getAdsByUser(@PathVariable Long userId) {
//        // Add method to retrieve user and pass to adService
//    }

    @PutMapping("/{adId}/availability")
    public Ad updateAvailability(@PathVariable Long adId, @RequestParam Ad.Availability availability) {
        return adService.updateAvailability(adId, availability);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Ad>> searchAds(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) Ad.Availability availability) {

        List<Ad> ads = adService.searchAds(title, location, category, minPrice, maxPrice, availability);

        return ResponseEntity.ok(ads);
    }

    @GetMapping("/{adId}/similar")
    public ResponseEntity<List<Ad>> getSimilarAds(@PathVariable Long adId) {
        Ad ad = adService.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found"));
        List<Ad> similarAds = adService.getSimilarAds(ad);
        return ResponseEntity.ok(similarAds);
    }











}