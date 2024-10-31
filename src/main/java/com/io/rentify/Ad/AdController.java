package com.io.rentify.Ad;


import com.io.rentify.chat.ChatMessage;
import com.io.rentify.chat.ChatMessageService;
import com.io.rentify.updatedUser.User;
import com.io.rentify.updatedUser.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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