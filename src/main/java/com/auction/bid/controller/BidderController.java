package com.auction.bid.controller;

import com.auction.bid.model.Bidder;
import com.auction.bid.model.Client;
import com.auction.bid.model.Notification;
import com.auction.bid.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bidders")
public class BidderController {

    @Autowired
    private final ClientService clientService;

    @Autowired
    private final NotificationService notificationService;
    @Autowired
    private final BidderService bidderService;
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final BidService bidService;



    public BidderController(BidderService bidderService,ClientService clientService,ItemService itemService, BidService bidService,NotificationService notificationService) {
        this.bidderService = bidderService;
        this.itemService = itemService;
        this.clientService = clientService;
        this.notificationService= notificationService;

        this.bidService = bidService;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("bidder", new Bidder());
        return "bidder/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute Bidder bidder) {
        bidderService.saveBidder(bidder);
        return "redirect:/bidder/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "bidder/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String phone, @RequestParam String password, HttpSession session) {
        Bidder bidder = bidderService.authenticate(phone, password);
        if (bidder != null) {
            session.setAttribute("bidder", bidder);
            return "redirect:/bidder/items";
        }
        return "bidder/login";
    }

    @GetMapping("/items")  // This is fine as is
    public String viewItems(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("items", search != null ? itemService.searchItems(search) : itemService.getAllItems());
        return "bidder/items";
    }

    @PostMapping("/bid/{itemId}")
    public String placeBid(@PathVariable Long itemId, @RequestParam Double bidAmount, HttpSession session) {
        Bidder bidder = (Bidder) session.getAttribute("bidder");
        if (bidder == null) return "redirect:/bidder/login";

        bidService.placeBid(bidder.getId(), itemId, bidAmount);
        return "redirect:/bidder/notifications";
    }

    @PostMapping("/notifications")
    public ResponseEntity<String> createNotification(
            @RequestParam("clientId") Long clientId,
            @RequestParam("message") String message) {
        System.out.println("Received clientId: " + clientId + ", message: " + message);

        // Retrieve client by ID
        if (clientId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client not found");
        }
        // Create the notification
        notificationService.createNotification(message, clientId);
        return ResponseEntity.ok("Notification created successfully");
    }

    @PostMapping("/notifications_view")
    public ResponseEntity<Object> displayNotification() {

        // Retrieve the notifications for the given client ID
        List<Notification> notifications = notificationService.getNotifications();

        // Return the notifications in the response body
        return ResponseEntity.ok(notifications);
    }






}
