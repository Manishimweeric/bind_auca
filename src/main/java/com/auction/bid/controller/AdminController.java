package com.auction.bid.controller;

import com.auction.bid.model.Item;
import com.auction.bid.model.Bid;
import com.auction.bid.repository.ItemRepository;
import com.auction.bid.service.BidService;
import com.auction.bid.service.ItemService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ItemService itemService;
    private final BidService bidService;
    private  ItemRepository itemRepository;

    public AdminController(ItemService itemService, BidService bidService) {
        this.itemService = itemService;
        this.bidService = bidService;
    }

    private final String adminUsername = "auction_admin";
    private final String adminPassword = "password123";

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }
    @PostMapping("/items")
    public ResponseEntity<String> createItem(@ModelAttribute Item item, @RequestParam("image") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Photo is required for the item.");
        }

        try {
            // Define the folder path to save the image
            String uploadDir = "uploads/images/";

            // Create the directory if it doesn't exist
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create a unique file name using UUID and the original filename
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Save the file to the folder
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());

            // Set the file name (not the path) in the item's data
            item.setPhoto(fileName);

            // Save the item with the image name in the database
            itemService.saveItem(item);

            return ResponseEntity.ok("Item created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving the image.");
        }
    }

    @GetMapping("/items/{id}/image")
    public ResponseEntity<Resource> getItemImage(@PathVariable Long id) {
        Optional<Item> itemOpt = itemService.getItemById(id);

        if (itemOpt.isPresent() && itemOpt.get().getPhoto() != null) {
            String imageName = itemOpt.get().getPhoto();
            String imagePath = "uploads/images/" + imageName; // Construct the full path to the image

            try {
                Path path = Paths.get(imagePath);
                Resource resource = new FileSystemResource(path);

                if (resource.exists()) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)  // Update with the appropriate MIME type if needed
                            .body(resource);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemService.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<String> updateItem(@PathVariable Long id, @ModelAttribute Item item) {
        // Log the incoming data
        System.out.println("Incoming ID from URL: " + id);
        System.out.println("Incoming Item Data - ID: " + item.getId() + ", Name: " + item.getName() + ", Price: " + item.getPrice());

        // Update the item
        itemService.updateItem(id, item);

        return ResponseEntity.ok("Item updated successfully!");
    }


    @DeleteMapping("/items/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok("Item deleted successfully!");
    }

    @GetMapping("/bids")
    public ResponseEntity<List<Bid>> viewBids(@RequestParam(required = false) String status) {
        List<Bid> bids = (status != null && !status.isEmpty())
                ? bidService.getBidsByStatus(status)
                : bidService.getAllBidsSortedByStatus();
        return ResponseEntity.ok(bids);
    }

    @PostMapping("/bids/approve/{id}")
    public ResponseEntity<String> approveBid(@PathVariable Long id) {
        bidService.approveBid(id);
        return ResponseEntity.ok("Bid approved successfully!");
    }

    @PostMapping("/bids/deny/{id}")
    public ResponseEntity<String> denyBid(@PathVariable Long id) {
        bidService.denyBid(id);
        return ResponseEntity.ok("Bid denied successfully!");
    }
}
