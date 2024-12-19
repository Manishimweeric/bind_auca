package com.auction.bid.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @Column(name = "`read`", nullable = false)
    private boolean read=false;

    private long bidder;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public long getBidder() {
        return bidder;
    }

    public void setBidder(long bidder) {
        this.bidder = bidder;
    }
}


