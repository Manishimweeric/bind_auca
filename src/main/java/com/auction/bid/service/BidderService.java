package com.auction.bid.service;

import com.auction.bid.model.Bidder;
import com.auction.bid.repository.BidderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidderService {

    private final BidderRepository bidderRepository;

    public BidderService(BidderRepository bidderRepository) {
        this.bidderRepository = bidderRepository;
    }

    public void saveBidder(Bidder bidder) {
        bidderRepository.save(bidder);
    }

    public Bidder authenticate(String phone, String password) {
        return bidderRepository.findByPhoneAndPassword(phone, password).orElse(null);
    }


}
