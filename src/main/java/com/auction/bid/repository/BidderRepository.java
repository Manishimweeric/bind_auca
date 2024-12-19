package com.auction.bid.repository;

import com.auction.bid.model.Bidder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BidderRepository extends JpaRepository<Bidder, Long> {
    Optional<Bidder> findByPhoneAndPassword(String phone, String password);
}
