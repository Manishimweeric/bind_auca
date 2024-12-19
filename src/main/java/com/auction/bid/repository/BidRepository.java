package com.auction.bid.repository;

import com.auction.bid.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByBidderId(Long bidderId);

    List<Bid> findByStatusOrderByStatusAsc(String status);

    List<Bid> findAllByOrderByStatusAsc();
}
