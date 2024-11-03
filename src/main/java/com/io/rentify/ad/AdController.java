package com.io.rentify.ad;


import com.io.rentify.chat.ChatMessage;
import com.io.rentify.chat.ChatMessageService;
import com.io.rentify.updatedUser.User;
import com.io.rentify.updatedUser.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

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


    @PostMapping("/create")
    public Ad createAd(@RequestBody Ad ad, @AuthenticationPrincipal UserDetails userDetails) {
        // Get the user from the repository

        User user = myUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ad.setUser(user); // Set the user for the ad
        return adService.createAd(ad);



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
    public ResponseEntity<Ad> updateAd(@PathVariable Long adId, @RequestBody Ad updatedAd, @AuthenticationPrincipal UserDetails userDetails) {
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
    @PostMapping("/{adId}/message")
    public ChatMessage sendMessageToAdPoster(@PathVariable Long adId,
                                             @RequestBody String messageContent,
                                             @AuthenticationPrincipal UserDetails senderDetails) {
        // Find the ad by adId
        Ad ad = adService.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        // Get the sender and recipient (the user who posted the ad)
        User sender = myUserRepository.findByEmail(senderDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Sender not found"));
        User recipient = ad.getUser();

        // Create a new ChatMessage
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId(sender.getId().toString());
        chatMessage.setRecipientId(recipient.getId().toString());
        chatMessage.setContent(messageContent);
        chatMessage.setAdId(adId); // Associate the message with the specific ad

        // Save the message
        return chatMessageService.save(chatMessage);
    }

    @PostMapping("/{adId}/reply")
    public ChatMessage replyToUser(@PathVariable Long adId,
                                   @RequestBody String messageContent,
                                   @AuthenticationPrincipal UserDetails adPosterDetails) {
        // Find the ad by adId
        Ad ad = adService.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        // Get the ad poster (recipient)
        User recipient = ad.getUser();

        // Get the sender (the user who initiated the chat)
        String senderId = chatMessageService.findSenderId(adId, recipient.getId().toString());
        if (senderId == null) {
            throw new RuntimeException("No sender found for this ad");
        }

        // Create a new ChatMessage
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId(recipient.getId().toString());
        chatMessage.setRecipientId(senderId);
        chatMessage.setContent(messageContent);
        chatMessage.setAdId(adId); // Associate the message with the specific ad

        // Save the message
        return chatMessageService.save(chatMessage);
    }





    @GetMapping("/{adId}/message")
    public ModelAndView openChatWithAdPoster(@PathVariable Long adId, @AuthenticationPrincipal UserDetails userDetails) {
        Ad ad = adService.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        User sender = myUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User recipient = ad.getUser();

        ModelAndView modelAndView = new ModelAndView("chat");  // chat.html page
        modelAndView.addObject("senderId", sender.getId());
        modelAndView.addObject("recipientId", recipient.getId());
        modelAndView.addObject("adId", adId);

        return modelAndView;
    }


}