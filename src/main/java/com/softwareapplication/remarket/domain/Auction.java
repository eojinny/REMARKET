package com.softwareapplication.remarket.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name="Auction")
@EntityListeners(AuditingEntityListener.class)
public class Auction {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "auction_Id")
    private Long auctionId;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedDate;

    @Column(name = "title")
    private String title;

    @Column(name = "auction_price")
    private int auctionPrice;

    @Column(name = "due_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dueDate;

    @Column(name = "content")
    private String content;

    @Column(name = "bid_price")
    private String bidPrice;

    @Column(name = "status")
    private String status;

    @OneToOne
    @JoinColumn(name="img_id")
    private Image  image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public void update(String status){
        this.status=status;
    }
    public void update(String status, int auctionPrice){
        this.status=status;
        this.auctionPrice=auctionPrice;
    }

}
