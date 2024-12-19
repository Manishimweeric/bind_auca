package com.auction.bid.service;

import com.auction.bid.model.Bid;
import com.auction.bid.repository.BidRepository;
import com.auction.bid.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final ItemRepository itemRepository;

    public BidService(BidRepository bidRepository, ItemRepository itemRepository) {
        this.bidRepository = bidRepository;
        this.itemRepository = itemRepository;
    }

    public void placeBid(Long bidderId, Long itemId, Double bidAmount) {
        Bid bid = new Bid();
        bid.setBidderId(bidderId);
        bid.setItemId(itemId);
        bid.setBidAmount(bidAmount);
        bid.setStatus("Pending");
        bidRepository.save(bid);
    }

    public List<String> getNotificationsForBidder(Long bidderId) {
        return bidRepository.findByBidderId(bidderId).stream()
                .map(bid -> "Your bid for item ID " + bid.getItemId() + " is " + bid.getStatus())
                .collect(Collectors.toList());
    }

    public void saveBid(Bid bid) {
        bidRepository.save(bid);
    }

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    public void approveBid(Long id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bid ID: " + id));
        bid.setStatus("Approved");
        bidRepository.save(bid);
    }

    public void denyBid(Long id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bid ID: " + id));
        bid.setStatus("Denied");
        bidRepository.save(bid);
    }

    public List<Bid> getBidsByStatus(String status) {
        return bidRepository.findByStatusOrderByStatusAsc(status);
    }

    public List<Bid> getAllBidsSortedByStatus() {
        return bidRepository.findAllByOrderByStatusAsc();
    }
}
