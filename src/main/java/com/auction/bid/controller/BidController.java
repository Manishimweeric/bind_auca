package com.auction.bid.controller;

import com.auction.bid.model.Bid;
import com.auction.bid.model.Item;
import com.auction.bid.service.BidService;
import com.auction.bid.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bidder")
public class BidController {

    private final BidService bidService;
    private final ItemService itemService;

    public BidController(BidService bidService, ItemService itemService) {
        this.bidService = bidService;
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public ResponseEntity<List<Item>> showItemsForBidding() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @PostMapping("/bid")
    public ResponseEntity<String> placeBid(@RequestBody Bid bid) {
        bidService.saveBid(bid);
        return ResponseEntity.ok("Bid placed successfully!");
    }

    @GetMapping("/bids")
    public ResponseEntity<List<Bid>> viewBids(@RequestParam(required = false) String status) {
        List<Bid> bids;
        if (status != null && !status.isEmpty()) {
            bids = bidService.getBidsByStatus(status);
        } else {
            bids = bidService.getAllBidsSortedByStatus();
        }
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/bidHistory")
    public ResponseEntity<List<Bid>> viewBidHistory() {
        List<Bid> bidHistory = bidService.getAllBids();
        return ResponseEntity.ok(bidHistory);
    }
}
