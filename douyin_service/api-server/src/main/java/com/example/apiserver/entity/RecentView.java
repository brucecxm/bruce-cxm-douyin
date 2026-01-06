package com.example.apiserver.entity;

import javax.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "recent_view")
public class RecentView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "viewed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date viewedAt;

    @PrePersist
    protected void onCreate() {
        viewedAt = new Date();
    }
}